package shapranv.shell.utils.concurrent;

public interface ShellThreadLocal<T> {
    T get();

    void set(T value);

    void remove();
}
