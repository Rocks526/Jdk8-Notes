package java.util;

import java.util.function.Consumer;

/**
 * 集合上的迭代器，用于遍历集合元素
 */
public interface Iterator<E> {

    /**
     * 是否还有下一个元素
     */
    boolean hasNext();

    /**
     * 返回下一个元素
     */
    E next();

    /**
     * 从集合中移除刚刚返回的元素
     */
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    /**
     * Jdk8引入函数式编程优化迭代器
     *
     *      while(hasNext()){
     *          handler == > next();
     *      }
     *
     *  将迭代器遍历这种编程范式隐藏在此方法内，简化代码
     * @param action
     */
    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
