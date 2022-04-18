package java.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 基于数组的扩展，支持动态扩容、下标检查、克隆等特性
 * 非线程安全，当并发访问时，支持快速失败策略
 */
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{

    // 序列化ID
    private static final long serialVersionUID = 8683452581122892189L;

    // 初始容量
    private static final int DEFAULT_CAPACITY = 10;

    // 当数组为空时的值，避免频繁创建数组对象 ==> 主动声明初始容量为0时使用，add时扩容从0开始，1.5扩容
    private static final Object[] EMPTY_ELEMENTDATA = {};

    // 当数组为空时的值，避免频繁创建数组对象 ==> 默认构造方法时使用，add时扩容直接从10开始
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    // 底层数组，用于真正存储数据
    // transient代表序列化时忽略此属性，ArrayList重写了readObject和writeObject方法，定制了序列化的行为
    transient Object[] elementData; // non-private to simplify nested class access

    // 当前集合的大小
    private int size;

    // 构造方法，指定初始容量
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            // 直接创建目标大小数组，避免频繁扩容
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            // 空数组，内置的instance，避免频繁创建数组对象
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    // 默认构造方法，初始容量10，数组为空数组，第一次add时再真正创建数组
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    // 构造方法，根据指定集合创建ArrayList
    public ArrayList(Collection<? extends E> c) {
        // 转换数组
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // 数组非空
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                // 数组拷贝一下 ==> c.toArray返回的数组虽然是Object[]，但其内部可能是真正的泛型类型，因此重新拷贝一下
                // Jdk8返回的是泛型数组，Jdk9改为范围Object[]
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // 空数组
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    // 数组缩容，节省内存
    public void trimToSize() {
        // 修改次数++
        modCount++;
        if (size < elementData.length) {
            // 当前容量小于数组大小，进行缩容
            elementData = (size == 0)
                    // 如果是空数组，不创建新的，直接使用EMPTY_ELEMENTDATA
              ? EMPTY_ELEMENTDATA
                    // 数组拷贝，拷贝elementData从0到size的数据到一个新数组
              : Arrays.copyOf(elementData, size);
        }
    }

    // 数组容量检查，不足时进行扩容
    public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                // 初始化时指定容量为0，则扩容从0开始
            ? 0
                // 空参构造方法，初始容量从10开始
            : DEFAULT_CAPACITY;

        if (minCapacity > minExpand) {
            // 需要的容量大于当前的容量，进行库容
            ensureExplicitCapacity(minCapacity);
        }
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            // 默认构造方法初始化，计算需要的最小容量，最低从10开始
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    // 扩容操作
    private void ensureExplicitCapacity(int minCapacity) {
        // 变更++
        modCount++;

        // 扩容
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    // 数组最大长度
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    // 扩容操作，要求最新容量为minCapacity
    private void grow(int minCapacity) {
        // 之前的容量
        int oldCapacity = elementData.length;
        // 扩容1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            // 扩容1.5倍之后还是不足，则扩容到minCapacity
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            // 数组越界
            newCapacity = hugeCapacity(minCapacity);
        // 拷贝数组到新数组
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0)
            throw new OutOfMemoryError();
        // 最大长度修正
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    // 返回集合元素个数
    public int size() {
        return size;
    }

    // 判断集合是否为空
    public boolean isEmpty() {
        return size == 0;
    }


    // 判断集合是否包含某个元素
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // 查找某个元素在集合中出现的第一个索引下标
    public int indexOf(Object o) {
        if (o == null) {
            // 查null
            for (int i = 0; i < size; i++)
                // 遍历查找
                if (elementData[i]==null)
                    return i;
        } else {
            // 非null
            for (int i = 0; i < size; i++)
                // 遍历
                if (o.equals(elementData[i]))
                    return i;
        }
        // 没找到
        return -1;
    }

    // 查找某个元素在集合中出现的最后一个索引下标
    public int lastIndexOf(Object o) {
        // 遍历
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        // 未找到
        return -1;
    }


    // 克隆， 返回的ArrayList是一个新的实例，但里面的元素还是之前的引用
    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }


    // 转换数组
    public Object[] toArray() {
        // 直接拷贝
        return Arrays.copyOf(elementData, size);
    }

    // 转换泛型数组
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // 拷贝完整数组
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        // 拷贝完整数组，并且长度和a保持一致
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    // ================================= 下标访问操作 =======================================

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    // 根据下标获取
    public E get(int index) {
        // index检查
        rangeCheck(index);
        // 获取
        return elementData(index);
    }

    // 设置指定下标的元素
    public E set(int index, E element) {
        // index检查
        rangeCheck(index);
        // 获取旧值
        E oldValue = elementData(index);
        // 更新
        elementData[index] = element;
        // 返回旧值
        return oldValue;
    }

    // 集合末尾新增元素
    public boolean add(E e) {
        // 容量检查
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        // 新增
        elementData[size++] = e;
        return true;
    }

    // 指定下标新增元素
    public void add(int index, E element) {
        // index检查
        rangeCheckForAdd(index);
        // 容量检查
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        // index之后的数据拷贝
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        // 新增元素
        elementData[index] = element;
        // 更新size
        size++;
    }

    // 移除指定下标元素
    public E remove(int index) {
        // index检查
        rangeCheck(index);

        // 变更计数
        modCount++;
        // 获取旧值
        E oldValue = elementData(index);

        // 计算要搬移的元素个数
        int numMoved = size - index - 1;
        if (numMoved > 0)
            // 元素搬移
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        // 末尾元素设置为null，进行GC
        elementData[--size] = null; // clear to let GC do its work

        // 返回旧值
        return oldValue;
    }

    // 移除匹配的第一个元素
    public boolean remove(Object o) {
        if (o == null) {
            // 移除null
            for (int index = 0; index < size; index++)
                // 查找目标元素下标
                if (elementData[index] == null) {
                    // 根据下标移除
                    fastRemove(index);
                    return true;
                }
        } else {
            // 非null
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    // 根据元素下标快速移除
    private void fastRemove(int index) {
        // 变更记录+1
        modCount++;
        // 计算后面要搬移的元素个数
        int numMoved = size - index - 1;
        if (numMoved > 0)
            // 元素搬移
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        // 末尾元素设置为null，进行gc
        elementData[--size] = null; // clear to let GC do its work
    }


    // 清空集合元素
    public void clear() {
        // 变更记录+1
        modCount++;

        // 逐个遍历，将元素设置为null，帮助GC
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        // 更新元素个数
        size = 0;
    }


    // 批量新增
    public boolean addAll(Collection<? extends E> c) {
        // 转数组
        Object[] a = c.toArray();
        // 计算容量是否足够，不足则扩容
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        // 数组搬移，将c整个拷贝进原数组
        System.arraycopy(a, 0, elementData, size, numNew);
        // 修改数量
        size += numNew;
        return numNew != 0;
    }

    // 从指定下标开始批量插入
    public boolean addAll(int index, Collection<? extends E> c) {
        // 下标检查
        rangeCheckForAdd(index);

        // 容量计算
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        // 数组搬移计算
        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        // 拷贝目标数组进原数组
        System.arraycopy(a, 0, elementData, index, numNew);
        // 更新大小
        size += numNew;
        return numNew != 0;
    }


    // 根据下标范围移除
    protected void removeRange(int fromIndex, int toIndex) {
        // 变更记录+1
        modCount++;

        // 计算要搬移的元素个数
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // 更新大小，并将多余的元素清除
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }


    // 访问时的下标越界检查
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // 新增时的下标越界检查
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // 下标越界时的异常提示
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }


    // 移除c中包含的所有元素
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }


    // 保留与c数组的交集部分，与removeAll相反
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    /**
     * @param c 参数数组
     * @param complement    参数数组包含此元素时保留，还是不包含时保留
     * @return
     */
    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        // r是遍历原数组的下标，w是保留新数组的下标
        int r = 0, w = 0;
        boolean modified = false;
        try {
            // 遍历原数组
            for (; r < size; r++)
                // 检查符合条件的元素
                if (c.contains(elementData[r]) == complement)
                    // 覆盖到原数组里
                    elementData[w++] = elementData[r];
        } finally {
            // 正常情况，r必然等于size
            // 此处是为了兼容c在contain方法抛出异常的情况
            if (r != size) {
                // 抛出异常后面还没有遍历的元素，全部认为是满足条件的，拷贝到w的后面
                System.arraycopy(elementData, r,
                                 elementData, w,
                                 size - r);
                // 更新w的数量
                w += size - r;
            }
            // 此处代表有元素不满足条件，只有w个元素满足条件，并且此时这些满足条件的元素已经被拷贝到0到w的位置了
            if (w != size) {
                // 将不满足条件的元素清除
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                // 更新modCount和size
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }


    // 序列化
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
        // 保存开始序列化时的变更记录
        int expectedModCount = modCount;

        // 写入非静态属性、非 transient 属性
        s.defaultWriteObject();

        // 写入 size ，主要为了与 clone 方法的兼容 (size非transient属性，其实上面的方法已经写入了)
        s.writeInt(size);

        // 逐个写入 elementData 数组的元素 (elementData数组开辟的空间可能并没有用完，原生的序列化会把后面的null也写入，为了节省空间，因此重写了序列化方法，只写入有数据的[0-size)元素)
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }

        // 序列化过程中有其他线程修改过数组，抛出异常
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    // 反序列化
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {

        // 先设置为空数组
        elementData = EMPTY_ELEMENTDATA;

        // 读取非静态属性、非 transient 属性
        s.defaultReadObject();

        // 读取 size ，不过忽略返回值，不用 (默认方法里已经读取出size并设置了)
        s.readInt(); // ignored

        if (size > 0) {
            // 数组扩容，确保内存足够
            ensureCapacityInternal(size);

            // 按顺序逐步读取所有元素并赋值
            Object[] a = elementData;
            for (int i=0; i<size; i++) {
                a[i] = s.readObject();
            }
        }
    }

    // 从指定下标返回list迭代器
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    // 从开始下标返回List迭代器
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    // 返回默认迭代器，只支持单向
    public Iterator<E> iterator() {
        return new Itr();
    }

    // 基础迭代器实现，没有使用抽象类的迭代器
    private class Itr implements Iterator<E> {
        int cursor;       // 游标，下一个要遍历元素的下标
        int lastRet = -1; // 上一个遍历元素的下标，-1代表还没有元素被遍历
        int expectedModCount = modCount;    // 变更记录数 检测是否存在并发修改，实现fast-fail机制

        public boolean hasNext() {
            // 判断是否还有下一个
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            // 并发检查
            checkForComodification();
            int i = cursor;
            if (i >= size)
                // 下标检查
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                // 刚才确定过，元素下标小于size，是存在的，此时必然是并发操作导致的
                throw new ConcurrentModificationException();
            // 更新游标
            cursor = i + 1;
            // 更新lastRet 并 返回元素
            return (E) elementData[lastRet = i];
        }

        // 移除刚才遍历的元素
        public void remove() {
            // 下标检查，必须先遍历过元素，才能再移除
            if (lastRet < 0)
                throw new IllegalStateException();
            // 并发检查
            checkForComodification();

            try {
                // 移除
                ArrayList.this.remove(lastRet);
                // 更新游标
                cursor = lastRet;
                // 更新记录下标
                lastRet = -1;
                // 更新变更记录数
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                // lastRet是之前访问过的元素，因此必然存在，如果下标越界，必然是其他线程操作了
                throw new ConcurrentModificationException();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size) {
                // 所有元素都遍历过了，直接返回
                // 注意这个坑，这个方法并非遍历所有元素，而是针对还没有遍历过的元素
                return;
            }
            final Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            // 针对还没遍历的元素，逐个遍历，交给consumer处理
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // 更新游标和记录下标
            cursor = i;
            lastRet = i - 1;
            // 并发检查
            checkForComodification();
        }

        // 并发检查
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }


    // 针对List迭代器，一样重写了父类的
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        // 前面是否还有元素
        public boolean hasPrevious() {
            return cursor != 0;
        }

        // 下一个要遍历元素的下标
        public int nextIndex() {
            return cursor;
        }

        // 上一个遍历过的元素的下标
        public int previousIndex() {
            return cursor - 1;
        }

        // 向前遍历
        @SuppressWarnings("unchecked")
        public E previous() {
            // 并发检查
            checkForComodification();
            // 下标检查
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            // 更新游标、下标、返回元素
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            // 下标检查
            if (lastRet < 0)
                throw new IllegalStateException();
            // 并发检查
            checkForComodification();

            try {
                // 修改刚刚遍历的元素的数据
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            // 并发检查
            checkForComodification();

            try {
                // 新增元素
                int i = cursor;
                ArrayList.this.add(i, e);
                // 更新游标等
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }


    // 数组切分
    public List<E> subList(int fromIndex, int toIndex) {
        // 下标检查
        subListRangeCheck(fromIndex, toIndex, size);
        // 切分
        return new SubList(this, 0, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }

    // 依旧没有采用父类，自己重写
    private class SubList extends AbstractList<E> implements RandomAccess {
        // 原数组引用
        private final AbstractList<E> parent;
        // 原数组开始下标
        private final int parentOffset;
        // 此数组开始下标
        private final int offset;
        // 此数组大小
        int size;

        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayList.this.modCount;
        }

        // 修改元素，会影响原数组
        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        // 获取下标元素
        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }

        // 获取大小
        public int size() {
            checkForComodification();
            return this.size;
        }

        // 新增，会影响原数组
        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        // 移除，会影响原数组
        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        // 范围移除，会影响原数组
        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            parent.removeRange(parentOffset + fromIndex,
                               parentOffset + toIndex);
            this.modCount = parent.modCount;
            this.size -= toIndex - fromIndex;
        }


        // 批量新增，会影响原数组
        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }


        // 批量新增，会影响原数组
        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;

            checkForComodification();
            parent.addAll(parentOffset + index, c);
            this.modCount = parent.modCount;
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = ArrayList.this.modCount;

                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                @SuppressWarnings("unchecked")
                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    final int size = SubList.this.size;
                    int i = cursor;
                    if (i >= size) {
                        return;
                    }
                    final Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length) {
                        throw new ConcurrentModificationException();
                    }
                    while (i != size && modCount == expectedModCount) {
                        consumer.accept((E) elementData[offset + (i++)]);
                    }
                    // update once at end of iteration to reduce heap write traffic
                    lastRet = cursor = i;
                    checkForComodification();
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        ArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkForComodification();

                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (expectedModCount != ArrayList.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

        public Spliterator<E> spliterator() {
            checkForComodification();
            return new ArrayListSpliterator<E>(ArrayList.this, offset,
                                               offset + this.size, this.modCount);
        }
    }

    // 遍历所有元素，交给action处理
    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        // 记录变更次数
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
        final E[] elementData = (E[]) this.elementData;
        final int size = this.size;
        // 逐个遍历处理
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            action.accept(elementData[i]);
        }
        // 并发检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    // 切分器
    @Override
    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator<>(this, 0, -1, 0);
    }

    // TODO 使用较少，后续更新
    /** Index-based split-by-two, lazily initialized Spliterator */
    static final class ArrayListSpliterator<E> implements Spliterator<E> {

        private final ArrayList<E> list;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index
        private int expectedModCount; // initialized when fence set

        /** Create new spliterator covering the given  range */
        ArrayListSpliterator(ArrayList<E> list, int origin, int fence,
                             int expectedModCount) {
            this.list = list; // OK if null unless traversed
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        private int getFence() { // initialize fence to size on first use
            int hi; // (a specialized variant appears in method forEach)
            ArrayList<E> lst;
            if ((hi = fence) < 0) {
                if ((lst = list) == null)
                    hi = fence = 0;
                else {
                    expectedModCount = lst.modCount;
                    hi = fence = lst.size;
                }
            }
            return hi;
        }

        public ArrayListSpliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : // divide range in half unless too small
                new ArrayListSpliterator<E>(list, lo, index = mid,
                                            expectedModCount);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null)
                throw new NullPointerException();
            int hi = getFence(), i = index;
            if (i < hi) {
                index = i + 1;
                @SuppressWarnings("unchecked") E e = (E)list.elementData[i];
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi, mc; // hoist accesses and checks from loop
            ArrayList<E> lst; Object[] a;
            if (action == null)
                throw new NullPointerException();
            if ((lst = list) != null && (a = lst.elementData) != null) {
                if ((hi = fence) < 0) {
                    mc = lst.modCount;
                    hi = lst.size;
                }
                else
                    mc = expectedModCount;
                if ((i = index) >= 0 && (index = hi) <= a.length) {
                    for (; i < hi; ++i) {
                        @SuppressWarnings("unchecked") E e = (E) a[i];
                        action.accept(e);
                    }
                    if (lst.modCount == mc)
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


    // 根据条件移除
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // 记录移除数量
        int removeCount = 0;
        // 记录移除的所有下标
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                // 满足条件，记录一下，等会统一移除
                removeSet.set(i);
                removeCount++;
            }
        }
        // 并发检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }


        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            // 存在要移除的元素
            final int newSize = size - removeCount;
            // 将不需要移除的元素全部从0开始往前覆盖
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
                // 获取不需要移除的元素
                i = removeSet.nextClearBit(i);
                // 保存
                elementData[j] = elementData[i];
            }
            // 移除多余的元素
            for (int k=newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            // 更新size
            this.size = newSize;
            // 并发检查
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            modCount++;
        }

        return anyToRemove;
    }


    // 根据条件表达式替换
    @Override
    @SuppressWarnings("unchecked")
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        // 记录当前变更次数
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            // 逐个遍历替换
            elementData[i] = operator.apply((E) elementData[i]);
        }
        // 并发检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        // 直接调用Arrays的sort方法进行排序
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }
}
