package shapranv.shell.utils.concurrent;

public class CachedStringBuilder {

    private final ShellThreadLocal<StringBuilder> builder;

    private CachedStringBuilder(int capacity) {
        this.builder = ThreadLocalFactory.create(() -> new StringBuilder(capacity));
    }

    public static CachedStringBuilder of(int capacity) {
        return new CachedStringBuilder(capacity);
    }

    public StringBuilder getBuilder() {
        StringBuilder b = builder.get();
        b.setLength(0);

        return b;
    }
}
