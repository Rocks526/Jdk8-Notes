package java.util;

/**
 * 支持排序的集合，通过Comparator排序
 */
public interface SortedSet<E> extends Set<E> {

    // 返回集合用于排序的比较器
    Comparator<? super E> comparator();

    // 取出集合中范围元素的集合，要求实现为视图模式
    SortedSet<E> subSet(E fromElement, E toElement);

    // 取出集合中范围元素的集合，要求实现为视图模式 （小于或等于该元素）
    SortedSet<E> headSet(E toElement);

    // 取出集合中范围元素的集合，要求实现为视图模式 （大于或等于该元素）
    SortedSet<E> tailSet(E fromElement);

    // 返回最小的元素
    E first();

    // 返回最大的元素
    E last();

    // 切分器
    @Override
    default Spliterator<E> spliterator() {
        return new Spliterators.IteratorSpliterator<E>(
                this, Spliterator.DISTINCT | Spliterator.SORTED | Spliterator.ORDERED) {
            @Override
            public Comparator<? super E> getComparator() {
                return SortedSet.this.comparator();
            }
        };
    }
}
