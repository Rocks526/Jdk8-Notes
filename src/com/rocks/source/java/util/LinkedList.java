package java.util;

import java.util.function.Consumer;

// 双端链表、双端队列、栈
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{

    // transient不做序列化
    // 元素个数
    transient int size = 0;

    // 队头元素
    transient Node<E> first;

    // 队尾元素
    transient Node<E> last;

    // 空参
    public LinkedList() {
    }

    // 初始化集合
    public LinkedList(Collection<? extends E> c) {
        this();
        // 调用addAll添加
        addAll(c);
    }

    // 队头添加元素
    private void linkFirst(E e) {
        final Node<E> f = first;
        // 新元素
        final Node<E> newNode = new Node<>(null, e, f);
        // 更新first
        first = newNode;
        if (f == null)
            // 之前没有元素
            last = newNode;
        else
            // 更新之前头指针的prev
            f.prev = newNode;
        // 更新数量
        size++;
        modCount++;
    }

    // 队尾添加元素
    void linkLast(E e) {
        final Node<E> l = last;
        // 新元素
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            // 之前没有元素
            first = newNode;
        else
            // 更新之前尾指针的next
            l.next = newNode;
        // 更新数量
        size++;
        modCount++;
    }

    /**
     * 给节点后面新增一个元素
     * @param e 要新增的元素值
     * @param succ  给此节点前面新增
     */
    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;

        // 创建节点
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null)
            // succ是头结点
            first = newNode;
        else
            // 修改前节点的next指针
            pred.next = newNode;
        // 更新数量
        size++;
        modCount++;
    }

    // 移除非空的队头节点
    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;

        // 记录原数据
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.next = null; // help GC

        // 更新头指针
        first = next;
        if (next == null)
            // 只有一个元素，更新尾指针
            last = null;
        else
            // 取消头指针，本来是指向移除的元素的
            next.prev = null;
        // 更新大小
        size--;
        modCount++;
        return element;
    }

    // 移除非空的队尾元素
    private E unlinkLast(Node<E> l) {
        // assert l == last && l != null;

        // 记录原来的数据
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null;
        l.prev = null; // help GC

        // 更新尾指针
        last = prev;
        if (prev == null)
            // 只有一个元素，更新first
            first = null;
        else
            // 更新现在的尾指针的next
            prev.next = null;
        // 更新数量
        size--;
        modCount++;
        return element;
    }

    // 移除元素
    E unlink(Node<E> x) {
        // assert x != null;

        // 记录原来的引用
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            // 删除的元素是头结点
            first = next;
        } else {
            // 更新上个结点的尾指针，直接连接后续的链表
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            // 删除的元素是尾结点
            last = prev;
        } else {
            // 更新下个结点的前指针，直接连接前面的链表
            next.prev = prev;
            x.next = null;
        }

        // 更新size
        x.item = null;
        size--;
        modCount++;
        return element;
    }

    // 返回队头元素
    public E getFirst() {
        final Node<E> f = first;
        if (f == null)
            // 不存在则抛出异常
            throw new NoSuchElementException();
        return f.item;
    }

    // 返回队尾元素
    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            // 不存在抛出异常
            throw new NoSuchElementException();
        return l.item;
    }

    // 移除队头元素
    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            // 不存在抛出异常
            throw new NoSuchElementException();
        // 移除
        return unlinkFirst(f);
    }

    // 移除队尾元素
    public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            // 不存在抛出异常
            throw new NoSuchElementException();
        // 移除
        return unlinkLast(l);
    }


    // 队头添加元素，失败抛出异常
    public void addFirst(E e) {
        linkFirst(e);
    }

    // 队尾添加元素，失败抛出异常
    public void addLast(E e) {
        linkLast(e);
    }

    // 判断是否包含
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // 队列个数
    public int size() {
        return size;
    }

    // 集合添加元素
    public boolean add(E e) {
        // 队尾添加
        linkLast(e);
        return true;
    }

    // 移除第一个匹配项
    public boolean remove(Object o) {
        // 从头匹配
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    // 移除元素
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    // 批量添加
    public boolean addAll(Collection<? extends E> c) {
        // 从队尾添加
        return addAll(size, c);
    }


    // 从指定下标添加
    public boolean addAll(int index, Collection<? extends E> c) {
        // 下标检查
        checkPositionIndex(index);

        // 转换为数组
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            // 空数组
            return false;

        // pred是前指针，用于连接后面要插入的节点
        // succ记录index之后的节点，用于后续将链表连接起来
        Node<E> pred, succ;
        if (index == size) {
            // 从尾结点往后插
            succ = null;
            pred = last;
        } else {
            succ = node(index);
            pred = succ.prev;
        }

        for (Object o : a) {
            // 遍历插入
            @SuppressWarnings("unchecked") E e = (E) o;
            // 创建新节点
            Node<E> newNode = new Node<>(pred, e, null);
            if (pred == null)
                // 从头开始插入
                first = newNode;
            else
                // 从中间插入
                pred.next = newNode;
            // 更新前节点，继续插入
            pred = newNode;
        }

        if (succ == null) {
            // 插入的位置是之前的尾结点，不用连接之前的后半段链表，直接更新尾指针即可
            last = pred;
        } else {
            // 插入的位置是从原链表中间插入，将插入后的链表和原来的后半段链表连接起来
            pred.next = succ;
            succ.prev = pred;
        }

        // 更新大小
        size += numNew;
        modCount++;
        return true;
    }

    // 清空链表
    public void clear() {
        // 遍历清除即可
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        // 更新size和头尾引用
        first = last = null;
        size = 0;
        modCount++;
    }


    // ================================ 根据下标访问操作 ==============================================

    // 根据下标获取元素
    public E get(int index) {
        // 下标检查
        checkElementIndex(index);
        // 根据下标获取
        return node(index).item;
    }

    // 修改下标元素
    public E set(int index, E element) {
        // 下标检查
        checkElementIndex(index);
        // 获取下标元素
        Node<E> x = node(index);
        // 旧值
        E oldVal = x.item;
        // 更新
        x.item = element;
        return oldVal;
    }


    // 给指定下标添加元素
    public void add(int index, E element) {
        // 下标检查
        checkPositionIndex(index);

        // 链尾添加
        if (index == size)
            linkLast(element);
        else
            // 链表中间添加
            linkBefore(element, node(index));
    }


    // 根据下标移除
    public E remove(int index) {
        // 下标检查
        checkElementIndex(index);
        // 移除指定元素
        return unlink(node(index));
    }

    // 下标检查，用于根据下标访问，不能等于size
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    // 检查下标是否合法，用于根据下标新增，可以等于size
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    // 下标越界提示信息
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    // 下标检查，用于根据下标访问，不能等于size
    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // 检查下标是否合法，用于根据下标新增，可以等于size
    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }


    // 返回指定下标的节点
    Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
            // 在前半段，从前往后找
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            // 在后半段，从后往前找
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // ====================================== 搜索操作 =======================================

    // 获取目标元素在链表中第一次出现的下标
    public int indexOf(Object o) {
        // 从头查找
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }


    // 获取目标元素在链表中最后一次出现的下标
    public int lastIndexOf(Object o) {
        // 链表从尾部开始查找
        int index = size;
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (x.item == null)
                    return index;
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                index--;
                if (o.equals(x.item))
                    return index;
            }
        }
        return -1;
    }

    // ====================================== 队列操作 =================================================

    // 查看队头元素，失败时返回null
    public E peek() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    // 查看队头元素，失败时抛出异常
    public E element() {
        return getFirst();
    }

    // 队头取出元素，失败时返回null
    public E poll() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    // 队头取出元素，失败时抛出异常
    public E remove() {
        return removeFirst();
    }

    // 队尾添加元素，失败时返回false
    public boolean offer(E e) {
        return add(e);
    }

    // =========================================== 双端队列操作 ==========================================

    // 队头添加元素，失败返回false
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }


    // 队尾添加元素，失败返回false
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    // 查看队头元素，失败返回null
    public E peekFirst() {
        final Node<E> f = first;
        return (f == null) ? null : f.item;
     }

    // 查看队尾元素，失败返回null
    public E peekLast() {
        final Node<E> l = last;
        return (l == null) ? null : l.item;
    }

    // 队头取出元素，失败返回null
    public E pollFirst() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }


    // 队尾取出元素，失败返回null
    public E pollLast() {
        final Node<E> l = last;
        return (l == null) ? null : unlinkLast(l);
    }


    // 删除包含指定元素的第一个匹配项
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    // 删除包含指定元素的最后一个匹配项
    public boolean removeLastOccurrence(Object o) {
        if (o == null) {
            for (Node<E> x = last; x != null; x = x.prev) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = last; x != null; x = x.prev) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    // =========================== 栈操作 ====================================

    // 入栈
    public void push(E e) {
        addFirst(e);
    }

    // 出栈
    public E pop() {
        return removeFirst();
    }


    // 返回指定下标开始的迭代器
    public ListIterator<E> listIterator(int index) {
        // 下标检查
        checkPositionIndex(index);
        return new ListItr(index);
    }

    // 迭代器
    private class ListItr implements ListIterator<E> {
        // 上次返回的元素
        private Node<E> lastReturned;
        // 下一个要遍历的元素
        private Node<E> next;
        // 下一个要遍历的元素下标
        private int nextIndex;
        // 修改次数
        private int expectedModCount = modCount;

        // 从指定下标开始
        ListItr(int index) {
            // assert isPositionIndex(index);

            // 获取起始元素
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        public boolean hasNext() {
            // 是否还有下一个元素
            return nextIndex < size;
        }

        public E next() {
            // 并发检查
            checkForComodification();
            // 检查是否还有下一个
            if (!hasNext())
                throw new NoSuchElementException();
            // 更新指针
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.item;
        }

        public boolean hasPrevious() {
            // 是否还有上一个
            return nextIndex > 0;
        }

        public E previous() {
            // 并发检查
            checkForComodification();
            if (!hasPrevious())
                throw new NoSuchElementException();

            lastReturned = next = (next == null)  // 判断是否链表尾
                    ? last // 链尾
                    : next.prev;    // 非链尾
            nextIndex--;
            return lastReturned.item;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

        public void remove() {
            // 并发检查
            checkForComodification();
            // 是否遍历检查
            if (lastReturned == null)
                throw new IllegalStateException();

            // 删除之前记录后续链表节点
            Node<E> lastNext = lastReturned.next;
            // 删除
            unlink(lastReturned);
            if (next == lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
            expectedModCount++;
        }

        public void set(E e) {
            if (lastReturned == null)
                throw new IllegalStateException();
            checkForComodification();
            lastReturned.item = e;
        }

        public void add(E e) {
            checkForComodification();
            lastReturned = null;
            if (next == null)
                linkLast(e);
            else
                linkBefore(e, next);
            nextIndex++;
            expectedModCount++;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            while (modCount == expectedModCount && nextIndex < size) {
                action.accept(next.item);
                lastReturned = next;
                next = next.next;
                nextIndex++;
            }
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    // 节点
    private static class Node<E> {
        // 节点的值
        E item;
        // 后指针
        Node<E> next;
        // 前指针
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    // 相反的顺序返回迭代器
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }


    // 逆序迭代器
    private class DescendingIterator implements Iterator<E> {
        // 调用反方法即可
        private final ListItr itr = new ListItr(size());
        public boolean hasNext() {
            return itr.hasPrevious();
        }
        public E next() {
            return itr.previous();
        }
        public void remove() {
            itr.remove();
        }
    }

    // 克隆
    @SuppressWarnings("unchecked")
    private LinkedList<E> superClone() {
        try {
            // LinkedList与原来引用不一致，但里面元素引用一致
            return (LinkedList<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }


    // 克隆，
    public Object clone() {
        // 克隆
        LinkedList<E> clone = superClone();

        // Put clone into "virgin" state
        // 修改状态
        clone.first = clone.last = null;
        clone.size = 0;
        clone.modCount = 0;

        // 逐个添加元素，元素的引用和父类一致
        for (Node<E> x = first; x != null; x = x.next)
            clone.add(x.item);

        return clone;
    }

    // 转换数组
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next)
            // 从头遍历
            result[i++] = x.item;
        return result;
    }


    // 转换泛型数组
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                                a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;

        if (a.length > size)
            a[size] = null;

        return a;
    }

    private static final long serialVersionUID = 876323262645176354L;


    // 序列化
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {

        // 写入默认属性
        s.defaultWriteObject();

        // 写入size
        s.writeInt(size);

        // 逐个写入元素
        for (Node<E> x = first; x != null; x = x.next)
            s.writeObject(x.item);
    }


    // 反序列化
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // 写入默认属性
        s.defaultReadObject();

        // 读取size
        int size = s.readInt();

        // 逐个读取元素并添加回去
        for (int i = 0; i < size; i++)
            linkLast((E)s.readObject());
    }


    // 切分器
    @Override
    public Spliterator<E> spliterator() {
        return new LLSpliterator<E>(this, -1, 0);
    }


    // TODO 使用较少，后续更新
    static final class LLSpliterator<E> implements Spliterator<E> {
        static final int BATCH_UNIT = 1 << 10;  // batch array size increment
        static final int MAX_BATCH = 1 << 25;  // max batch array size;
        final LinkedList<E> list; // null OK unless traversed
        Node<E> current;      // current node; null until initialized
        int est;              // size estimate; -1 until first needed
        int expectedModCount; // initialized when est set
        int batch;            // batch size for splits

        LLSpliterator(LinkedList<E> list, int est, int expectedModCount) {
            this.list = list;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getEst() {
            int s; // force initialization
            final LinkedList<E> lst;
            if ((s = est) < 0) {
                if ((lst = list) == null)
                    s = est = 0;
                else {
                    expectedModCount = lst.modCount;
                    current = lst.first;
                    s = est = lst.size;
                }
            }
            return s;
        }

        public long estimateSize() { return (long) getEst(); }

        public Spliterator<E> trySplit() {
            Node<E> p;
            int s = getEst();
            if (s > 1 && (p = current) != null) {
                int n = batch + BATCH_UNIT;
                if (n > s)
                    n = s;
                if (n > MAX_BATCH)
                    n = MAX_BATCH;
                Object[] a = new Object[n];
                int j = 0;
                do { a[j++] = p.item; } while ((p = p.next) != null && j < n);
                current = p;
                batch = j;
                est = s - j;
                return Spliterators.spliterator(a, 0, j, Spliterator.ORDERED);
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Node<E> p; int n;
            if (action == null) throw new NullPointerException();
            if ((n = getEst()) > 0 && (p = current) != null) {
                current = null;
                est = 0;
                do {
                    E e = p.item;
                    p = p.next;
                    action.accept(e);
                } while (p != null && --n > 0);
            }
            if (list.modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            Node<E> p;
            if (action == null) throw new NullPointerException();
            if (getEst() > 0 && (p = current) != null) {
                --est;
                E e = p.item;
                current = p.next;
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }

}
