package java.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.io.Serializable;

/**
 * 哈希类型顶级接口，支持KV存储，用来替代以前的Dictionary
 */
public interface Map<K,V> {

    // ======================== 查询操作 ================================

    // 返回KV个数
    int size();

    // 是否为空
    boolean isEmpty();

    // key里是否包含某个元素
    boolean containsKey(Object key);

    // value里是否包含某个元素
    boolean containsValue(Object value);

    // 根据K获取V
    V get(Object key);

    // ========================= 变更操作 =====================================

    // 新增KV对
    V put(K key, V value);


    // 移除KV对
    V remove(Object key);


    // ========================= 批量操作 =====================================

    // 批量新增
    void putAll(Map<? extends K, ? extends V> m);

    // 清空集合
    void clear();


    // ========================= 视图操作 =====================================

    // 返回包含所有K的集合视图
    Set<K> keySet();

    // 返回包含所有V的集合视图 ==> K不可重复，所以是Set，V可以重复，所以是Collection
    Collection<V> values();


    // 返回所有KV对的集合视图
    Set<Map.Entry<K, V>> entrySet();

    // KV对
    interface Entry<K,V> {

        // 获取K
        K getKey();

        // 获取V
        V getValue();

        // 修改V
        V setValue(V value);

        // 判断KV对是否相等
        boolean equals(Object o);

        // 哈希值
        int hashCode();

        // 获取一个Key比较器
        public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K,V>> comparingByKey() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        // 获取一个Value比较器
        public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K,V>> comparingByValue() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }

        // 返回一个比较器，用给定的比较器比较Key
        public static <K, V> Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Map.Entry<K, V>> & Serializable)
                (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
        }

        // 返回一个比较器，用给定的比较器比较Value
        public static <K, V> Comparator<Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Map.Entry<K, V>> & Serializable)
                (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
        }
    }

    // ======================= 比较和哈希 =============================================

    // 是否相等
    boolean equals(Object o);

    // 哈希值
    int hashCode();

    // ========================= 支持默认值的操作 ==============================

    // 获取value，不存在则返回defaultValue
    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key))
            ? v
            : defaultValue;
    }

    // 增强for循环，使用函数表达式处理map元素
    default void forEach(BiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        for (Map.Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch(IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }


    // 批量修改value，根据函数表达式处理
    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        for (Map.Entry<K, V> entry : entrySet()) {
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch(IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }

            // ise thrown from function is not a cme.
            v = function.apply(k, v);

            try {
                entry.setValue(v);
            } catch(IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new ConcurrentModificationException(ise);
            }
        }
    }

    // key不存在时设置value，并返回当前值
    default V putIfAbsent(K key, V value) {
        V v = get(key);
        if (v == null) {
            v = put(key, value);
        }

        return v;
    }

    // 包含key，并且value等于期望值时，再移除
    default boolean remove(Object key, Object value) {
        Object curValue = get(key);
        // 不相等或者key不存在
        if (!Objects.equals(curValue, value) ||
            (curValue == null && !containsKey(key))) {
            return false;
        }
        remove(key);
        return true;
    }

    // 包含key，并且value等于期望值时，再替换
    default boolean replace(K key, V oldValue, V newValue) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, oldValue) ||
            (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
        return true;
    }

    // key存在时再替换
    default V replace(K key, V value) {
        V curValue;
        if (((curValue = get(key)) != null) || containsKey(key)) {
            curValue = put(key, value);
        }
        return curValue;
    }

    /**
     * 复合操作，当key不存在时，对key进行函数式计算，获得value，再put
     *      * <pre> {@code
     *      * if (map.get(key) == null) {
     *      *     V newValue = mappingFunction.apply(key);
     *      *     if (newValue != null)
     *      *         map.put(key, newValue);
     *      * }
     *      * }</pre>
     */
    default V computeIfAbsent(K key,
            Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v;
        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }

    /**
     * 复合操作，当key存在时，根据key和旧的value进行函数式计算，生成新的value
     *  如果新的value为null，则执行remove操作，反之执行put操作
     *
     *      * if (map.get(key) != null) {
     *      *     V oldValue = map.get(key);
     *      *     V newValue = remappingFunction.apply(key, oldValue);
     *      *     if (newValue != null)
     *      *         map.put(key, newValue);
     *      *     else
     *      *         map.remove(key);
     *      * }
     *      * }</pre>
     */
    default V computeIfPresent(K key,
            BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue;
        if ((oldValue = get(key)) != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                put(key, newValue);
                return newValue;
            } else {
                remove(key);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 复合操作，基于key和旧的value计算新的value，
     * 1. 如果新值不为null则put
     * 2. 如果新值为null，则删除旧值，如果旧值不存在，则不处理
     */
    default V compute(K key,
            BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = get(key);

        V newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            // 新值为null，删除旧值
            if (oldValue != null || containsKey(key)) {
                remove(key);
                return null;
            } else {
                // 旧值不存在
                return null;
            }
        } else {
            // 替换旧值
            put(key, newValue);
            return newValue;
        }
    }


    /**
     * 复合操作，如果旧值不存在，则使用value为新值，反之根据旧值和value计算新值
     * 如果新值为null，则remove，反之put
     */
    default V merge(K key, V value,
            BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        V oldValue = get(key);
        V newValue = (oldValue == null) ? value :
                   remappingFunction.apply(oldValue, value);
        if(newValue == null) {
            remove(key);
        } else {
            put(key, newValue);
        }
        return newValue;
    }
}
