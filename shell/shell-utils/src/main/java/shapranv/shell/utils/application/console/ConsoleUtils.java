package shapranv.shell.utils.application.console;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.console.command.ConsoleCommand;

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

    public static void printCommandInfo(ConsoleCommand command, Logger logger) {
        printCommandInfo(command, command.getDescription(), logger);
    }

    public static void printCommandInfo(ConsoleCommand command, String description, Logger logger) {
        logger.info("[{}] {}", command.getCode(), description);
    }
}
