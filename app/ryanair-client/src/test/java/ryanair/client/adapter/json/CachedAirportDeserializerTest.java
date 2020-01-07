package ryanair.client.adapter.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import shapranv.ryanair.client.api.domain.Airport;
import shapranv.ryanair.client.api.domain.Route;
import shapranv.ryanair.client.service.AirportService;
import shapranv.shell.utils.application.Environment;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CachedAirportDeserializerTest {
    private ObjectMapper objectMapper;
    private JavaType inputType;

    @Mock
    private AirportService airportService;

    @Before
    public void init() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.writerWithDefaultPrettyPrinter();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.inputType = objectMapper.getTypeFactory().constructCollectionType(List.class, Route.class);

        Environment environment = Environment.getInstance();
        environment.addService(airportService, AirportService.class);
    }

    @Test
    public void shouldDeserializeCachedAirport() throws Exception {
        String json = "[{\"arrivalAirport\":{\"code\":\"AGP\",\"name\":\"Malaga\",\"aliases\":[],\"city\":{\"code\":\"MALAGA\",\"name\":\"Malaga\"},\"country\":{\"code\":\"es\",\"name\":\"Spain\"},\"coordinates\":{\"latitude\":36.6749,\"longitude\":-4.49911}},\"connectingAirport\":null,\"operator\":\"RYANAIR\"}]";

        Airport agp = mock(Airport.class);
        when(airportService.getAirport(eq("AGP"))).thenReturn(agp);

        List<Route> routes = objectMapper.readValue(json, inputType);

        assertEquals(routes.size(), 1);
        assertTrue(routes.get(0).getArrivalAirport() == agp);
    }
}
