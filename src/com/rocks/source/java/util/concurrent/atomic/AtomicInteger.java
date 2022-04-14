package java.util.concurrent.atomic;
import java.util.function.IntUnaryOperator;
import java.util.function.IntBinaryOperator;
import sun.misc.Unsafe;

/**
 *
 *  AtomicInteger是一个原子性的int更新类，循环CAS + volatile 实现多写多读的线程安全
 * @since 1.5
 * @author Doug Lea
*/
public class AtomicInteger extends Number implements java.io.Serializable {
    private static final long serialVersionUID = 6214790243416807050L;

    // 通过Unsafe的CAS操作实现原子性更新
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    // AtomicInteger对象的value值在内存中的地址偏移量
    private static final long valueOffset;

    static {
        try {
            // 获取内存地址偏移量
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    // 值
    private volatile int value;

    /**
     *  创建一个带初始值的AtomicInteger
     * @param initialValue the initial value
     */
    public AtomicInteger(int initialValue) {
        value = initialValue;
    }

    /**
     * 创建一个AtomicInteger，初始值为0
     */
    public AtomicInteger() {
    }

    /**
     * 获取当前值
     * @return the current value
     */
    public final int get() {
        return value;
    }

    /**
     * 设置值，set操作本身就是原子性，不需要CAS
     * @param newValue the new value
     */
    public final void set(int newValue) {
        value = newValue;
    }

    /**
     *  原子性设置值，延迟设置，不会立刻清除其他线程的缓存
     * @param newValue the new value
     * @since 1.6
     */
    public final void lazySet(int newValue) {
        unsafe.putOrderedInt(this, valueOffset, newValue);
    }

    /**
     *  原子性设置值并返回旧的值
     * @param newValue the new value
     * @return the previous value
     */
    public final int getAndSet(int newValue) {
        return unsafe.getAndSetInt(this, valueOffset, newValue);
    }

    /**
     *  CAS操作，当前值为 expect 时则更新为 update
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    /**
     *  compareAndSet替代此方法
     *
     * @param expect the expected value
     * @param update the new value
     * @return {@code true} if successful
     */
    public final boolean weakCompareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    /**
     *  原子性获取并自增
     * @return the previous value
     */
    public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, valueOffset, 1);
    }

    /**
     *  原子性获取并自减
     * @return the previous value
     */
    public final int getAndDecrement() {
        return unsafe.getAndAddInt(this, valueOffset, -1);
    }

    /**
     *  原子性获取并自增指定值
     * @param delta the value to add
     * @return the previous value
     */
    public final int getAndAdd(int delta) {
        return unsafe.getAndAddInt(this, valueOffset, delta);
    }

    /**
     *  原子性自增并返回自增后的值
     * @return the updated value
     */
    public final int incrementAndGet() {
        return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
    }

    /**
     *  原子性自减并返回自减后的值
     * @return the updated value
     */
    public final int decrementAndGet() {
        return unsafe.getAndAddInt(this, valueOffset, -1) - 1;
    }

    /**
     *  原子性自增指定值并返回自增后的值
     * @param delta the value to add
     * @return the updated value
     */
    public final int addAndGet(int delta) {
        return unsafe.getAndAddInt(this, valueOffset, delta) + delta;
    }

    /**
     *
     * 原子性更新并返回旧的值，传入一个更新函数，根据旧的值进行更新（函数需要支持幂等，可能多次重试）
     *
     * @param updateFunction a side-effect-free function
     * @return the previous value
     * @since 1.8
     */
    public final int getAndUpdate(IntUnaryOperator updateFunction) {
        int prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsInt(prev);
            // 循环CAS
        } while (!compareAndSet(prev, next));
        return prev;
    }

    /**
     *
     * 原子性更新并返回更新后的值，传入一个更新函数，根据旧的值进行更新（函数需要支持幂等，可能多次重试）
     *
     * @param updateFunction a side-effect-free function
     * @return the updated value
     * @since 1.8
     */
    public final int updateAndGet(IntUnaryOperator updateFunction) {
        int prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsInt(prev);
            // 循环CAS
        } while (!compareAndSet(prev, next));
        return next;
    }

    /**
     *
     * 原子性更新并返回旧的值，传入一个更新函数，根据旧的值和另一个传入参数进行更新（函数需要支持幂等，可能多次重试）
     *
     * @param x the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the previous value
     * @since 1.8
     */
    public final int getAndAccumulate(int x,
                                      IntBinaryOperator accumulatorFunction) {
        int prev, next;
        do {
            prev = get();
            next = accumulatorFunction.applyAsInt(prev, x);
            // 循环CAS
        } while (!compareAndSet(prev, next));
        return prev;
    }

    /**
     *
     * 原子性更新并返回更新后的值，传入一个更新函数，根据旧的值和另一个传入参数进行更新（函数需要支持幂等，可能多次重试）
     *
     * @param x the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the updated value
     * @since 1.8
     */
    public final int accumulateAndGet(int x,
                                      IntBinaryOperator accumulatorFunction) {
        int prev, next;
        do {
            prev = get();
            next = accumulatorFunction.applyAsInt(prev, x);
            // 循环CAS
        } while (!compareAndSet(prev, next));
        return next;
    }

    /**
     * 输出当前值
     * @return the String representation of the current value
     */
    public String toString() {
        return Integer.toString(get());
    }

    /**
     * 返回当前值的int值
     */
    public int intValue() {
        return get();
    }

    /**
     * 返回当前值的long值
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public long longValue() {
        return (long)get();
    }

    /**
     * 返回当前值的float值
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public float floatValue() {
        return (float)get();
    }

    /**
     * 返回当前值的double值
     * @jls 5.1.2 Widening Primitive Conversions
     */
    public double doubleValue() {
        return (double)get();
    }

}
