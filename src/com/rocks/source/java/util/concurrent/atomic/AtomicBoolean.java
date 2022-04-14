package java.util.concurrent.atomic;
import sun.misc.Unsafe;

/**
 *  基于CAS + Volatile 实现的boo类型原子性更新
 *
 *  bool类型不需要增加或减少，只有set操作，加 volatile 即可实现线程安全，为何还要AtomicBoolean?
 *
 *  bool可能需要这样的场景：  if (flag == false) flag = true;
 *
 *  针对这种场景，存在竞态条件，因此需要 AtomicBoolean
 *
 * @since 1.5
 * @author Doug Lea
 */
public class AtomicBoolean implements java.io.Serializable {
    private static final long serialVersionUID = 4654671469794556979L;

    // unsafe工具
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    // value的偏移量
    private static final long valueOffset;

    static {
        try {
            // 获取value的偏移量
            valueOffset = unsafe.objectFieldOffset
                (AtomicBoolean.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    // 实际值，用1和0标识true和false
    private volatile int value;

    /**
     *  初始化
     * @param initialValue the initial value
     */
    public AtomicBoolean(boolean initialValue) {
        value = initialValue ? 1 : 0;
    }

    public AtomicBoolean() {
    }

    /**
     *  返回当前值
     * @return the current value
     */
    public final boolean get() {
        return value != 0;
    }

    /**
     *  CAS操作，当前值为 expect 时则更新为 update
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(boolean expect, boolean update) {
        int e = expect ? 1 : 0;
        int u = update ? 1 : 0;
        return unsafe.compareAndSwapInt(this, valueOffset, e, u);
    }

    /**
     * compareAndSet替代此方法
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful
     */
    public boolean weakCompareAndSet(boolean expect, boolean update) {
        int e = expect ? 1 : 0;
        int u = update ? 1 : 0;
        return unsafe.compareAndSwapInt(this, valueOffset, e, u);
    }

    /**
     * 设置值，set操作本身就是原子性，不需要CAS
     * @param newValue the new value
     */
    public final void set(boolean newValue) {
        value = newValue ? 1 : 0;
    }

    /**
     * 原子性设置值，延迟设置，不会立刻清除其他线程的缓存
     * @param newValue the new value
     * @since 1.6
     */
    public final void lazySet(boolean newValue) {
        int v = newValue ? 1 : 0;
        unsafe.putOrderedInt(this, valueOffset, v);
    }

    /**
     * 原子性设置值并返回旧的值
     * @param newValue the new value
     * @return the previous value
     */
    public final boolean getAndSet(boolean newValue) {
        boolean prev;
        do {
            prev = get();
        } while (!compareAndSet(prev, newValue));
        return prev;
    }

    /**
     * 输出当前值
     * @return the String representation of the current value
     */
    public String toString() {
        return Boolean.toString(get());
    }

}
