package shapranv.shell.utils.application.console.command;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.console.ConsoleListener;

import java.io.BufferedReader;

public abstract class ConsoleCommand {
    private String code;
    private String description;

    public ConsoleCommand(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public abstract boolean execute(ConsoleListener listener, BufferedReader console, Logger logger) throws Exception;
}
