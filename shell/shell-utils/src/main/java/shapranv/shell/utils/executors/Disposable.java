package shapranv.shell.utils.executors;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

@SuppressWarnings("deprecation")
public interface Disposable extends Closeable {

    @Override
    void close();

    default void close(long timeout, TimeUnit timeUnit) {
        long start = currentTimeMillis();
        long timeoutMillis = timeUnit.toMillis(timeout);
        Thread thread = new Thread(this::close);
        thread.start();

        try {
            thread.join(timeoutMillis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (thread.isAlive()) {
            thread.interrupt();
        }

        while (thread.isAlive()) {
            if ((currentTimeMillis() - start) > timeoutMillis) {
                if (this instanceof Thread) {
                    Thread currentThread = (Thread) this;
                    currentThread.stop();
                    throw new RuntimeException("Can't stop thread [" + currentThread.getName() + "]");
                } else {
                    throw new RuntimeException("Can't stop disposable [" + this.getClass().getSimpleName() + "]");
                }
            }
        }
    }
}
