package shapranv.ryanair.client.module.api.domain.availability;

import lombok.Data;

@Data
public class Fare {
    public String type;
    public double amount;
    public int count;
    public boolean hasDiscount;
    public double publishedFare;
    public int discountInPercent;
    public boolean hasPromoDiscount;
    public double discountAmount;
}
