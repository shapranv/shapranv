package shapranv.shell.utils.application.console;

import org.apache.logging.log4j.Logger;
import shapranv.shell.utils.application.Environment;
import shapranv.shell.utils.application.console.command.ConsoleCommand;
import shapranv.shell.utils.application.console.command.SystemCommands;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

public class ConsoleClient extends ConsoleListener {
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
    protected List<ConsoleCommand> createCommands() {
        return Arrays.asList(
                SystemCommands.HELP,
                new ConsoleCommand("-s", "Service registry") {
                    @Override
                    public boolean execute(ConsoleListener listener, BufferedReader console, Logger logger) throws Exception {
                        serviceRegistry.listen(console, logger);
                        printHelp(logger);
                        return true;
                    }
                },
                SystemCommands.SET_PROPERTY,
                SystemCommands.EXIT
        );
    }
}
