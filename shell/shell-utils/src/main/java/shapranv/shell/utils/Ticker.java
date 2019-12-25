package shapranv.shell.utils;

import lombok.RequiredArgsConstructor;
import shapranv.shell.utils.executors.ExecutorFactory;

import java.io.Closeable;
import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class Ticker implements Closeable {
    private ScheduledFuture<?> scheduledFuture;

    private final Supplier<Duration> interval;
    private final Runnable action;
    private final Runnable onClose;

    public Ticker(Duration interval, Runnable action) {
        this(() -> interval, action);
    }

    public Ticker(Supplier<Duration> interval, Runnable action) {
        this(interval, action, ()-> {});
    }

    public void start() {
        long period = interval.get().toMillis();
        this.scheduledFuture = ExecutorFactory.defaultScheduledExecutorService
                .scheduleAtFixedRate(action, period, period, TimeUnit.MILLISECONDS);
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
