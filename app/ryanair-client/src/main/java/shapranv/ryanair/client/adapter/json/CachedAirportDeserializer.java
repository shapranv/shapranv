package shapranv.ryanair.client.adapter.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import shapranv.ryanair.client.api.domain.Airport;
import shapranv.ryanair.client.service.AirportService;
import shapranv.shell.utils.application.Environment;

import java.io.IOException;

public class CachedAirportDeserializer extends StdDeserializer<Airport> {
    public CachedAirportDeserializer() {
        super((Class<?>) null);
    }

    public CachedAirportDeserializer(Class<?> type) {
        super(type);
    }

    @Override
    public Airport deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        AirportService airportService = Environment.getInstance().getService(AirportService.class);
        JsonNode node = parser.getCodec().readTree(parser);
        String code = node.get("code").asText();

        return airportService.getAirport(code);
    }
}
