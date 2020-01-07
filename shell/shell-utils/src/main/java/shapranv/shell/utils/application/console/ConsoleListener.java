package shapranv.shell.utils.application.console;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.config.ConfigService;

import java.io.BufferedReader;

import static shapranv.shell.utils.application.console.ConsoleUtils.FOOTER_LINE;
import static shapranv.shell.utils.application.console.ConsoleUtils.getHeader;

public interface ConsoleListener {
    String getName();

    default void listen(BufferedReader console, Logger logger) {
        printHelp(logger);

        while (true) {
            try {
                String command = console.readLine();
                if(!processCommand(console, command, logger)) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    default void printHelp(Logger logger) {
        logger.info(getHeader(getName()));
        printMenu(logger);
        logger.info(FOOTER_LINE);
    }

    default void printMenu(Logger logger) {
        //do nothing
    }

    default void printStatus(Logger logger) {
        //do nothing
    }

    default boolean processCommand(BufferedReader console, String command, Logger logger) throws Exception {
        switch (ConsoleCommand.findByCode(command)) {
            case EXIT:
                return false;
            case SET_PROPERTY:
                logger.info("Property:");
                String property = console.readLine();
                logger.info("Value:");
                String value = console.readLine();
                ConfigService.getInstance().setProperty(property, value);
                logger.info("System property updated: " + property + "=" + value);
                return true;
            case UNDEF:
            case HELP:
                printHelp(logger);
                break;
        }

        return true;
    }
}
