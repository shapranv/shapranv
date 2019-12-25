package shapranv.shell.utils.application.console;

import org.apache.commons.lang3.StringUtils;

public class ConsoleUtils {
    public static final String FOOTER_LINE = "------------------------------------------";

    public static String getHeader(String serviceName) {
        int prefixLength = serviceName.length() >= FOOTER_LINE.length()
                ? 0
                : (FOOTER_LINE.length() - serviceName.length()) / 2 - 1;
        String prefix = StringUtils.repeat("-", prefixLength);
        int suffixLength = FOOTER_LINE.length() - serviceName.length() - prefix.length() - 2;
        String suffix = StringUtils.repeat("-", suffixLength);

        return prefix + " " + serviceName + " " + suffix;
    }
}
