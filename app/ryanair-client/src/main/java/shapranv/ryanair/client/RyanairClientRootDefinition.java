package shapranv.ryanair.client;

import shapranv.ryanair.client.module.RyanairClientModule;
import shapranv.shell.utils.application.module.ModuleDefinition;

import java.util.Collections;
import java.util.List;

import static shapranv.shell.utils.application.module.ModuleDefinitionFactory.of;

public class RyanairClientRootDefinition implements ModuleDefinition {
    @Override
    public String getName() {
        return "Ryanair client";
    }

    @Override
    public List<ModuleDefinition> getDependencies() {
        return Collections.singletonList(
                of("RyanairClient", RyanairClientModule.class)
        );
    }
}
