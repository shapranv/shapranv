package shapranv.shell.utils.application.console;

import java.util.function.Consumer;

public interface Service {
    String getName();

    void printStatus(Consumer<String> printer);
}
