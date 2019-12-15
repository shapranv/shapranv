package shapranv.shell.utils.application;

import lombok.extern.log4j.Log4j2;
import shapranv.shell.utils.application.module.Module;
import shapranv.shell.utils.application.module.ModuleDefinition;
import shapranv.shell.utils.reflection.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Application {
    private static final List<Module> activeModules = new ArrayList<>();

    public static void main(String[] args) {
        try {
            startApplication();
            activeModules.forEach(Module::ready);
        } catch (Exception e) {
            log.error("Cannot start application", e);
            stopApplication();
            return;
        }

        System.gc();

        System.exit(0);
    }

    private static void startApplication() {
        Environment env = Environment.getInstance();
        String rootModuleName = ApplicationSettings.getRootModuleDefinition();
        ModuleDefinition rootModule = (ModuleDefinition) ReflectionUtils.createInstance(rootModuleName);

        for (ModuleDefinition moduleDefinition : rootModule.getDependencies()) {
            Module module = moduleDefinition.moduleInstance();
            module.start(env);
            activeModules.add(module);
        }

        log.info("Application started: {}", rootModule.getName());
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
}
