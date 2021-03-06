package shapranv.ryanair.client.api.domain.availability;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TripDate {
    public Date dateOut;
    public List<Flight> flights;
}
