package shapranv.ryanair.client.module.api.domain.fares;

import lombok.Data;

@Data
public class Price {
    public double value;
    public int valueMainUnit;
    public int valueFractionalUnit;
    public String currencyCode;
    public String currencySymbol;
}
