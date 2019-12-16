package shapranv.shell.utils.application.module;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModuleDefinitionFactory {

    public static ModuleDefinition of(String name, Class<? extends Module> moduleClass, ModuleDefinition... dependencies) {
        ArrayUtils.reverse(dependencies);

        return new ModuleDefinition() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Class<? extends Module> getModuleClass() {
                return moduleClass;
            }

            @Override
            public List<ModuleDefinition> getDependencies() {
                return dependencies != null ? Arrays.asList(dependencies) : Collections.emptyList();
            }
        };
    }
}
