package shapranv.shell.utils;

import java.util.function.Function;

public class SystemUtils {
    public static String getProperty(String name) {
        return getSystemProperty(name);
    }

    public static boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        return getProperty(name, Boolean::valueOf, defaultValue);
    }

    public static long getLongProperty(String name) {
        return getLongProperty(name, 0);
    }

    public static long getLongProperty(String name, long defaultValue) {
        return getProperty(name, Long::valueOf, defaultValue);
    }

    private static <T> T getProperty(String name, Function<String, T> mapper, T defaultValue) {
        String value = getSystemProperty(name);
        return value == null ? defaultValue : mapper.apply(value);
    }

    private static String getSystemProperty(String name) {
        return System.getProperties().getProperty(name, System.getenv(name));
    }
}
