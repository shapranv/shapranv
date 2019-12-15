package shapranv.shell.utils.application.module;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModuleDefinition {

    private final String name;
    private final Class<? extends Module> moduleClass;
}
