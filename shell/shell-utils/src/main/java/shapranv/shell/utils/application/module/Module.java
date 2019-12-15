package shapranv.shell.utils.application.module;

import shapranv.shell.utils.application.Environment;

public interface Module {

    void start(Environment env);

    void ready();

    void stop();

}
