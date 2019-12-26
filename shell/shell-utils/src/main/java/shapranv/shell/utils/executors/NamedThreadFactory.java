package shapranv.shell.utils.executors;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {
    private final Logger logger;
    private final ThreadFactory threadFactory;
    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public NamedThreadFactory(String name) {
        this(name, true);
    }

    public NamedThreadFactory(String name, boolean withUncaughtExceptionHandler) {
        this.threadFactory = new DefaultThreadFactory(name, true, Thread.NORM_PRIORITY, null);
        this.logger = LogManager.getLogger(getClass().getSimpleName() + "." + name);

        if (withUncaughtExceptionHandler) {
            uncaughtExceptionHandler = (thread, exception) ->
                    logger.error("Uncaught exception on [{}]", thread.getName(), exception);
        } else {
            uncaughtExceptionHandler = null;
        }
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = threadFactory.newThread(runnable);
        if (uncaughtExceptionHandler != null) {
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        logger.info("[{}] thread created", thread.getName());

        return thread;
    }
}
