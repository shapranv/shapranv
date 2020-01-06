package shapranv.ryanair.client.api.domain.availability;

import lombok.Data;

import java.util.List;

@Data
public class Trip {
    public String origin;
    public String originName;
    public String destination;
    public String destinationName;
    public String routeGroup;
    public String tripType;
    public String upgradeType;
    public List<TripDate> dates;
}
