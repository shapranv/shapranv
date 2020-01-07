package shapranv.shell.utils.application.console;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.console.command.ConsoleCommand;

import java.io.BufferedReader;
import java.util.List;

import static shapranv.shell.utils.application.console.ConsoleUtils.FOOTER_LINE;
import static shapranv.shell.utils.application.console.ConsoleUtils.getHeader;
import static shapranv.shell.utils.application.console.ConsoleUtils.printCommandInfo;
import static shapranv.shell.utils.application.console.command.SystemCommands.*;

public abstract class ConsoleListener {
    private final List<ConsoleCommand> commands;

    protected ConsoleListener() {
        this.commands = createCommands();
    }

    protected abstract String getName();

    protected abstract List<ConsoleCommand> createCommands();

    public void listen(BufferedReader console, Logger logger) {
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

    public void printHelp(Logger logger) {
        logger.info(getHeader(getName()));
        printMenu(logger);
        logger.info(FOOTER_LINE);
    }

    protected void printMenu(Logger logger) {
        commands.forEach(command -> printCommandInfo(command, logger));
    }

    protected boolean processCommand(BufferedReader console, String command, Logger logger) throws Exception {
        return findCommand(command).execute(this, console, logger);
    }

    private ConsoleCommand findCommand(String code) {
        return commands.stream().filter(command -> code.equals(command.getCode())).findFirst().orElse(UNDEF);
    }
}
