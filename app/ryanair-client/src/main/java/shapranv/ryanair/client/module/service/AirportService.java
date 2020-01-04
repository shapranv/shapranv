package shapranv.ryanair.client.module.service;

import com.fasterxml.jackson.databind.JavaType;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import shapranv.ryanair.client.module.api.domain.Airport;
import shapranv.shell.utils.collections.CollectionUtils;
import shapranv.shell.utils.service.HttpStaticDataLoader;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static shapranv.shell.utils.http.RequestParameter.*;

@Log4j2
public class AirportService extends HttpStaticDataLoader {
    private final JavaType inputType;

    private final AtomicReference<Map<String, Airport>> airports = new AtomicReference<>(Collections.emptyMap());
    private final Set<Consumer<Map<String, Airport>>> updateListeners = CollectionUtils.setFromConcurrentMap();

    public AirportService() {
        super("airports");
        this.inputType = objectMapper.getTypeFactory().constructCollectionType(List.class, Airport.class);
    }

    public void addUpdateListener(Consumer<Map<String, Airport>> updateListener) {
        this.updateListeners.add(updateListener);
    }

    @Override
    protected String getHttpRequest(long requestId) {
        return requestBuilder.clear()
                .addParam(PHRASE, StringUtils.EMPTY)
                .addParam(MARKET, "en-ie") //TODO: Move to settings?
                .buildRequest();
    }

    @Override
    protected void refreshCache(long requestId, String httpResponse) {
        if (StringUtils.isBlank(httpResponse)) {
            return;
        }

        List<Airport> update;

        try {
            update = objectMapper.readValue(httpResponse, inputType);
        } catch (Exception e) {
            log.error("Can't process http response", e);
            return;
        }

        airports.set(update.stream().collect(Collectors.toMap(Airport::getCode, Function.identity())));
        log.info("Airports updated. Cache size: [{}]", airports.get().size());
        updateListeners.forEach(listener -> listener.accept(airports.get()));
    }

    @Override
    public String getName() {
        return "Airport service";
    }

    @Override
    protected int getCacheSize() {
        return airports.get().size();
    }

    public Set<String> getAllAirportCodes() {
        return airports.get().keySet();
    }
}
