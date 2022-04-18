package java.util;

/**
 * 双端队列接口
 */
public interface Deque<E> extends Queue<E> {

    // 队头添加元素，失败抛出异常
    void addFirst(E e);

    // 队尾添加元素，失败抛出异常
    void addLast(E e);

    // 队头添加元素，失败返回false
    boolean offerFirst(E e);

    // 队尾添加元素，失败返回false
    boolean offerLast(E e);

    // 队头取出元素，失败抛出异常
    E removeFirst();

    // 队尾取出元素，失败抛出异常
    E removeLast();

    // 队头取出元素，失败返回null
    E pollFirst();

    // 队尾取出元素，失败返回null
    E pollLast();

    // 查看队头元素，失败抛出异常
    E getFirst();

    // 查看队尾元素，失败抛出异常
    E getLast();

    // 查看队头元素，失败返回null
    E peekFirst();

    // 查看队尾元素，失败返回null
    E peekLast();


    // 删除包含指定元素的第一个匹配项
    boolean removeFirstOccurrence(Object o);

    // 删除包含指定元素的最后一个匹配项
    boolean removeLastOccurrence(Object o);

    // =================== 队列方法 ============================================

    boolean add(E e);

    boolean offer(E e);

    E remove();

    E poll();

    E element();

    E peek();


    // ======================== 栈方法 =============================

    // 入栈
    void push(E e);

    // 出栈
    E pop();


    // ======================== 集合方法 ====================================

    boolean remove(Object o);

    boolean contains(Object o);

    public int size();

    // 正向返回迭代器
    Iterator<E> iterator();

    // 相反的顺序返回迭代器
    Iterator<E> descendingIterator();

}
