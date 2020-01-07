package shapranv.shell.utils.application.console.command;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.console.ConsoleListener;

import java.io.BufferedReader;

public class Exit extends ConsoleCommand {

    public Exit() {
        this("Stop application");
    }

    public Exit(String description) {
        super("-q", description);
    }

    @Override
    public boolean execute(ConsoleListener listener, BufferedReader console, Logger logger) throws Exception {
        return false;
    }
}
