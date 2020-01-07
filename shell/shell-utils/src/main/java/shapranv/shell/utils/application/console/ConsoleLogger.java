package shapranv.shell.utils.application.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleLogger {
    private static final String name = "ConsoleLogger";

    private static final Logger logger = LogManager.getLogger(name);

    public static Logger getLogger() {
        return logger;
    }
}
