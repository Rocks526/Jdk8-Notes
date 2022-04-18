package java.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 底层基于数组，支持动态扩容，线程安全，有序可重复
 * @param <E>
 */
public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{

    // 存储数据
    protected Object[] elementData;

    // 元素个数
    protected int elementCount;

    // 每次扩容增长的元素个数
    protected int capacityIncrement;

    private static final long serialVersionUID = -2767605614048989439L;


    public Vector(int initialCapacity, int capacityIncrement) {
        super();
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        // 直接创建数组，和ArrayList懒加载不一致
        // 可以看到Vector已经使用较少，没有做很细节的有效
        this.elementData = new Object[initialCapacity];
        this.capacityIncrement = capacityIncrement;
    }

    public Vector(int initialCapacity) {
        this(initialCapacity, 0);
    }

    // 默认初始容量为10
    public Vector() {
        this(10);
    }

    // 将传入集合初始化
    public Vector(Collection<? extends E> c) {
        elementData = c.toArray();
        elementCount = elementData.length;
        // c.toArray might (incorrectly) not return Object[] (see 6260652)
        if (elementData.getClass() != Object[].class)
            // 数组拷贝一次，Object[]内部可能是其他类型
            elementData = Arrays.copyOf(elementData, elementCount, Object[].class);
    }


    // synchronized保证线程安全
    // 将集合元素拷贝到目标数组
    public synchronized void copyInto(Object[] anArray) {
        System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }

    // 缩容
    public synchronized void trimToSize() {
        // 变更记录
        modCount++;
        int oldCapacity = elementData.length;
        if (elementCount < oldCapacity) {
            // 元素个数小于数组长度时进行缩容
            elementData = Arrays.copyOf(elementData, elementCount);
        }
    }

    // 确保容量足够，不足时自动扩容
    public synchronized void ensureCapacity(int minCapacity) {
        if (minCapacity > 0) {
            // 变更
            modCount++;
            // 容量检查
            ensureCapacityHelper(minCapacity);
        }
    }


    private void ensureCapacityHelper(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            // 扩容
            grow(minCapacity);
    }

    // 数组最大长度
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        // 如果构造方法定义了capacityIncrement，每次增长capacityIncrement个元素
        // 反之，每次扩容为之前容量的2倍
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                                         capacityIncrement : oldCapacity);
        if (newCapacity - minCapacity < 0)
            // 扩容后还不足，使用要求的最小值
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            // 数组长度校验
            newCapacity = hugeCapacity(minCapacity);
        // 扩容拷贝
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }


    // 修改集合长度
    public synchronized void setSize(int newSize) {
        modCount++;
        if (newSize > elementCount) {
            // 扩容
            ensureCapacityHelper(newSize);
        } else {
            // 缩容
            for (int i = newSize ; i < elementCount ; i++) {
                elementData[i] = null;
            }
        }
        elementCount = newSize;
    }

    // 返回当前集合容量
    public synchronized int capacity() {
        return elementData.length;
    }

    // 返回当前集合元素个数
    public synchronized int size() {
        return elementCount;
    }

    // 判断集合是否为空
    public synchronized boolean isEmpty() {
        return elementCount == 0;
    }


    // 获取所有元素遍历
    public Enumeration<E> elements() {
        return new Enumeration<E>() {
            int count = 0;

            public boolean hasMoreElements() {
                return count < elementCount;
            }

            public E nextElement() {
                synchronized (Vector.this) {
                    if (count < elementCount) {
                        return elementData(count++);
                    }
                }
                throw new NoSuchElementException("Vector Enumeration");
            }
        };
    }

    // 判断是否包含
    public boolean contains(Object o) {
        return indexOf(o, 0) >= 0;
    }

    // 查询元素第一次出现的下标
    public int indexOf(Object o) {
        return indexOf(o, 0);
    }

    // 从指定下标开始查询目标元素
    public synchronized int indexOf(Object o, int index) {
        // 遍历查询
        if (o == null) {
            for (int i = index ; i < elementCount ; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = index ; i < elementCount ; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    // 查询元素最后一次出现的下标
    public synchronized int lastIndexOf(Object o) {
        return lastIndexOf(o, elementCount-1);
    }

    // 从指定下标开始查询目标元素
    public synchronized int lastIndexOf(Object o, int index) {
        // 下标检查
        if (index >= elementCount)
            throw new IndexOutOfBoundsException(index + " >= "+ elementCount);
        // 从后往前检查
        if (o == null) {
            for (int i = index; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = index; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    // 获取指定下标元素
    public synchronized E elementAt(int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
        }

        return elementData(index);
    }

    // 获取第一个元素
    public synchronized E firstElement() {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData(0);
    }

    // 获取最后一个元素
    public synchronized E lastElement() {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData(elementCount - 1);
    }

    // 设置指定下标元素
    public synchronized void setElementAt(E obj, int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " +
                                                     elementCount);
        }
        elementData[index] = obj;
    }

    // 移除指定下标元素
    public synchronized void removeElementAt(int index) {
        modCount++;
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " +
                                                     elementCount);
        }
        else if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        // 移除下标之后的元素进行搬移
        int j = elementCount - index - 1;
        if (j > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, j);
        }
        // 删除元素
        elementCount--;
        elementData[elementCount] = null; /* to let gc do its work */
    }


    // 给指定下标插入元素
    public synchronized void insertElementAt(E obj, int index) {
        modCount++;
        // 下标检查
        if (index > elementCount) {
            throw new ArrayIndexOutOfBoundsException(index
                                                     + " > " + elementCount);
        }
        // 容量检查
        ensureCapacityHelper(elementCount + 1);
        // 将index之后的元素往后拷贝
        System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
        // 插入
        elementData[index] = obj;
        elementCount++;
    }

    // 尾部新增元素
    public synchronized void addElement(E obj) {
        modCount++;
        // 容量检查
        ensureCapacityHelper(elementCount + 1);
        // 新增
        elementData[elementCount++] = obj;
    }

    // 移除第一个出现的匹配元素
    public synchronized boolean removeElement(Object obj) {
        modCount++;
        // 查找下标
        int i = indexOf(obj);
        if (i >= 0) {
            // 找到目标元素，移除
            removeElementAt(i);
            return true;
        }
        // 没有找到目标元素
        return false;
    }


    // 移除所有元素
    public synchronized void removeAllElements() {
        modCount++;
        // Let gc do its work
        // 清除所有元素
        for (int i = 0; i < elementCount; i++)
            elementData[i] = null;
        // 修改数量
        elementCount = 0;
    }

    // 克隆
    public synchronized Object clone() {
        try {
            // 克隆新对象
            @SuppressWarnings("unchecked")
                Vector<E> v = (Vector<E>) super.clone();
            // 拷贝数组
            v.elementData = Arrays.copyOf(elementData, elementCount);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    // 转换数组
    public synchronized Object[] toArray() {
        return Arrays.copyOf(elementData, elementCount);
    }

    // 转换泛型数组
    @SuppressWarnings("unchecked")
    public synchronized <T> T[] toArray(T[] a) {
        // 返回a和elementData里长度大的，多的元素全部改为null

        if (a.length < elementCount)
            return (T[]) Arrays.copyOf(elementData, elementCount, a.getClass());

        System.arraycopy(elementData, 0, a, 0, elementCount);

        if (a.length > elementCount)
            a[elementCount] = null;

        return a;
    }

    // ========================== 下标访问操作 ======================================

    // 获取指定下标元素
    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    // 根据下标获取
    public synchronized E get(int index) {
        // 下标检查 没有负值检查???
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        return elementData(index);
    }

    // 设置指定下标的元素
    public synchronized E set(int index, E element) {
        // 下标检查 没有负值检查???
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        // 获取旧值
        E oldValue = elementData(index);
        // 更新
        elementData[index] = element;
        return oldValue;
    }


    // 新增元素
    public synchronized boolean add(E e) {
        modCount++;
        // 容量检查
        ensureCapacityHelper(elementCount + 1);
        // 添加末尾
        elementData[elementCount++] = e;
        return true;
    }

    // 移除元素
    public boolean remove(Object o) {
        return removeElement(o);
    }

    // 制定下标新增
    public void add(int index, E element) {
        insertElementAt(element, index);
    }

    // 移除指定下标元素
    public synchronized E remove(int index) {
        modCount++;
        // 下标检查
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        // 获取移除的值
        E oldValue = elementData(index);

        // 检查是否需要搬移后面的数据
        int numMoved = elementCount - index - 1;
        if (numMoved > 0)
            // 搬移数据
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);

        // 末尾数据改为null
        elementData[--elementCount] = null; // Let gc do its work

        return oldValue;
    }

    // 清空集合
    public void clear() {
        removeAllElements();
    }

    // ============================ 批量操作 ===========================

    // 判断是否全部包含，继承父类，循环调用contain方法
    public synchronized boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }


    // 批量新增
    public synchronized boolean addAll(Collection<? extends E> c) {
        modCount++;
        Object[] a = c.toArray();
        int numNew = a.length;
        // 容量检查
        ensureCapacityHelper(elementCount + numNew);
        // 拷贝到末尾
        System.arraycopy(a, 0, elementData, elementCount, numNew);
        elementCount += numNew;
        return numNew != 0;
    }

    // 批量移除，迭代器移除，会导致多次数据搬移，性能差!!
    public synchronized boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    // 保留两个集合的交集，迭代器移除，会导致多次数据搬移，性能差!!
    public synchronized boolean retainAll(Collection<?> c) {
        return super.retainAll(c);
    }

    // 指定下标开始新增
    public synchronized boolean addAll(int index, Collection<? extends E> c) {
        modCount++;
        // 下标检查
        if (index < 0 || index > elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        // 容量检查
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityHelper(elementCount + numNew);

        // 元素搬移
        int numMoved = elementCount - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        // 拷贝
        System.arraycopy(a, 0, elementData, index, numNew);
        elementCount += numNew;
        return numNew != 0;
    }

    // 是否相等
    public synchronized boolean equals(Object o) {
        return super.equals(o);
    }

    // 哈希值
    public synchronized int hashCode() {
        return super.hashCode();
    }

    // 输出
    public synchronized String toString() {
        return super.toString();
    }

    // 切分
    public synchronized List<E> subList(int fromIndex, int toIndex) {
        return Collections.synchronizedList(super.subList(fromIndex, toIndex),
                                            this);
    }

    // 范围移除
    protected synchronized void removeRange(int fromIndex, int toIndex) {
        modCount++;
        // 从后往前覆盖
        int numMoved = elementCount - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // 清除多的元素
        int newElementCount = elementCount - (toIndex-fromIndex);
        while (elementCount != newElementCount)
            elementData[--elementCount] = null;
    }


    // 序列化
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        final java.io.ObjectOutputStream.PutField fields = s.putFields();
        final Object[] data;
        synchronized (this) {
            fields.put("capacityIncrement", capacityIncrement);
            fields.put("elementCount", elementCount);
            data = elementData.clone();
        }
        fields.put("elementData", data);
        s.writeFields();
    }

    // 返回list迭代器
    public synchronized ListIterator<E> listIterator(int index) {
        if (index < 0 || index > elementCount)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    public synchronized ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    // 返回迭代器
    public synchronized Iterator<E> iterator() {
        return new Itr();
    }

    // 重写父类
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        public boolean hasNext() {
            // Racy but within spec, since modifications are checked
            // within or after synchronization in next/previous
            return cursor != elementCount;
        }

        public E next() {
            synchronized (Vector.this) {
                checkForComodification();
                int i = cursor;
                if (i >= elementCount)
                    throw new NoSuchElementException();
                cursor = i + 1;
                return elementData(lastRet = i);
            }
        }

        public void remove() {
            if (lastRet == -1)
                throw new IllegalStateException();
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.remove(lastRet);
                expectedModCount = modCount;
            }
            cursor = lastRet;
            lastRet = -1;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            synchronized (Vector.this) {
                final int size = elementCount;
                int i = cursor;
                if (i >= size) {
                    return;
                }
        @SuppressWarnings("unchecked")
                final E[] elementData = (E[]) Vector.this.elementData;
                if (i >= elementData.length) {
                    throw new ConcurrentModificationException();
                }
                while (i != size && modCount == expectedModCount) {
                    action.accept(elementData[i++]);
                }
                // update once at end of iteration to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * An optimized version of AbstractList.ListItr
     */
    final class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public E previous() {
            synchronized (Vector.this) {
                checkForComodification();
                int i = cursor - 1;
                if (i < 0)
                    throw new NoSuchElementException();
                cursor = i;
                return elementData(lastRet = i);
            }
        }

        public void set(E e) {
            if (lastRet == -1)
                throw new IllegalStateException();
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.set(lastRet, e);
            }
        }

        public void add(E e) {
            int i = cursor;
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.add(i, e);
                expectedModCount = modCount;
            }
            cursor = i + 1;
            lastRet = -1;
        }
    }

    // foreach遍历
    @Override
    public synchronized void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
        final E[] elementData = (E[]) this.elementData;
        final int elementCount = this.elementCount;
        for (int i=0; modCount == expectedModCount && i < elementCount; i++) {
            action.accept(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    // 移除满足条件的元素
    @Override
    @SuppressWarnings("unchecked")
    public synchronized boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // figure out which elements are to be removed
        // any exception thrown from the filter predicate at this stage
        // will leave the collection unmodified
        int removeCount = 0;
        final int size = elementCount;
        // 记录满足条件的元素下标
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                removeSet.set(i);
                removeCount++;
            }
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }

        // shift surviving elements left over the spaces left by removed elements
        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            // 需要保留的元素依次往前覆盖
            final int newSize = size - removeCount;
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
                i = removeSet.nextClearBit(i);
                elementData[j] = elementData[i];
            }
            // 多余的清除
            for (int k=newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            elementCount = newSize;
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            modCount++;
        }

        return anyToRemove;
    }

    // 批量替换
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        final int expectedModCount = modCount;
        final int size = elementCount;
        // 循环遍历处理
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            elementData[i] = operator.apply((E) elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    // 排序
    @SuppressWarnings("unchecked")
    @Override
    public synchronized void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        // 调用Arrays.sort
        Arrays.sort((E[]) elementData, 0, elementCount, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    // 切分器
    @Override
    public Spliterator<E> spliterator() {
        return new VectorSpliterator<>(this, null, 0, -1, 0);
    }

    static final class VectorSpliterator<E> implements Spliterator<E> {
        private final Vector<E> list;
        private Object[] array;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index
        private int expectedModCount; // initialized when fence set

        /** Create new spliterator covering the given  range */
        VectorSpliterator(Vector<E> list, Object[] array, int origin, int fence,
                          int expectedModCount) {
            this.list = list;
            this.array = array;
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        private int getFence() { // initialize on first use
            int hi;
            if ((hi = fence) < 0) {
                synchronized(list) {
                    array = list.elementData;
                    expectedModCount = list.modCount;
                    hi = fence = list.elementCount;
                }
            }
            return hi;
        }

        public Spliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null :
                new VectorSpliterator<E>(list, array, lo, index = mid,
                                         expectedModCount);
        }

        @SuppressWarnings("unchecked")
        public boolean tryAdvance(Consumer<? super E> action) {
            int i;
            if (action == null)
                throw new NullPointerException();
            if (getFence() > (i = index)) {
                index = i + 1;
                action.accept((E)array[i]);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi; // hoist accesses and checks from loop
            Vector<E> lst; Object[] a;
            if (action == null)
                throw new NullPointerException();
            if ((lst = list) != null) {
                if ((hi = fence) < 0) {
                    synchronized(lst) {
                        expectedModCount = lst.modCount;
                        a = array = lst.elementData;
                        hi = fence = lst.elementCount;
                    }
                }
                else
                    a = array;
                if (a != null && (i = index) >= 0 && (index = hi) <= a.length) {
                    while (i < hi)
                        action.accept((E) a[i++]);
                    if (lst.modCount == expectedModCount)
                        return;
                }
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return (long) (getFence() - index);
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }
}
