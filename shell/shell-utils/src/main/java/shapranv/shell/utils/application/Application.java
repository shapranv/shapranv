package shapranv.shell.utils.application;

import lombok.extern.log4j.Log4j2;
import shapranv.shell.utils.application.config.ConfigService;
import shapranv.shell.utils.application.config.ConfigUtils;
import shapranv.shell.utils.application.console.ConsoleClient;
import shapranv.shell.utils.application.console.ConsoleLogger;
import shapranv.shell.utils.application.console.ServiceRegistry;
import shapranv.shell.utils.application.module.Module;
import shapranv.shell.utils.application.module.ModuleDefinition;
import shapranv.shell.utils.reflection.ReflectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static shapranv.shell.utils.application.config.ConfigProps.*;

@Log4j2
public class Application {
    static final String ENV_PARAM = "-env";

    static final String APPLICATION_CONFIG_ROOT = "application";
    static final String APPLICATION_CONFIG_PATH = "/config/";

    static final String COMMON_PROPERTIES = APPLICATION_CONFIG_ROOT + APPLICATION_CONFIG_PATH + "common.properties";
    static final String PROPERTIES_SUFFIX = ".properties";

    private static final List<Module> activeModules = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ModuleDefinition rootModule = startApplication(args);
            activeModules.forEach(Module::ready);
            startConsoleClient(rootModule.getName());
            System.gc();
        } catch (Exception e) {
            log.error("Cannot start application", e);
            stopApplication();
            return;
        }

        stopApplication();
        System.exit(0);
    }

    private static ModuleDefinition startApplication(String[] args) {
        Environment env = Environment.getInstance();
        ConfigService configService = ConfigService.getInstance();
        env.addService(new ServiceRegistry(), ServiceRegistry.class);

        Map<String, String> appParams = retrieveAppParams(args);

        loadConfiguration(configService, appParams);

        String rootModuleName = configService.get(ROOT_MODULE_DEFINITION);
        ModuleDefinition rootModule = (ModuleDefinition) ReflectionUtils.createInstance(rootModuleName);

        for (ModuleDefinition moduleDefinition : rootModule.getDependencies()) {
            Module module = moduleDefinition.moduleInstance();
            module.start(env);
            activeModules.add(module);
        }

        log.info("{} started", rootModule.getName());
        return rootModule;
    }

    private static void loadConfiguration(ConfigService configService, Map<String, String> appParams) {
        String envPath = appParams.get(ENV_PARAM);
        log.info("Loading environment settings from: {}", envPath);
        ConfigUtils.loadConfig(envPath, System::setProperty);
        log.info("Loading common settings from: {}", COMMON_PROPERTIES);
        ConfigUtils.loadConfig(COMMON_PROPERTIES, configService::setProperty);
    }

    private static void startConsoleClient(String appName) {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        ConsoleClient consoleClient = new ConsoleClient(appName);
        consoleClient.listen(console, ConsoleLogger.getLogger());
    }

    private static void stopApplication() {
        activeModules.forEach(Application::stopModule);
    }

    private static void stopModule(Module module) {
        try {
            module.stop();
        } catch (Exception e) {
            log.error("Cannot stop module {}", module.getClass().getSimpleName());
        }
    }

    private static Map<String, String> retrieveAppParams(String[] args) {
        Map<String, String> appParams = new HashMap<>();
        appParams.put(ENV_PARAM, null);

        for (int i = 0; i < args.length; i++) {
            if (appParams.containsKey(args[i]) && (i + 1) < args.length) {
                appParams.put(args[i], args[i + 1]);
            }
        }

        return appParams;
    }
}
