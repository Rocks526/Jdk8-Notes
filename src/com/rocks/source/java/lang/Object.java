package java.lang;

/**
 *  Object是类层次结构的根
 *  每个类都有Object作为超类
 *  包括数组，也实现了Object的方法
 */
public class Object {


    // 此方法主要进行一些系统相关的初始化操作。
    private static native void registerNatives();
    static {
        registerNatives();
    }

    // 此方法用于返回运行时对象的Class对象，即创建该对象的类。
    public final native Class<?> getClass();

    // 本地方法，返回一个int类型的hash值，主要用于基于Hash相关的容器，如HashMap，TreeMap，HashSet等。
    public native int hashCode();


    // 用于判断两个对象实例是否相等的方法，默认实现是直接比较引用地址，如果有需要可以在子类进行重写。
    public boolean equals(Object obj) {
        return (this == obj);
    }


    // 对象克隆本地方法，用于快速获得一个对象副本。
    protected native Object clone() throws CloneNotSupportedException;


    // 对象打印方法，默认输出的是对象对应的类的全限定名 + @ + Hash值，其中 @ 后面的数值为Hash码的无符号十六进制表示。
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }


    // 这三个方法是用来配合synchronized实现管程模型的
    public final native void notify();
    public final native void notifyAll();
    public final native void wait(long timeout) throws InterruptedException;
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }
    public final void wait() throws InterruptedException {
        wait(0);
    }

    // Jvm在进行GC时，回收对象之前会调用对象的此方法。
    protected void finalize() throws Throwable { }
}
