package shapranv.ryanair.client.adapter.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import shapranv.shell.utils.collections.CollectionUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class CachedDayDeserializer extends StdDeserializer<LocalDate> {
    private final Map<String, LocalDate> dateCache = CollectionUtils.concurrentMap();

    public CachedDayDeserializer() {
        super((Class<?>) null);
    }

    public CachedDayDeserializer(Class<?> type) {
        super(type);
    }

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String day = parser.readValueAs(String.class);

        return dateCache.computeIfAbsent(day, LocalDate::parse);
    }
}
