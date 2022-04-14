package java.lang;

/**
 * 排序接口
 * @see java.util.Comparator
 * @since 1.2
 */
public interface Comparable<T> {
    /**
     * - 实现此接口，重写compareTo方法可以进行大小比较
     * - 返回负数：小于比较对象
     * - 返回0：等于比较对象
     * - 返回正数：大于比较对象
     */
    public int compareTo(T o);
}
