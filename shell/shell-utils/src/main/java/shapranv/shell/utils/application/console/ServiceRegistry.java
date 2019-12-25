package shapranv.shell.utils.application.console;

import lombok.extern.log4j.Log4j2;
import shapranv.shell.utils.service.Service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static shapranv.shell.utils.application.console.ConsoleUtils.printCommandInfo;

@Log4j2
public class ServiceRegistry implements ConsoleListener {
    private final List<Service> services = new ArrayList<>();

    public void register(Service service) {
        if (services.contains(service)) {
            log.warn("{} already registered", service.getName());
        } else {
            log.info("{} registered", service.getName());
            services.add(service);
        }
    }

    @Override
    public boolean processCommand(BufferedReader console, String code, Consumer<String> printer) throws Exception {
        ConsoleCommand command = ConsoleCommand.findByCode(code);

        switch (command) {
            case UNDEF:
                try {
                    int index = Integer.parseInt(code) - 1;
                    if (index >= 0 && index < services.size()) {
                        Service service = services.get(index);
                        if (service instanceof ConsoleListener) {
                            ((ConsoleListener) service).listen(console, printer);
                            printHelp(printer);
                        }
                        return true;
                    }
                } catch (NumberFormatException e) {
                    //skip
                }
                printer.accept("Unknown service...");
                return true;
        }

        return ConsoleListener.super.processCommand(console, code, printer);
    }

    @Override
    public String getName() {
        return "Service registry";
    }

    @Override
    public void printMenu(Consumer<String> printer) {
        for (int i = 0; i < services.size(); i++) {
            printer.accept("[" + String.valueOf(i + 1) + "] " + services.get(i).getName());
        }
        printCommandInfo(ConsoleCommand.EXIT, "Quit registry", printer);
    }
}
