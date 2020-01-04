package shapranv.ryanair.client.module.api.domain.availability;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Flight {
    public int faresLeft;
    public String flightKey;
    public int infantsLeft;
    public RegularFare regularFare;
    public String operatedBy;
    public List<Segment> segments;
    public String flightNumber;
    public List<Date> time;
    public List<Date> timeUTC;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm")
    public Date duration;
}
