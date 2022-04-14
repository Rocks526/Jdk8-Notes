package java.util;


/**
 * 集合的基础抽象类，可以直接基于此类进行扩展
 */
public abstract class AbstractCollection<E> implements Collection<E> {

    protected AbstractCollection() {
        // 唯一构造器，只能由子类调用
    }

    // ======================= 查询操作 ===================================

    /**
     * 返回一个迭代器，用于遍历集合
     */
    public abstract Iterator<E> iterator();

    /**
     * 返回集合元素个数
     */
    public abstract int size();

    /**
     * 判断集合是否为空，默认实现为判断size是否为0
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 判断是否包含某个元素，默认实现为通过迭代器遍历，挨个对比
     */
    public boolean contains(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    return true;
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return true;
        }
        return false;
    }

    /**
     * 返回一个数组，默认实现通过迭代器遍历实现，返回数组顺序和迭代器遍历一致
     */
    public Object[] toArray() {
        // 创建size大小数据
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++) {
            // 迭代器元素比size小，直接截断，以size为准
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        // 如果迭代器元素个数大于size，则通过finishToArray方法扩容数组，容纳迭代器后续元素
        return it.hasNext() ? finishToArray(r, it) : r;
    }


    /**
     * 将集合返回为数组个数，泛型通过参数传入
     * 实现逻辑与toArray类似，返回元素个数以迭代器为准
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = size();
        T[] r = a.length >= size ? a :
                  (T[])java.lang.reflect.Array
                  .newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it = iterator();

        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) { // fewer elements than expected
                if (a == r) {
                    r[i] = null; // null-terminate
                } else if (a.length < i) {
                    return Arrays.copyOf(r, i);
                } else {
                    System.arraycopy(r, 0, a, 0, i);
                    if (a.length > i) {
                        a[i] = null;
                    }
                }
                return a;
            }
            r[i] = (T)it.next();
        }
        // more elements than expected
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    // 数组最大长度，超过此长度，Jvm可能会有问题
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    /**
     * 迭代器元素大于size，对数组进行扩容，将迭代器后续元素融入
     */
    @SuppressWarnings("unchecked")
    private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
        int i = r.length;
        while (it.hasNext()) {
            int cap = r.length;
            if (i == cap) {
                int newCap = cap + (cap >> 1) + 1;
                // overflow-conscious code
                if (newCap - MAX_ARRAY_SIZE > 0)
                    newCap = hugeCapacity(cap + 1);
                r = Arrays.copyOf(r, newCap);
            }
            r[i++] = (T)it.next();
        }
        // trim if overallocated
        return (i == r.length) ? r : Arrays.copyOf(r, i);
    }

    // 数组容量扩充
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError
                ("Required array size too large");
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    // ===================== 变更操作 ==========================================

    /**
     * 新增一个元素，默认实现不支持此操作
     */
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * 移除一个元素，默认实现是通过迭代器遍历查找并移除
     */
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext()) {
                if (it.next()==null) {
                    it.remove();
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }


    // ======================== 批量操作 =====================================


    /**
     * 判断是否包含目标集合的所有元素，默认for循环调用contain
     */
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    /**
     * 批量新增元素，默认for调用add
     */
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }


    /**
     * 批量移除元素，默认通过迭代器遍历，contain对比实现
     */
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }


    /**
     * 保留集合与参数集合的交集部分，默认迭代器遍历 + contain方法实现
     */
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }


    /**
     * 清空集合，默认迭代器遍历删除
     */
    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }


    //  ============== 字符串转换 ==========================================

    /**
     * 集合输出，默认为： [v1, v2, v3 .....]
     */
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

}
