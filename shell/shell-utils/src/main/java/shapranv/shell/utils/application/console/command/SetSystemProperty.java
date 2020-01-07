package shapranv.shell.utils.application.console.command;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.config.ConfigService;
import shapranv.shell.utils.application.console.ConsoleListener;

import java.io.BufferedReader;

public class SetSystemProperty extends ConsoleCommand {

    public SetSystemProperty() {
        super("-property", "Set system property");
    }

    @Override
    public boolean execute(ConsoleListener listener, BufferedReader console, Logger logger) throws Exception {
        logger.info("Property:");
        String property = console.readLine();
        logger.info("Value:");
        String value = console.readLine();
        ConfigService.getInstance().setProperty(property, value);
        logger.info("System property updated: {}={}", property, value);

        return true;
    }
}
