package shapranv.shell.utils.application;

import java.util.function.Function;

public class ApplicationSettings {

    public String getProperty(String name) {
        return getSystemProperty(name);
    }

    public boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    public boolean getBooleanProperty(String name, boolean defaultValue) {
        return getProperty(name, Boolean::valueOf, defaultValue);
    }

    public long getLongProperty(String name) {
        return getLongProperty(name, 0);
    }

    public long getLongProperty(String name, long defaultValue) {
        return getProperty(name, Long::valueOf, defaultValue);
    }

    private <T> T getProperty(String name, Function<String, T> mapper, T defaultValue) {
        String value = getSystemProperty(name);
        return value == null ? defaultValue : mapper.apply(value);
    }

    private String getSystemProperty(String name) {
        return System.getProperties().getProperty(name, System.getenv(name));
    }
}
