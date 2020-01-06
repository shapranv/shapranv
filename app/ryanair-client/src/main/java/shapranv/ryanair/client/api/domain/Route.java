package shapranv.ryanair.client.api.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import shapranv.ryanair.client.adapter.json.CachedAirportDeserializer;

@Data
public class Route {
    @JsonDeserialize(using = CachedAirportDeserializer.class)
    private Airport arrivalAirport;
    @JsonDeserialize(using = CachedAirportDeserializer.class)
    private Airport connectingAirport;
    private String operator;
}
