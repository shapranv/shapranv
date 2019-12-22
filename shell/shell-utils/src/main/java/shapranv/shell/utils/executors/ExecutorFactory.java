package shapranv.shell.utils.executors;

import io.netty.util.concurrent.DefaultEventExecutor;

import java.util.concurrent.ScheduledExecutorService;

public class ExecutorFactory {
    public static final ScheduledExecutorService defaultScheduledExecutorService =
            newScheduledExecutorService("DefaultScheduledExecutorService");

    public static ScheduledExecutorService newScheduledExecutorService(String name) {
        return new DefaultEventExecutor(new NamedThreadFactory(name));
    }
}
