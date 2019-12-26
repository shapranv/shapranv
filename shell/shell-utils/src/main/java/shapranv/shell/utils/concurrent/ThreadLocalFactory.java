package shapranv.shell.utils.concurrent;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.function.Supplier;

public class ThreadLocalFactory {

    public static <V> ShellThreadLocal<V> create() {
        return create(() -> null);
    }

    public static <V> ShellThreadLocal<V> create(Supplier<? extends V> supplier) {
        ThreadLocal<V> threadLocal = ThreadLocal.withInitial(supplier);

        FastThreadLocal<V> fastThreadLocal = new FastThreadLocal<V>() {
            @Override
            protected V initialValue() throws Exception {
                return supplier.get();
            }
        };

        return new ShellThreadLocal<V>() {
            @Override
            public V get() {
                return Thread.currentThread() instanceof FastThreadLocalThread
                        ? fastThreadLocal.get()
                        : threadLocal.get();
            }

            @Override
            public void set(V value) {
                if (Thread.currentThread() instanceof FastThreadLocalThread) {
                    fastThreadLocal.set(value);
                } else {
                    threadLocal.set(value);
                }
            }

            @Override
            public void remove() {
                if (Thread.currentThread() instanceof FastThreadLocalThread) {
                    fastThreadLocal.remove();
                } else {
                    threadLocal.remove();
                }
            }
        };
    }
}
