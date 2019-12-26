package shapranv.shell.utils.application.console;

import shapranv.shell.utils.application.Environment;

import java.io.BufferedReader;
import java.util.function.Consumer;

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
    public boolean processCommand(BufferedReader console, String code, Consumer<String> printer) throws Exception {
        ConsoleCommand command = ConsoleCommand.findByCode(code);

        switch (command) {
            case SERVICES:
                serviceRegistry.listen(console, printer);
                printHelp(printer);
                return true;
        }

        return ConsoleListener.super.processCommand(console, code, printer);
    }

    @Override
    public void printMenu(Consumer<String> printer) {
        printCommandInfo(ConsoleCommand.HELP, printer);
        printCommandInfo(ConsoleCommand.SERVICES, printer);
        printCommandInfo(ConsoleCommand.SET_PROPERTY, printer);
        printCommandInfo(ConsoleCommand.EXIT, printer);
    }
}
