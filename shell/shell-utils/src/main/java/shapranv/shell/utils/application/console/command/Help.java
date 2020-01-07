package shapranv.shell.utils.application.console.command;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.console.ConsoleListener;

import java.io.BufferedReader;

public class Help  extends ConsoleCommand {
    public Help() {
        super("-h", "Help");
    }

    @Override
    public boolean execute(ConsoleListener listener, BufferedReader console, Logger logger) throws Exception {
        listener.printHelp(logger);
        return true;
    }

}
