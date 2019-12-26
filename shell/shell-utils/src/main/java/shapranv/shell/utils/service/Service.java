package shapranv.shell.utils.service;

import java.util.function.Consumer;

public interface Service {
    String getName();

    void printStatus(Consumer<String> printer);

    void start();
    void stop();
}
