package shapranv.shell.utils.application.console;

import shapranv.shell.utils.application.config.ConfigService;

import java.io.BufferedReader;
import java.util.function.Consumer;

import static shapranv.shell.utils.application.console.ConsoleUtils.FOOTER_LINE;
import static shapranv.shell.utils.application.console.ConsoleUtils.getHeader;

public interface ConsoleListener {
    String getName();

    default void listen(BufferedReader console, Consumer<String> printer) {
        printHelp(printer);

        while (true) {
            try {
                String command = console.readLine();
                if(!processCommand(console, command, printer)) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    default void printHelp(Consumer<String> printer) {
        printer.accept(getHeader(getName()));
        printMenu(printer);
        printer.accept(FOOTER_LINE);
    }

    default void printMenu(Consumer<String> printer) {
        //do nothing
    }

    default void printStatus(Consumer<String> printer) {
        //do nothing
    }

    default boolean processCommand(BufferedReader console, String command, Consumer<String> printer) throws Exception {
        switch (ConsoleCommand.findByCode(command)) {
            case EXIT:
                return false;
            case SET_PROPERTY:
                printer.accept("Property:");
                String property = console.readLine();
                printer.accept("Value:");
                String value = console.readLine();
                ConfigService.getInstance().setProperty(property, value);
                printer.accept("System property updated: " + property + "=" + value);
                return true;
            case UNDEF:
            case HELP:
                printHelp(printer);
                break;
        }

        return true;
    }
}
