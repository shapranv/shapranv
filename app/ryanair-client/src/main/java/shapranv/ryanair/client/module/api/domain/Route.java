package shapranv.ryanair.client.module.api.domain;

import lombok.Data;

@Data
public class Route {
    private Airport arrivalAirport;
    private Airport connectingAirport;
    private String operator;
}
