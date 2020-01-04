package shapranv.ryanair.client.module.api.domain.availability;

import lombok.Data;

import java.util.List;

@Data
public class RegularFare {
    public String fareKey;
    public String fareClass;
    public List<Fare> fares;
}
