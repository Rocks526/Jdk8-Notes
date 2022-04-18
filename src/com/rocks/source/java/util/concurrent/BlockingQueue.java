package java.util.concurrent;

import java.util.Collection;
import java.util.Queue;

/**
 * 阻塞队列接口，对队列接口的扩展，用于实现生产者-消费者模型
 */
public interface BlockingQueue<E> extends Queue<E> {

    // 往队列里添加元素，失败抛出异常
    boolean add(E e);

    // 往队列里添加元素，失败返回false
    boolean offer(E e);

    // 往队列里添加元素，一直等待，可被中断
    void put(E e) throws InterruptedException;

    // 往队列里添加元素，超时后返回false
    boolean offer(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    // 从队列里取出元素，一直等待，可被中断
    E take() throws InterruptedException;

    // 从队列里取出元素，超时后返回null
    E poll(long timeout, TimeUnit unit)
        throws InterruptedException;

    // 返回队列剩余容量
    int remainingCapacity();


    // 移除指定元素，失败返回false
    boolean remove(Object o);

    // 判断队列是否包含某个元素
    public boolean contains(Object o);

    // 将队列所有元素导出到参数集合里，返回导出的数量
    int drainTo(Collection<? super E> c);

    // 将队列所有元素导出到参数集合里，第二个参数指定要导出的数量，结果返回导出的数量
    int drainTo(Collection<? super E> c, int maxElements);
}
