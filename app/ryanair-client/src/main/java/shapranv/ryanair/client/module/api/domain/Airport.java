package shapranv.ryanair.client.module.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class Airport {
    private String code;
    private String name;
    private List<String> aliases;
    private City city;
    private Country country;
    private Coordinates coordinates;
}
