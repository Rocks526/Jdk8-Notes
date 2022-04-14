package java.util;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * 集合的通用顶级接口，声明一个集合的基础方法
 */
public interface Collection<E> extends Iterable<E> {

    // =================== 查询操作 ====================================

    /**
     * 返回集合元素个数
     */
    int size();

    /**
     * 集合是否是空的
     */
    boolean isEmpty();

    /**
     * 集合是否包含某个元素
     */
    boolean contains(Object o);

    /**
     * 获取集合的迭代器
     */
    Iterator<E> iterator();

    /**
     * 返回包含此集合所有元素的数组
     */
    Object[] toArray();

    /**
     * 返回包含此集合所有元素的数组，传入返回数组的类型，要求与集合类型一致
     */
    <T> T[] toArray(T[] a);


    // =================== 变更操作 ====================================

    /**
     * 向集合添加一个元素
     * 操作失败抛出UnsupportedOperationException
     * 参数不合法抛出IllegalStateException
     */
    boolean add(E e);

    /**
     * 从集合删除一个元素，如果存在的话
     */
    boolean remove(Object o);


    // =================== 批量操作 ====================================

    /**
     * 判断是否包含参数集合的所有元素
     */
    boolean containsAll(Collection<?> c);

    /**
     * 将指定集合的元素全部添加到此集合中
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * 删除此集合中包含的所有参数集合的元素
     */
    boolean removeAll(Collection<?> c);

    /**
     * 删除此集合中满足给定条件的所有元素，返回是否有元素被删除
     */
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * 仅保留此集合中包含参数集合元素的元素
     * 即保留交集部分
     */
    boolean retainAll(Collection<?> c);

    /**
     * 清空整个集合
     */
    void clear();


    // =================== 比较 和 Hash 操作 ====================================

    /**
     * 判断指定对象与此集合是否相等
     */
    boolean equals(Object o);

    /**
     * 生成HashCode
     */
    int hashCode();

    /**
     * 创建切分器
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    /**
     * 创建一个Stream流
     */
    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * 返回一个并行流
     */
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
