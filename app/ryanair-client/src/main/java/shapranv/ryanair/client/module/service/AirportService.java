package shapranv.ryanair.client.module.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import shapranv.ryanair.client.module.api.domain.Airport;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
public class AirportService {
    private final ObjectMapper objectMapper;
    private final JavaType inputType;
    private final AtomicReference<Map<String, Airport>> airports = new AtomicReference<>(Collections.emptyMap());

    public AirportService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.writerWithDefaultPrettyPrinter();
        this.inputType = objectMapper.getTypeFactory().constructCollectionType(List.class, Airport.class);
    }

    public void refreshCache(String json) {
        List<Airport> update;

        try {
            update = objectMapper.readValue(json, inputType);
        } catch (Exception e) {
            log.error("Can't process input", e);
            return;
        }

        airports.set(update.stream().collect(Collectors.toMap(Airport::getCode, Function.identity())));
        log.error("Airports updated. Cache size: [{}]", airports.get().size());
    }
}
