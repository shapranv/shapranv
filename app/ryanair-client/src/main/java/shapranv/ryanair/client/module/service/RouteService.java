package shapranv.ryanair.client.module.service;

import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import shapranv.ryanair.client.module.api.domain.Airport;
import shapranv.ryanair.client.module.api.domain.Route;
import shapranv.shell.utils.collections.CollectionUtils;
import shapranv.shell.utils.service.HttpStaticDataLoader;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static shapranv.shell.utils.http.RequestParameter.*;

@Log4j2
public class RouteService extends HttpStaticDataLoader {
    private final JavaType inputType;

    private final Map<String, List<Route>> routes = CollectionUtils.concurrentMap();

    private final Queue<String> airportsToLoad = new LinkedBlockingQueue<>();
    private final Map<Long, String> currentlyLoading = CollectionUtils.concurrentMap();

    private final Set<Runnable> updateListeners = CollectionUtils.setFromConcurrentMap();

    public RouteService() {
        super("routes");
        this.inputType = objectMapper.getTypeFactory().constructCollectionType(List.class, Route.class);
    }

    public void addUpdateListener(Runnable updateListener) {
        this.updateListeners.add(updateListener);
    }

    @Override
    public String getName() {
        return "Routes service";
    }

    public void onAirportsUpdated(Map<String, Airport> airports) {
        if (airportsToLoad.isEmpty()) {
            airports.keySet().forEach(airportsToLoad::offer);
            start();
        } else {
            log.warn("Airports are still loading. Skipping update...");
        }
    }

    @Override
    protected void loadData() {
        if (airportsToLoad.isEmpty() && currentlyLoading.isEmpty()) {
            log.warn("Airports loaded. Stopping service...");
            stop();
            updateListeners.forEach(Runnable::run);
        } else {
            super.loadData();
        }
    }

    @Override
    protected String getHttpRequest(long requestId) {
        currentlyLoading.put(requestId, airportsToLoad.peek());
        return requestBuilder.clear()
                .addParam(ARRIVAL_PHRASE, StringUtils.EMPTY)
                .addParam(DEPARTURE_PHRASE, airportsToLoad.poll())
                .addParam(MARKET, "en-ie")
                .buildRequest();
    }

    @Override
    protected void refreshCache(long requestId, String httpResponse) {
        if (StringUtils.isBlank(httpResponse)) {
            resendRequest(requestId);
            return;
        }

        List<Route> update;

        try {
            update = objectMapper.readValue(httpResponse, inputType);
        } catch (Exception e) {
            log.error("Can't process http response", e);
            resendRequest(requestId);
            return;
        }

        String airportCode = currentlyLoading.remove(requestId);
        routes.put(airportCode, update);
        log.info("Routes from {} updated. [{}] destinations loaded.", airportCode, update.size());
    }

    private void resendRequest(long requestId) {
        airportsToLoad.add(currentlyLoading.get(requestId));
        currentlyLoading.remove(requestId);
    }

    @Override
    protected int getCacheSize() {
        return routes.values().stream().map(List::size).reduce(0, Integer::sum);
    }

    public List<Route> getRoutesFrom(String airport) {
        if (!routes.containsKey(airport)) {
            log.error("Unknown airport code: {}", airport);
        }
        return routes.getOrDefault(airport, Collections.emptyList());
    }
}
