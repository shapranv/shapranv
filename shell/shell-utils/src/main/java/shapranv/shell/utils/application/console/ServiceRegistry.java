package shapranv.shell.utils.application.console;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.console.command.ConsoleCommand;
import shapranv.shell.utils.application.console.command.Exit;
import shapranv.shell.utils.service.Service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class ServiceRegistry extends ConsoleListener {
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
    public String getName() {
        return "Service registry";
    }

    @Override
    protected List<ConsoleCommand> createCommands() {
        return Arrays.asList(new Exit("Quit registry"));
    }

    @Override
    public boolean processCommand(BufferedReader console, String code, Logger logger) throws Exception {
        if (StringUtils.isNumeric(code)) {
            int index = Integer.parseInt(code) - 1;
            if (index >= 0 && index < services.size()) {
                Service service = services.get(index);
                if (service instanceof ConsoleListener) {
                    ((ConsoleListener) service).listen(console, logger);
                    printHelp(logger);
                }
            } else {
                logger.info("Unknown service...");
            }
            return true;
        }

        return super.processCommand(console, code, logger);
    }

    @Override
    public void printMenu(Logger logger) {
        for (int i = 0; i < services.size(); i++) {
            logger.info("[{}] {}", i + 1, services.get(i).getName());
        }
        super.printMenu(logger);
    }
}
