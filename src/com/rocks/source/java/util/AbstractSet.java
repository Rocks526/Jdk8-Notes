package java.util;

/**
 * Set接口的抽象实现，继承自AbstractCollection，大部分方法继承了AbstractCollection的实现
 */
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {

    protected AbstractSet() {
    }

    // ======================== 比较和哈希 ===================================

    // 比较方法
    public boolean equals(Object o) {
        // 引用判断
        if (o == this)
            return true;
        // 类型判断
        if (!(o instanceof Set))
            return false;
        // 元素大小判断
        Collection<?> c = (Collection<?>) o;
        if (c.size() != size())
            return false;
        try {
            // 调用containsAll判断，不关注顺序
            return containsAll(c);
        } catch (ClassCastException unused)   {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }
    }

    // 计算哈希值
    public int hashCode() {
        int h = 0;
        // 每个元素的哈希值累计
        Iterator<E> i = iterator();
        while (i.hasNext()) {
            E obj = i.next();
            if (obj != null)
                h += obj.hashCode();
        }
        return h;
    }

    // 移除参数的所有元素
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        // 检查大小，遍历小的集合，contains方法对比，remove移除
        if (size() > c.size()) {
            for (Iterator<?> i = c.iterator(); i.hasNext(); )
                modified |= remove(i.next());
        } else {
            for (Iterator<?> i = iterator(); i.hasNext(); ) {
                if (c.contains(i.next())) {
                    i.remove();
                    modified = true;
                }
            }
        }
        return modified;
    }

}
