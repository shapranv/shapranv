package shapranv.shell.utils.collections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class GarbageFreeConcurrentMap<K, V> extends ConcurrentHashMap<K, V> {
    @Override
    public V putIfAbsent(K key, V value) {
        V currentValue = get(key);

        if (currentValue != null) {
            return currentValue;
        } else {
            return super.putIfAbsent(key, value);
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        V currentValue = get(key);

        if (currentValue != null) {
            return currentValue;
        } else {
            return super.computeIfAbsent(key, mappingFunction);
        }
    }
}
