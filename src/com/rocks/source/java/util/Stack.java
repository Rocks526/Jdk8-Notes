
package java.util;

/**
 * 继承自Vector，拥有Vector一切特性，动态扩容，线程安全，有序可重复等
 * 新增了栈相关的操作
 */
public class Stack<E> extends Vector<E> {

    public Stack() {
    }

    // 入栈
    public E push(E item) {
        addElement(item);
        return item;
    }

    // 出栈
    public synchronized E pop() {
        E       obj;
        int     len = size();
        // 获取栈顶元素
        obj = peek();
        // 移除栈顶元素
        removeElementAt(len - 1);
        return obj;
    }


    // 查看栈顶元素
    public synchronized E peek() {
        int len = size();

        if (len == 0)
            throw new EmptyStackException();
        return elementAt(len - 1);
    }

    // 是否栈空
    public boolean empty() {
        return size() == 0;
    }

    // 搜索某个对象
    public synchronized int search(Object o) {
        // 从栈顶往下搜，即数组反序搜
        int i = lastIndexOf(o);
        // 查找到了
        if (i >= 0) {
            return size() - i;
        }
        return -1;
    }

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = 1224463164541339165L;
}
