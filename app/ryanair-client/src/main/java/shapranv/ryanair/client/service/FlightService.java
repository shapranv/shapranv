package shapranv.ryanair.client.service;

import com.fasterxml.jackson.databind.JavaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import shapranv.ryanair.client.api.domain.fares.Fare;
import shapranv.ryanair.client.api.domain.fares.MonthFares;
import shapranv.shell.utils.collections.CollectionUtils;
import shapranv.shell.utils.concurrent.CachedStringBuilder;
import shapranv.shell.utils.service.HttpDataLoader;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static shapranv.shell.utils.http.RequestParameter.*;

@Log4j2
public class FlightService extends HttpDataLoader {
    private static final CachedStringBuilder cachedBuilder = CachedStringBuilder.of(32);

    private final AirportService airportService;
    private final RouteService routeService;

    private final JavaType inputType;
    private final LoaderContext context = new LoaderContext();
    private final Map<Long, MonthFaresRequest> currentlyLoading = CollectionUtils.concurrentMap();

    private AtomicReference<Map<String, Map<String, Map<Date, Fare>>>> faresCache = new AtomicReference<>(
            CollectionUtils.concurrentMap()
    );

    public FlightService(AirportService airportService, RouteService routeService) {
        super("flights");
        this.airportService = airportService;
        this.routeService = routeService;
        this.inputType = objectMapper.getTypeFactory().constructType(MonthFares.class);
    }

    public void onRoutesUpdated() {
        if (context.isEmpty()) {
            context.loadAirports(airportService.getAllAirportCodes());
            start();
        } else {
            log.warn("Flights are still loading. Skipping update...");
        }
    }

    @Override
    protected void loadData() {
        if (context.isEmpty() && currentlyLoading.isEmpty()) {
            log.warn("Flights are loaded. Stopping service...");
            faresCache.set(context.loadedFares);
            stop();
        } else {
            super.loadData();
        }
    }

    @Override
    protected String getHttpRequest(long requestId) {
        MonthFaresRequest request = context.nextMonthFaresRequest();

        if (request == null) {
            return null;
        }

        currentlyLoading.put(requestId, request);
        String requestSuffix = cachedBuilder.getBuilder()
                .append("/")
                .append(request.from)
                .append("/")
                .append(request.to)
                .append("/cheapestPerDay")
                .toString();

        String monthOfDate = request.month.toString();

        return requestBuilder.clear()
                .requestSuffix(requestSuffix)
                .addParam(MARKET, "en-ie") //TODO: Move to settings?
                .addParam(INBOUND_MONTH_OF_DATE, monthOfDate)
                .addParam(OUTBOUND_MONTH_OF_DATE, monthOfDate)
                .buildRequest();
    }

    @Override
    protected void refreshCache(long requestId, String httpResponse) {
        if (StringUtils.isBlank(httpResponse)) {
            resendRequest(requestId);
            return;
        }

        MonthFares update;

        try {
            update = objectMapper.readValue(httpResponse, inputType);
        } catch (Exception e) {
            log.error("Can't process http response", e);
            resendRequest(requestId);
            return;
        }

        MonthFaresRequest request = currentlyLoading.remove(requestId);

        update.getOutbound().getFares().stream()
                .filter(Fare::isAvailable)
                .forEach(outboundFare ->
                        context.loadedFares.computeIfAbsent(request.from, from -> CollectionUtils.concurrentMap())
                                .computeIfAbsent(request.to, to -> CollectionUtils.concurrentMap())
                                .put(outboundFare.getDepartureDate(), outboundFare)
                );

        update.getInbound().getFares().stream()
                .filter(Fare::isAvailable)
                .forEach(inboundFare ->
                        context.loadedFares.computeIfAbsent(request.to, from -> CollectionUtils.concurrentMap())
                                .computeIfAbsent(request.from, to -> CollectionUtils.concurrentMap())
                                .put(inboundFare.getDepartureDate(), inboundFare)
                );

        log.info("Month fares loaded: {}->{}", request.from, request.to);
    }

    @Override
    public String getName() {
        return "Flights service";
    }

    @Override
    protected int getCacheSize() {
        return context.loadedFares.values().stream()
                .map(fares -> fares.values().stream().map(Map::size).reduce(0, Integer::sum)
                ).reduce(0, Integer::sum);
    }

    private void resendRequest(long requestId) {
        currentlyLoading.remove(requestId);
    }

    @Override
    public void printStatus(Consumer<String> printer) {
        super.printStatus(printer);

        int total = routeService.getCacheSize();
        int loaded = context.loadedFaresCounter.get();

        if (loaded < total) {
            printer.accept("Loading progress: " + (int)(((double)loaded / total) * 100) + "% [" + loaded + "/" + total + "]");
        } else {
            printer.accept("Loading finished: [" + loaded + "/" + total + "]");
        }
    }

    private class LoaderContext {
        private final Queue<String> airportsToLoad = new LinkedList<>();
        private final Queue<String> routesToLoad = new LinkedList<>();
        private String currentAirport = null;

        private AtomicInteger loadedFaresCounter = new AtomicInteger(0);
        private Map<String, Map<String, Map<Date, Fare>>> loadedFares = CollectionUtils.concurrentMap();

        private void loadAirports(Set<String> airports) {
            airports.forEach(airportsToLoad::offer);
            this.loadedFares = CollectionUtils.concurrentMap();
            this.loadedFaresCounter.set(0);
        }

        private synchronized MonthFaresRequest nextMonthFaresRequest() {
            while (routesToLoad.isEmpty() && !airportsToLoad.isEmpty()) {
                currentAirport = airportsToLoad.poll();
                loadedFares.computeIfAbsent(currentAirport, key -> CollectionUtils.concurrentMap());
                routeService.getRoutesFrom(currentAirport).forEach(route ->
                        routesToLoad.add(route.getArrivalAirport().getCode())
                );
                log.info("Loading fares from {}. Destinations to load: {}", currentAirport, routesToLoad.size());
            }

            while (!routesToLoad.isEmpty()) {
                String destinationToLoad = routesToLoad.poll();
                loadedFaresCounter.incrementAndGet();

                if (!loadedFares.get(currentAirport).containsKey(destinationToLoad)) {
                    return new MonthFaresRequest(currentAirport, destinationToLoad, LocalDate.now());
                } else {
                    log.info("{}->{} fares already loaded. Skipping...", currentAirport, destinationToLoad);
                }
            }

            return null;
        }

        private synchronized boolean isEmpty() {
            return airportsToLoad.isEmpty() && routesToLoad.isEmpty();
        }
    }

    @RequiredArgsConstructor
    private class MonthFaresRequest {
        private final String from;
        private final String to;
        private final LocalDate month;
    }
}
