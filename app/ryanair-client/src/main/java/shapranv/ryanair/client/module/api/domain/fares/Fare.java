package shapranv.ryanair.client.module.api.domain.fares;

import lombok.Data;

import java.util.Date;

@Data
public class Fare {
    public Date day;
    public Date arrivalDate;
    public Date departureDate;
    public Price price;
    public boolean soldOut;
    public String sellKey;
    public boolean unavailable;

    public boolean isAvailable() {
        return !soldOut && !unavailable;
    }
}
