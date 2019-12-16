package shapranv.shell.utils.application;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Environment {
    private final Map<Class<?>, Object> services = new HashMap<>();

    private static final AtomicReference<Environment> instance = new AtomicReference<>(new Environment());

    private Environment() {

    }

    static Environment getInstance() {
        return instance.get();
    }

    public <T> void addService(T service, Class<T> type) {
        services.put(type, service);
    }

    @SuppressWarnings("unchecked")
    public <T> T ensureService(Class<T> type) {
        if (services.containsKey(type)) {
            return (T) services.get(type);
        } else {
            throw new RuntimeException("Service is not defined [" + type.getSimpleName() + "]");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> type) {
        return (T) services.get(type);
    }
}
