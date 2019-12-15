package shapranv.shell.utils.application;

import lombok.Getter;
import shapranv.shell.utils.SystemUtils;

public class ApplicationSettings {
    public static final String ROOT_MODULE_DEFINITION_PROPERTY = "root.module.definition";

    @Getter
    private static final String rootModuleDefinition = SystemUtils.getProperty(ROOT_MODULE_DEFINITION_PROPERTY);

}
