package shapranv.shell.utils.application.console;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    public boolean processCommand(String code, Consumer<String> printer) throws Exception {
        ConsoleCommand command = ConsoleCommand.findByCode(code);

        switch (command) {
            case EXIT:
                return false;
            case HELP:
                printHelp(printer);
                break;
            default:
                try {
                    int index = Integer.parseInt(code) - 1;
                    if (index >= 0 && index < services.size()) {
                        Service service = services.get(index);
                        service.printStatus(System.out::println);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    //skip
                }
                printer.accept("Unknown service...");
        }

        return true;
    }

    @Override
    public String getName() {
        return "Service registry";
    }

    @Override
    public void printMenu(Consumer<String> printer) {
        for (int i = 0; i < services.size(); i++) {
            printer.accept(String.valueOf(i + 1) + " " + services.get(i).getName());
        }
        printer.accept(ConsoleCommand.EXIT.getCode() + " Quit registry");
    }
}
