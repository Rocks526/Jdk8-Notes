package java.util;

/**
 * 用于List的迭代器，支持在双向遍历，并且支持遍历过程中进行更新、删除等
 */
public interface ListIterator<E> extends Iterator<E> {

    // ========================= 查询操作 ===================================

    // 向后遍历，是否还有下一个元素
    boolean hasNext();

    // 向后遍历，返回下一个元素
    E next();

    // 向前遍历，是否还有前一个元素
    boolean hasPrevious();

    // 返回前一个元素
    E previous();

    // 返回下一个元素的下标
    int nextIndex();

    // 返回上一个元素的下标
    int previousIndex();


    // ======================== 更新操作 ========================================

    // 从列表中删除当前游标指向的元素
    void remove();

    // 替换当前游标指向的元素
    void set(E e);

    // 在当前游标后面新增一个元素
    void add(E e);
}
