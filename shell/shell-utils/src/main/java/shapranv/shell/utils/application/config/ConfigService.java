package shapranv.shell.utils.application.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import shapranv.shell.utils.collections.CollectionUtils;

import java.util.Map;
import java.util.function.Function;

import static shapranv.shell.utils.SystemUtils.getSystemProperty;

@Log4j2
public class ConfigService {

    private final static Function<String, String> EMPTY_MAPPER = value -> value;

    @Getter
    private final static ConfigService instance = new ConfigService();

    private final Map<String, String> properties = CollectionUtils.concurrentMap();

    private ConfigService() {
    }

    public String get(String name) {
        return get(name, EMPTY_MAPPER, null);
    }

    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return get(name, Boolean::valueOf, defaultValue);
    }

    public long getLong(String name) {
        return getLong(name, 0);
    }

    public long getLong(String name, long defaultValue) {
        return get(name, Long::valueOf, defaultValue);
    }

    private <T> T get(String name, Function<String, T> mapper, T defaultValue) {
        String value = properties.getOrDefault(name, getSystemProperty(name));
        return value == null ? defaultValue : mapper.apply(value);
    }

    public void setProperty(String name, String value) {
        if (value == null) {
            String previousValue = properties.remove(name);

            if (previousValue != null) {
                log.info("Property removed: {}", name);
            }
        } else {
            String previousValue = properties.put(name, value);

            if (previousValue == null) {
                log.info("Property added: {}={}", name, value);
            } else {
                log.info("Property updated: {} {}->{}", name, previousValue, value);
            }
        }
    }
}
