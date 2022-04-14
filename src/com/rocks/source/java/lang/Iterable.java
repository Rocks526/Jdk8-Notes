package java.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * 1.使用此接口，代表支持遍历
 * 2.可以通过iterator获取一个迭代器进行遍历
 * 3.也可以通过for循环遍历
 * 4.Jdk1.8之后，引入了增强forEach遍历，可以传入一个函数表达式进行遍历
 */
public interface Iterable<T> {

    /**
     * 获取一个遍历的迭代器
     */
    Iterator<T> iterator();

    /**
     * Jdk8引入的函数式编程，简化代码，增强for循环
     * @param action
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * 获取一个切分器，Jdk8引入
     */
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }

}
