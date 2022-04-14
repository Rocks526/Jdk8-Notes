package java.util;

import java.util.function.UnaryOperator;


/**
 * List通用接口，代表有序、可重复集合
 */
public interface List<E> extends Collection<E> {

    // ================= 查询操作 ===========================================

    // 返回集合元素个数
    int size();

    // 判断是否为空
    boolean isEmpty();

    // 判断集合是否包含元素
    boolean contains(Object o);

    // 返回一个迭代器
    Iterator<E> iterator();

    // 转换数组
    Object[] toArray();

    // 转换泛型数组
    <T> T[] toArray(T[] a);


    // =========================== 变更操作 =====================================

    // 新增元素，添加到末尾!!!
    boolean add(E e);

    // 列表删除第一个匹配项
    boolean remove(Object o);


    // =========================== 批量操作 =====================================

    // 是否包含参数集合的所有元素
    boolean containsAll(Collection<?> c);

    // 批量新增，追加到末尾!!!
    boolean addAll(Collection<? extends E> c);

    // 从指定索引开始批量插入
    boolean addAll(int index, Collection<? extends E> c);

    // 批量删除
    boolean removeAll(Collection<?> c);

    // 保留集合交集部分
    boolean retainAll(Collection<?> c);

    // 将每个元素根据传入的函数式方法，替换为目标元素，通过迭代器遍历实现
    default void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final ListIterator<E> li = this.listIterator();
        while (li.hasNext()) {
            li.set(operator.apply(li.next()));
        }
    }


    /**
     * 集合排序，传入比较器参数
     * 默认采用转数组、排序数组、迭代器遍历替换元素的方式实现
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    default void sort(Comparator<? super E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);
        ListIterator<E> i = this.listIterator();
        for (Object e : a) {
            i.next();
            i.set((E) e);
        }
    }

    // 清空集合
    void clear();


    // =========================== 哈希操作 =====================================

    // 判断集合是否相等，默认比较引用
    boolean equals(Object o);

    // 生成集合的hashcode方法
    int hashCode();


    // =============== 根据下标访问操作 ================================

    // 获取指定下标的元素
    E get(int index);

    // 设置指定下标的元素值
    E set(int index, E element);

    // 给指定下标插入元素，会导致后续元素全部搬移
    void add(int index, E element);

    // 移除指定下标的元素，会导致元素搬移
    E remove(int index);


    // ===================== 查找操作 ========================================

    // 返回该元素第一次出现的索引下标
    int indexOf(Object o);

    // 返回该元素在集合中的最后的下标
    int lastIndexOf(Object o);


    // ============================ List迭代器 =====================================


    // 定制的遍历List的迭代器，支持双向遍历
    ListIterator<E> listIterator();

    // 从指定的下标开始返回一个迭代器
    ListIterator<E> listIterator(int index);

    // ================= 视图操作 ======================================

    // 返回集合指定下标之间的元素作为一个只读视图，注意只读！！！
    List<E> subList(int fromIndex, int toIndex);

    // 创建一个切分器
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED);
    }
}
