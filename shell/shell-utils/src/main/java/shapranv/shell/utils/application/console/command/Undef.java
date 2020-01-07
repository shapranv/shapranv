package shapranv.shell.utils.application.console.command;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.console.ConsoleListener;

import java.io.BufferedReader;

public class Undef extends ConsoleCommand {
    public Undef() {
        super("", "");
    }

    @Override
    public boolean execute(ConsoleListener listener, BufferedReader console, Logger logger) throws Exception {
        logger.info("Unknown command...");
        listener.printHelp(logger);
        return true;
    }
}
