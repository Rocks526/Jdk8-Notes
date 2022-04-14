package java.util;

/**
 * 不包含重复元素、无序的集合
 */
public interface Set<E> extends Collection<E> {

    // ===================== 查询操作 ==========================================

    // 返回集合大小
    int size();

    // 是否为空
    boolean isEmpty();

    // 是否包含某个元素
    boolean contains(Object o);

    // 获取迭代器
    Iterator<E> iterator();

    // 转换数组
    Object[] toArray();

    // 转换泛型数组
    <T> T[] toArray(T[] a);


    // ========================= 变更操作 =====================================

    // 集合增加一个元素
    boolean add(E e);

    // 移除一个元素
    boolean remove(Object o);


    // ========================= 批量操作 =====================================

    // 是否包含参数集合的所有元素
    boolean containsAll(Collection<?> c);

    // 批量添加
    boolean addAll(Collection<? extends E> c);

    // 与参数集合取交集
    boolean retainAll(Collection<?> c);

    // 移除参数集合的所有元素
    boolean removeAll(Collection<?> c);

    // 清空集合
    void clear();


    // ========================= 比较与哈希操作 =====================================

    // 判断是否相等
    boolean equals(Object o);

    // 获取哈希值
    int hashCode();

    // 获取切分器
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT);
    }
}
