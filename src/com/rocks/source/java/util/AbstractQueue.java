package java.util;


/**
 * 队列的抽象实现，一些基础操作由实现类实现，抽象类基于基础操作实现了一些复合操作的逻辑
 */
public abstract class AbstractQueue<E>
    extends AbstractCollection<E>
    implements Queue<E> {

    protected AbstractQueue() {
    }

    // 调用offer添加，offer子类实现
    public boolean add(E e) {
        if (offer(e))
            return true;
        else
            throw new IllegalStateException("Queue full");
    }

    // 调用poll删除，poll子类实现
    public E remove() {
        E x = poll();
        if (x != null)
            return x;
        else
            throw new NoSuchElementException();
    }


    // 调用peek实现，peek子类实现
    public E element() {
        E x = peek();
        if (x != null)
            return x;
        else
            throw new NoSuchElementException();
    }

    // 一直循环弹出
    public void clear() {
        while (poll() != null)
            ;
    }

    // 循环add
    public boolean addAll(Collection<? extends E> c) {
        if (c == null)
            throw new NullPointerException();
        if (c == this)
            throw new IllegalArgumentException();
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }

}
