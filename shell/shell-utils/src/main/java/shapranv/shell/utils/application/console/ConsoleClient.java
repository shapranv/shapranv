package shapranv.shell.utils.application.console;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.Environment;

import java.io.BufferedReader;

import static shapranv.shell.utils.application.console.ConsoleUtils.printCommandInfo;

public class ConsoleClient implements ConsoleListener {
    private final String appName;
    private final ServiceRegistry serviceRegistry;

    public ConsoleClient(String appName) {
        this.appName = appName;

        Environment env = Environment.getInstance();
        this.serviceRegistry = env.ensureService(ServiceRegistry.class);
    }

    @Override
    public String getName() {
        return appName;
    }

    @Override
    public boolean processCommand(BufferedReader console, String code, Logger logger) throws Exception {
        ConsoleCommand command = ConsoleCommand.findByCode(code);

        switch (command) {
            case SERVICES:
                serviceRegistry.listen(console, logger);
                printHelp(logger);
                return true;
        }

        return ConsoleListener.super.processCommand(console, code, logger);
    }

    @Override
    public void printMenu(Logger logger) {
        printCommandInfo(ConsoleCommand.HELP, logger);
        printCommandInfo(ConsoleCommand.SERVICES, logger);
        printCommandInfo(ConsoleCommand.SET_PROPERTY, logger);
        printCommandInfo(ConsoleCommand.EXIT, logger);
    }
}
