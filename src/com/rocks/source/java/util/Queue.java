package java.util;

/**
 * 队列接口
 */
public interface Queue<E> extends Collection<E> {

    // 队尾添加元素，失败时抛出异常
    boolean add(E e);

    // 队尾添加元素，失败时返回false
    boolean offer(E e);

    // 队头取出元素，失败时抛出异常
    E remove();

    // 队头取出元素，失败时返回null
    E poll();

    // 查看队头元素，失败时抛出异常
    E element();

    // 查看队头元素，失败时返回null
    E peek();
}
