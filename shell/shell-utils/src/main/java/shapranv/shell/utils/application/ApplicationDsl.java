package shapranv.shell.utils.application;

import shapranv.shell.utils.application.module.ModuleDefinition;

public class ApplicationDsl {

    public static void run(String[] args, ApplicationConfiguration configuration) {
        System.setProperty(ApplicationSettings.ROOT_MODULE_DEFINITION_PROPERTY, configuration.rootModule);

        Application.main(args);
    }

    public static class ApplicationConfiguration {
        private String rootModule;

        public ApplicationConfiguration rootModule(Class<? extends ModuleDefinition> rootModule) {
            this.rootModule = rootModule.getName();
            return this;
        }
    }
}
