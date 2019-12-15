package shapranv.shell.utils.collections;

import java.util.concurrent.ConcurrentMap;

public final class CollectionUtils {

    public static <K, V> ConcurrentMap<K, V> concurrentMap() {
        return new GarbageFreeConcurrentMap<>();
    }
}