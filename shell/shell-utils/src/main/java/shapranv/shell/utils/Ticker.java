package shapranv.shell.utils;

import lombok.RequiredArgsConstructor;
import shapranv.shell.utils.executors.ExecutorFactory;

import java.io.Closeable;
import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class Ticker implements Closeable {
    private ScheduledFuture<?> scheduledFuture;

    private final Duration interval;
    private final Runnable action;
    private final Runnable onClose;

    public Ticker(Duration interval, Runnable action) {
        this(interval, action, ()-> {});
    }

    public void start() {
        this.scheduledFuture = ExecutorFactory.defaultScheduledExecutorService
                .scheduleAtFixedRate(action, interval.toMillis(), interval.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() {
        if (scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
            this.scheduledFuture = null;
            onClose.run();
        }
    }
}
