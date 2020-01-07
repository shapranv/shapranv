package ryanair.client.adapter.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import shapranv.ryanair.client.api.domain.fares.Fare;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class CachedDayDeserializerTest {
    private ObjectMapper objectMapper;
    private JavaType inputType;

    @Before
    public void init() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.writerWithDefaultPrettyPrinter();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.inputType = objectMapper.getTypeFactory().constructType(Fare.class);
    }

    @Test
    public void shouldDeserializeCachedDay() throws Exception {
        String json = "{\"day\":\"2020-04-01\",\"arrivalDate\":null,\"departureDate\":null,\"price\":null,\"soldOut\":false,\"sellKey\":null,\"unavailable\":true}";

        Fare fare1 = objectMapper.readValue(json, inputType);
        Fare fare2 = objectMapper.readValue(json, inputType);

        assertNotNull(fare1);
        assertNotNull(fare2);
        assertTrue(fare1.getDay() == fare2.getDay());
    }
}
