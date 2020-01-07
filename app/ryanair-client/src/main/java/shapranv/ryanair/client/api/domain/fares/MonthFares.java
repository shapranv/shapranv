package shapranv.ryanair.client.api.domain.fares;

import lombok.Data;

import java.util.List;

@Data
public class MonthFares {
    public FareSet outbound;
    public FareSet inbound;

    @Data
    public static class FareSet {
        public List<Fare> fares;
        public Fare minFare;
        public Fare maxFare;
    }
}
