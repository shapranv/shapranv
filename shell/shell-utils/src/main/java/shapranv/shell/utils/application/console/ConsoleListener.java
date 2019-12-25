package shapranv.shell.utils.application.console;

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
                if(!processCommand(command, printer)) {
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

    default boolean processCommand(String command, Consumer<String> printer) throws Exception {
        return false;
    }
}
