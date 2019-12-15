package shapranv.shell.utils.application.module;

import java.util.List;

public interface ModuleDefinition {

    String getName();

    default Class<? extends Module> getModuleClass() {
        return null;
    }

    List<ModuleDefinition> getDependencies();

    default Module moduleInstance() {
        try {
            return getModuleClass().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot instantiate module for " + getClass().getSimpleName(), e);
        }
    }
}
