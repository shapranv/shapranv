package shapranv.ryanair.client.module;

import lombok.extern.log4j.Log4j2;
import shapranv.shell.utils.application.Environment;
import shapranv.shell.utils.application.module.Module;

@Log4j2
public class RyanairClientModule implements Module {
    @Override
    public void start(Environment env) {
        log.info("Ryanair client module created");
    }

    @Override
    public void ready() {
        log.info("Ryanair client module is ready");
    }

    @Override
    public void stop() {
        log.info("Ryanair client module stopped");
    }
}
