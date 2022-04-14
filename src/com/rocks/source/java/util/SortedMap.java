package java.util;

// 支持排序的map
public interface SortedMap<K,V> extends Map<K,V> {

    // 返回用于key排序的比较器
    Comparator<? super K> comparator();

    // map切分，获取范围kv对 ==> k在fromKey和toKey之间
    SortedMap<K,V> subMap(K fromKey, K toKey);

    // map切分，获取范围kv对 ==> k小于等于toKey
    SortedMap<K,V> headMap(K toKey);

    // map切分，获取范围kv对 ==> k大于等于fromKey
    SortedMap<K,V> tailMap(K fromKey);

    // 最小的key
    K firstKey();

    // 最大的key
    K lastKey();

    // 所有的key集合
    Set<K> keySet();

    // 所有的value集合
    Collection<V> values();

    // 所有kv对集合
    Set<Map.Entry<K, V>> entrySet();
}
