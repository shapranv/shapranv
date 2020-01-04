package shapranv.ryanair.client.module.api.domain.availability;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Segment {
    public int segmentNr;
    public String origin;
    public String destination;
    public String flightNumber;
    public List<Date> time;
    public List<Date> timeUTC;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm")
    public Date duration;
}
