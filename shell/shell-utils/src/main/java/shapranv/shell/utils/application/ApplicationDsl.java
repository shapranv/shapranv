package shapranv.shell.utils.application;

import shapranv.shell.utils.application.module.ModuleDefinition;

import java.util.Arrays;
import java.util.List;

import static shapranv.shell.utils.application.Application.*;
import static shapranv.shell.utils.application.config.ConfigProps.*;

public class ApplicationDsl {

    public static void run(String[] args, ApplicationConfiguration configuration) {
        System.setProperty(ROOT_MODULE_DEFINITION, configuration.rootModule);
        System.setProperty(LOG4J2_LOG_FILE, configuration.logfile);
        System.setProperty(LOG4J2_LOG_PATH, configuration.logPath);

        List<String> params = Arrays.asList(
                Application.ENV_PARAM, configuration.envPath
        );
        params.addAll(Arrays.asList(args));

        Application.main(params.toArray(new String[0]));
    }

    public static class ApplicationConfiguration {
        private String rootPath;
        private String rootModule;
        private String envPath;
        private String logfile;
        private String logPath = "logs/";

        public ApplicationConfiguration rootPath(String rootPath) {
            this.rootPath = rootPath;
            return this;
        }

        public ApplicationConfiguration rootModule(Class<? extends ModuleDefinition> rootModule) {
            this.rootModule = rootModule.getName();

            if (this.logfile == null) {
                this.logfile = rootModule.getSimpleName();
            }

            return this;
        }

        public ApplicationConfiguration env(String env) {
            this.envPath = rootPath + env + PROPERTIES_SUFFIX;
            return this;
        }

        public ApplicationConfiguration logfile(String logfile) {
            this.logfile = logfile;
            return this;
        }

        public ApplicationConfiguration logPath(String logPath) {
            this.logPath = logPath;
            return this;
        }
    }
}
