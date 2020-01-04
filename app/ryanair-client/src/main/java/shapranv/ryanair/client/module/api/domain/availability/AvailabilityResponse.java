package shapranv.ryanair.client.module.api.domain.availability;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AvailabilityResponse {
    public String currency;
    public int currPrecision;
    public List<Trip> trips;
    public Date serverTimeUTC;
}
