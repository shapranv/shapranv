package shapranv.shell.utils.collections;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public final class CollectionUtils {

    public static <K, V> ConcurrentMap<K, V> concurrentMap() {
        return new GarbageFreeConcurrentMap<>();
    }

    public static <V> Set<V> setFromConcurrentMap() {
        return Collections.newSetFromMap(concurrentMap());
    }
}