package shapranv.ryanair.client.module.service;

import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import shapranv.ryanair.client.module.api.domain.availability.AvailabilityResponse;
import shapranv.shell.utils.collections.CollectionUtils;
import shapranv.shell.utils.service.HttpDataLoader;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static shapranv.shell.utils.http.RequestParameter.*;

@Log4j2
public class AvailabilityService extends HttpDataLoader {
    private final AirportService airportService;
    private final RouteService routeService;

    private final JavaType inputType;

    private final Queue<String> airportsToLoad = new LinkedBlockingQueue<>();
    private volatile String currentAirport = null;
    private final Queue<String> routesToLoad = new LinkedBlockingQueue<>();
    private final Map<Long, String> currentlyLoading = CollectionUtils.concurrentMap();

    public AvailabilityService(AirportService airportService, RouteService routeService) {
        super("availability");
        this.airportService = airportService;
        this.routeService = routeService;
        this.inputType = objectMapper.getTypeFactory().constructType(AvailabilityResponse.class);
    }

    @Override
    public String getName() {
        return "Availability service";
    }

    public void onRoutesUpdated() {
        if (airportsToLoad.isEmpty()) {
            airportService.getAllAirportCodes().forEach(airportsToLoad::offer);
            start();
        } else {
            log.warn("Flight availability is still loading. Skipping update...");
        }
    }

    @Override
    protected void loadData() {
        if (airportsToLoad.isEmpty() && routesToLoad.isEmpty() && currentlyLoading.isEmpty()) {
            log.warn("Flight availability loaded. Stopping service...");
            stop();
        } else {
            super.loadData();
        }
    }

    @Override
    protected String getHttpRequest(long requestId) {
        if (routesToLoad.isEmpty()) {
            currentAirport = airportsToLoad.poll();
            routeService.getRoutesFrom(currentAirport).forEach(route ->
                    routesToLoad.add(route.getArrivalAirport().getCode())
            );
        }
        //TODO: Set origin/destination dynamically
        String destination = routesToLoad.poll();
        String request = requestBuilder.clear()
                .addParam(ADT, 1)
                .addParam(CHD, 0)
                .addParam(INF, 0)
                .addParam(TEEN, 0)
                .addParam(DATE_IN, "2020-02-21")
                .addParam(DATE_OUT, "2020-02-18")
                .addParam(ORIGIN, /*currentAirport*/"KRK")
                .addParam(DESTINATION, /*routesToLoad.poll()*/"STN")
                .addParam(ROUND_TRIP, true)
                .addParam(FLEX_DAYS_IN, 2)
                .addParam(FLEX_DAYS_BEFORE_IN, 2)
                .addParam(FLEX_DAYS_OUT, 2)
                .addParam(FLEX_DAYS_BEFORE_OUT, 2)
                .addParam(TO_US, "AGREED")
                .addParam(PROMO_CODE, StringUtils.EMPTY)
                .addParam(INCLUDE_CONNECTING_FLIGHTS, false)
                .buildRequest();


        currentlyLoading.put(requestId, request);

        return request;
    }

    @Override
    protected void refreshCache(long requestId, String httpResponse) {
        if (StringUtils.isBlank(httpResponse)) {
            resendRequest(requestId);
            return;
        }

        AvailabilityResponse update;

        try {
            update = objectMapper.readValue(httpResponse, inputType);
        } catch (Exception e) {
            log.error("Can't process http response", e);
            resendRequest(requestId);
            return;
        }

        currentlyLoading.remove(requestId);
    }

    private void resendRequest(long requestId) {
        currentlyLoading.remove(requestId);
    }

    @Override
    protected int getCacheSize() {
        return 0;
    }
}
