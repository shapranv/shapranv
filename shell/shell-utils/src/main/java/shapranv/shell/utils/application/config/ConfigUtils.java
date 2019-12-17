package shapranv.shell.utils.application.config;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.lang3.StringUtils;
import shapranv.shell.utils.SystemUtils;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class ConfigUtils {
    private static final String SYS_PROP_PREFIX = "sys.";

    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String PLACEHOLDER_SUFFIX = "}";

    public static void loadConfig(String resourcePath, BiConsumer<String, String> consumer) {
        try {
            ImmutableConfiguration configuration = toConfiguration(resourcePath);
            Iterator<String> keys = configuration.getKeys();

            while (keys.hasNext()) {
                String key = keys.next();
                Object property = configuration.getProperty(key);
                String value = property instanceof List
                        ? StringUtils.joinWith(",", (List)property).trim()
                        : property.toString().trim();

                value = replacePlaceholders(value);

                if (isSystemProperty(value)) {
                    System.setProperty(key, strip(value));
                }

                consumer.accept(key, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to load configuration from " + resourcePath, e);
        }
    }

    private static ImmutableConfiguration toConfiguration(String resourcePath) throws Exception {
        URL url = SystemUtils.getResourceUrl(resourcePath);

        return new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters().properties().setURL(url))
                .getConfiguration();
    }

    private static String replacePlaceholders(String value) {
        while (value.contains(PLACEHOLDER_PREFIX)) {
            int start = value.indexOf(PLACEHOLDER_PREFIX) + PLACEHOLDER_PREFIX.length();
            int end = value.indexOf(PLACEHOLDER_SUFFIX);
            String placeholder = value.substring(start, end);
            String placeholderValue = Objects.requireNonNull(
                    SystemUtils.getSystemProperty(placeholder), "Placeholder [" + placeholder + "] not found!"
            );
            value = value.replace(PLACEHOLDER_PREFIX + placeholder + PLACEHOLDER_SUFFIX, placeholderValue);
        }

        return value.trim();
    }

    private static boolean isSystemProperty(String value) {
        return value.startsWith(SYS_PROP_PREFIX);
    }

    private static String strip(String value) {
        return value.replace(SYS_PROP_PREFIX, StringUtils.EMPTY).trim();
    }
}
