package shapranv.shell.utils.application.config;

import java.util.function.BiConsumer;

public class ConfigUtils {
    private static final String SYS_PROP_PREFIX = "sys.";

    static String getSystemProperty(String name) {
        return System.getProperties().getProperty(name, System.getenv(name));
    }

    public static void loadConfig(String resourcePath, BiConsumer<String, String> consumer) {

    }
}
