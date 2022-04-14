package java.util;


/**
 * List接口的抽象实现，实现部分通用接口，减少底层实现类的开发量
 */
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {

    // 唯一构造方法，只能子类调用
    protected AbstractList() {
    }

    // 集合添加元素，添加的末尾，减少元素搬移工作
    public boolean add(E e) {
        // 调用add(index, value)实现
        add(size(), e);
        return true;
    }

    // 获取指定下标的元素
    abstract public E get(int index);

    // 设置指定下标的元素
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    // 给指定下标新增一个元素
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    // 移除指定下标的元素
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    // ============================ 查找操作 =========================================

    // 查找目标元素在集合中出现的第一个下标
    public int indexOf(Object o) {
        ListIterator<E> it = listIterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    return it.previousIndex();
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return it.previousIndex();
        }
        return -1;
    }

    // 查找目标元素在集合中出现的最后一个下标
    public int lastIndexOf(Object o) {
        // 从尾向前遍历
        ListIterator<E> it = listIterator(size());
        if (o==null) {
            while (it.hasPrevious())
                if (it.previous()==null)
                    return it.nextIndex();
        } else {
            while (it.hasPrevious())
                if (o.equals(it.previous()))
                    return it.nextIndex();
        }
        return -1;
    }


    // ========================= 批量操作 ============================

    // 清空集合
    public void clear() {
        removeRange(0, size());
    }

    // 批量新增
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);
        boolean modified = false;
        for (E e : c) {
            add(index++, e);
            modified = true;
        }
        return modified;
    }


    // ========================= 迭代器 ===================================

    // 返回List迭代器
    public Iterator<E> iterator() {
        return new Itr();
    }

    // 返回List迭代器
    public ListIterator<E> listIterator() {
        // 从头开始遍历
        return listIterator(0);
    }

    // 从指定下标返回迭代器
    public ListIterator<E> listIterator(final int index) {
        rangeCheckForAdd(index);

        return new ListItr(index);
    }

    // List迭代器的实现
    private class Itr implements Iterator<E> {

        // 游标
        int cursor = 0;

        // 最近一次调用next或prev的元素的下标，如果删除，则置为-1
        int lastRet = -1;

        // 检测是否存在并发修改，实现fast-fail机制
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        // 获取下一个元素
        public E next() {
            checkForComodification();
            try {
                int i = cursor;
                // 获取值
                E next = get(i);
                // 更新游标和上一个元素下标
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                // 检查是否并发导致
                checkForComodification();
                // 非并发导致，抛出异常
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            // 上次操作元素下标检查
            if (lastRet < 0)
                throw new IllegalStateException();
            // 并发操作检查
            checkForComodification();

            try {
                // 移除指定下标的元素
                AbstractList.this.remove(lastRet);
                if (lastRet < cursor)
                    // 游标更新
                    cursor--;
                // 删除成功，lastRet置为-1
                lastRet = -1;
                // list调用remove更新了modCount，因此更新expectedModCount
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                // lastRet是之前访问过的元素，因此必然存在，如果下标越界，必然是其他线程操作了
                throw new ConcurrentModificationException();
            }
        }

        // 快速失败策略，每次遍历之前检查是否存在并发操作
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    // Itr的增强
    private class ListItr extends Itr implements ListIterator<E> {

        // 支持从指定下标开始遍历
        ListItr(int index) {
            cursor = index;
        }

        // 判断前面是否还有元素
        public boolean hasPrevious() {
            return cursor != 0;
        }

        // 向前遍历
        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                E previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        // 当前元素的下标
        public int nextIndex() {
            return cursor;
        }

        // 上个元素的下标
        public int previousIndex() {
            return cursor-1;
        }

        // 更新刚刚遍历的元素
        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractList.this.set(lastRet, e);
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        // 新增元素
        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                AbstractList.this.add(i, e);
                lastRet = -1;
                cursor = i + 1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    // 集合根据范围切分，返回只读的视图
    public List<E> subList(int fromIndex, int toIndex) {
        return (this instanceof RandomAccess ?
                new RandomAccessSubList<>(this, fromIndex, toIndex) :
                new SubList<>(this, fromIndex, toIndex));
    }

    // ===================== 比较和哈希 ===================================


    // 相等比较，要求两个集合元素和顺序完全一致则相等
    public boolean equals(Object o) {
        // 引用判断
        if (o == this)
            return true;
        // 类型判断
        if (!(o instanceof List))
            return false;

        // 获取迭代器
        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        // 逐一对比
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    // 计算哈希值
    public int hashCode() {
        int hashCode = 1;
        // 和每个元素都有关
        for (E e : this)
            hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
        return hashCode;
    }

    // 移除指定范围的元素
    protected void removeRange(int fromIndex, int toIndex) {
        // 迭代器移除
        ListIterator<E> it = listIterator(fromIndex);
        for (int i=0, n=toIndex-fromIndex; i<n; i++) {
            it.next();
            it.remove();
        }
    }

    // 此集合修改的次数，用来实现fast-fail
    protected transient int modCount = 0;

    // 下标合法性校验
    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // 地址越界提示
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size();
    }
}

// 切分集合，从当前的实现来看，切分的子集合对api的支持依赖于原集合
class SubList<E> extends AbstractList<E> {

    // 原集合的引用
    private final AbstractList<E> l;
    // 偏移量，即原集合截取的开始下标
    private final int offset;
    // 大小
    private int size;

    SubList(AbstractList<E> list, int fromIndex, int toIndex) {
        // 参数校验
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > list.size())
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
        // 保留原集合引用
        l = list;
        // 开始下标
        offset = fromIndex;
        // 长度
        size = toIndex - fromIndex;
        // 修改次数
        this.modCount = l.modCount;
    }

    // 设置指定下标元素
    public E set(int index, E element) {
        rangeCheck(index);
        checkForComodification();
        return l.set(index+offset, element);
    }

    // 获取元素
    public E get(int index) {
        rangeCheck(index);
        checkForComodification();
        return l.get(index+offset);
    }

    // 获取大小
    public int size() {
        checkForComodification();
        return size;
    }

    // 新增元素
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        checkForComodification();
        l.add(index+offset, element);
        this.modCount = l.modCount;
        size++;
    }

    // 移除元素
    public E remove(int index) {
        rangeCheck(index);
        checkForComodification();
        E result = l.remove(index+offset);
        this.modCount = l.modCount;
        size--;
        return result;
    }

    // 范围移除
    protected void removeRange(int fromIndex, int toIndex) {
        checkForComodification();
        l.removeRange(fromIndex+offset, toIndex+offset);
        this.modCount = l.modCount;
        size -= (toIndex-fromIndex);
    }

    // 批量新增
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    // 指定下标开始批量新增
    public boolean addAll(int index, Collection<? extends E> c) {
        // 范围检查
        rangeCheckForAdd(index);
        int cSize = c.size();
        if (cSize==0)
            return false;

        // 并发检查
        checkForComodification();
        l.addAll(offset+index, c);
        this.modCount = l.modCount;
        size += cSize;
        return true;
    }

    // 迭代器
    public Iterator<E> iterator() {
        return listIterator();
    }

    // 迭代器
    public ListIterator<E> listIterator(final int index) {
        checkForComodification();
        rangeCheckForAdd(index);

        return new ListIterator<E>() {
            // 创建新的迭代器，要计算offset
            private final ListIterator<E> i = l.listIterator(index+offset);

            public boolean hasNext() {
                return nextIndex() < size;
            }

            public E next() {
                if (hasNext())
                    return i.next();
                else
                    throw new NoSuchElementException();
            }

            public boolean hasPrevious() {
                return previousIndex() >= 0;
            }

            public E previous() {
                if (hasPrevious())
                    return i.previous();
                else
                    throw new NoSuchElementException();
            }

            public int nextIndex() {
                return i.nextIndex() - offset;
            }

            public int previousIndex() {
                return i.previousIndex() - offset;
            }

            public void remove() {
                i.remove();
                SubList.this.modCount = l.modCount;
                size--;
            }

            public void set(E e) {
                i.set(e);
            }

            public void add(E e) {
                i.add(e);
                SubList.this.modCount = l.modCount;
                size++;
            }
        };
    }

    // 切分集合
    public List<E> subList(int fromIndex, int toIndex) {
        return new SubList<>(this, fromIndex, toIndex);
    }

    // 下标检查
    private void rangeCheck(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // 下标检查
    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    private void checkForComodification() {
        if (this.modCount != l.modCount)
            throw new ConcurrentModificationException();
    }
}

// 实际作用和SubList一致，多了一个RandomAccess接口标识
class RandomAccessSubList<E> extends SubList<E> implements RandomAccess {
    RandomAccessSubList(AbstractList<E> list, int fromIndex, int toIndex) {
        super(list, fromIndex, toIndex);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return new RandomAccessSubList<>(this, fromIndex, toIndex);
    }
}
