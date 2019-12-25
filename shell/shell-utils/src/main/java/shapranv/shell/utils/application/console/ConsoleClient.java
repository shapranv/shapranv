package shapranv.shell.utils.application.console;

import shapranv.shell.utils.application.Environment;

import java.io.BufferedReader;
import java.util.function.Consumer;

public class ConsoleClient implements ConsoleListener {
    private final String appName;
    private final BufferedReader console;
    private final ServiceRegistry serviceRegistry;

    public ConsoleClient(String appName, BufferedReader console) {
        this.appName = appName;
        this.console = console;

        Environment env = Environment.getInstance();
        this.serviceRegistry = env.getService(ServiceRegistry.class);
    }

    @Override
    public String getName() {
        return appName;
    }

    @Override
    public boolean processCommand(String code, Consumer<String> printer) throws Exception {
        ConsoleCommand command = ConsoleCommand.findByCode(code);

        switch (command) {
            case EXIT:
                return false;
            case SERVICES:
                serviceRegistry.listen(console, printer);
                printHelp(printer);
                break;
            case HELP:
            case UNDEF:
                printHelp(printer);
                break;
        }

        return true;
    }

    @Override
    public void printMenu(Consumer<String> printer) {
        for (ConsoleCommand command : ConsoleCommand.values()) {
            if (command != ConsoleCommand.UNDEF) {
                printer.accept(command.getCode() + " " + command.getDescription());
            }
        }
    }
}
