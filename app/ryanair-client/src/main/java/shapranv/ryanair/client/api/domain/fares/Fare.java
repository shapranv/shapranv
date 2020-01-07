package shapranv.ryanair.client.api.domain.fares;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import shapranv.ryanair.client.adapter.json.CachedDayDeserializer;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Fare {
    @JsonDeserialize(using = CachedDayDeserializer.class)
    public LocalDate day;
    public Date arrivalDate;
    public Date departureDate;
    public Price price;
    public boolean soldOut;
    //public String sellKey;
    public boolean unavailable;

    public boolean isAvailable() {
        return !soldOut && !unavailable;
    }
}
