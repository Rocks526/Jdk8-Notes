# 一：Atomic包



# 二：UnSafe

### 2.1 UnSafe介绍

Jdk1.5推出的并发包（java.util.concurrent）包含很多优秀的并发工具，这些并发工具底层大量使用了UnSafe类。Unsafe是一个偏向于底层的工具类，用于操作内存地址，主要提供以下功能：

- 绕过 JVM 直接修改内存（对象）
- 使用硬件 CPU 指令实现 CAS 原子操作
- 使用硬件 CPU 指令实现内存屏障

> Unsafe 并不是 JDK 的标准，它是 Sun 的内部实现，存在于 sun.misc 包中，在 Oracle 发行的 JDK 中并不包含其源代码。

虽然我们在一般的并发编程中不会直接用到 Unsafe，但是很多 Java 基础类库与诸如 Netty、Cassandra 和 Kafka 等高性能库都采用它，它在提升 Java 运行效率、增强 Java 语言底层操作能力方面起了很大作用。因此我们还是有必要学习一下这个类，了解其提供的功能。

### 2.2 初始化

Unsafe 为调用者提供执行非安全操作的能力，由于返回的 Unsafe 对象可以读写任意的内存地址数据，调用者应该小心谨慎的使用改对象，一定不用把它传递到非信任代码。在UnSafe的构造方法里，将其设置为私有的，并提供一个静态方法用于获取实例，`在静态方法里对类加载器进行了校验，应用层的加载会抛出异常。`

```java
    // 私有化
    private Unsafe() {
    }
    private static final Unsafe theUnsafe;
    
    // 单例
    @CallerSensitive
    public static Unsafe getUnsafe() {
        Class var0 = Reflection.getCallerClass();
        // 检查是否系统加载，应用不可获取
        if (!VM.isSystemDomainLoader(var0.getClassLoader())) {
            throw new SecurityException("Unsafe");
        } else {
            return theUnsafe;
        }
    }

    static {
        // 注册
        registerNatives();
        Reflection.registerMethodsToFilter(Unsafe.class, new String[]{"getUnsafe"});
        theUnsafe = new Unsafe();
	// ............
    }
```

虽然静态方法加了校验，但我们可以通过反射获取UnSafe实例，如下所示：

```java
    public static void init(){
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

### 2.3 通过内存地址直接操作对象属性

- 通过内存地址直接访问、设置对象属性

```java
    // 获取指定对象中指定偏移量的字段或数组元素的值，如果对象为null，则获取内存中该地址的值
    public native int getInt(Object var1, long var2);
    // 设置指定对象中指定偏移量的字段或数组元素的值
    public native void putInt(Object var1, long var2, int var4);

    public native Object getObject(Object var1, long var2);

    public native void putObject(Object var1, long var2, Object var4);

    public native boolean getBoolean(Object var1, long var2);

    public native void putBoolean(Object var1, long var2, boolean var4);

    public native byte getByte(Object var1, long var2);

    public native void putByte(Object var1, long var2, byte var4);

    public native short getShort(Object var1, long var2);

    public native void putShort(Object var1, long var2, short var4);

    public native char getChar(Object var1, long var2);

    public native void putChar(Object var1, long var2, char var4);

    public native long getLong(Object var1, long var2);

    public native void putLong(Object var1, long var2, long var4);

    public native float getFloat(Object var1, long var2);

    public native void putFloat(Object var1, long var2, float var4);

    public native double getDouble(Object var1, long var2);

    public native void putDouble(Object var1, long var2, double var4);

    /** @deprecated */
    @Deprecated
    public int getInt(Object var1, int var2) {
        return this.getInt(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putInt(Object var1, int var2, int var3) {
        this.putInt(var1, (long)var2, var3);
    }

    /** @deprecated */
    @Deprecated
    public Object getObject(Object var1, int var2) {
        return this.getObject(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putObject(Object var1, int var2, Object var3) {
        this.putObject(var1, (long)var2, var3);
    }

    /** @deprecated */
    @Deprecated
    public boolean getBoolean(Object var1, int var2) {
        return this.getBoolean(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putBoolean(Object var1, int var2, boolean var3) {
        this.putBoolean(var1, (long)var2, var3);
    }

    /** @deprecated */
    @Deprecated
    public byte getByte(Object var1, int var2) {
        return this.getByte(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putByte(Object var1, int var2, byte var3) {
        this.putByte(var1, (long)var2, var3);
    }

    /** @deprecated */
    @Deprecated
    public short getShort(Object var1, int var2) {
        return this.getShort(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putShort(Object var1, int var2, short var3) {
        this.putShort(var1, (long)var2, var3);
    }

    /** @deprecated */
    @Deprecated
    public char getChar(Object var1, int var2) {
        return this.getChar(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putChar(Object var1, int var2, char var3) {
        this.putChar(var1, (long)var2, var3);
    }

    /** @deprecated */
    @Deprecated
    public long getLong(Object var1, int var2) {
        return this.getLong(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putLong(Object var1, int var2, long var3) {
        this.putLong(var1, (long)var2, var3);
    }

    /** @deprecated */
    @Deprecated
    public float getFloat(Object var1, int var2) {
        return this.getFloat(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putFloat(Object var1, int var2, float var3) {
        this.putFloat(var1, (long)var2, var3);
    }

    /** @deprecated */
    @Deprecated
    public double getDouble(Object var1, int var2) {
        return this.getDouble(var1, (long)var2);
    }

    /** @deprecated */
    @Deprecated
    public void putDouble(Object var1, int var2, double var3) {
        this.putDouble(var1, (long)var2, var3);
    }

    public native byte getByte(long var1);

    public native void putByte(long var1, byte var3);

    public native short getShort(long var1);

    public native void putShort(long var1, short var3);

    public native char getChar(long var1);

    public native void putChar(long var1, char var3);

    public native int getInt(long var1);

    public native void putInt(long var1, int var3);

    public native long getLong(long var1);

    public native void putLong(long var1, long var3);

    public native float getFloat(long var1);

    public native void putFloat(long var1, float var3);

    public native double getDouble(long var1);

    public native void putDouble(long var1, double var3);

    public native long getAddress(long var1);

    public native void putAddress(long var1, long var3);

```

上面的方法虽然多，但主要分为两类，一类是get方法，用于根据对象和偏移量获取对应字段的值，另一类是put方法，用于根据对象和偏移量设置对应字段的值，使用时需要注意以下几点：

- offset偏移量通过一系列的FieldOffset方法获取
- 只有一个参数的get方法，是将offset当作绝对地址获取对应的数据
- offset计算错误时，这一系列方法不会抛出异常，但会访问对应的地址，产生不可控结果
- 获取字段时，字段类型也需要和方法类型一致，否则可能产生不可控结果

offset获取方式：

```java
    // 获取字段偏移量，自动判断是否静态属性
    /** @deprecated */
    @Deprecated
    public int fieldOffset(Field var1) {
        return Modifier.isStatic(var1.getModifiers()) ? (int)this.staticFieldOffset(var1) : (int)this.objectFieldOffset(var1);
    }
    // 获取静态属性的基准地址，即该字段所属的类
    /** @deprecated */
    @Deprecated
    public Object staticFieldBase(Class<?> var1) {
        Field[] var2 = var1.getDeclaredFields();

        for(int var3 = 0; var3 < var2.length; ++var3) {
            if (Modifier.isStatic(var2[var3].getModifiers())) {
                return this.staticFieldBase(var2[var3]);
            }
        }
        return null;
    }
    // 获取静态属性的偏移量
    public native long staticFieldOffset(Field var1);
    // 获取对象属性的偏移量
    public native long objectFieldOffset(Field var1);
    // 获取静态属性的基准地址，即该字段所属的类
    public native Object staticFieldBase(Field var1);
    // 获取数组的起始元素地址
    public native int arrayBaseOffset(Class<?> var1);
    // 获取数组中每个元素的字节长度
    public native int arrayIndexScale(Class<?> var1);
```

- 示例代码

```java
package com.rocks.test.lang;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe测试
 * @author lizhaoxuan
 * @date 2022/02/14
 */
public class UnSafeTest {

    private static Unsafe unSafe;

    public static void init(){
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unSafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getObjectFieldValue() throws Exception {
        // 对象属性操作 (获取类里某个指定字段的偏移量，获取实际值时再传入对象，这样可以一个偏移量应用于多个对象)
        Field pCountField = Parent.class.getDeclaredField("pCount");
        long pCountFieldOffset = unSafe.objectFieldOffset(pCountField);
        Parent parent = new Parent();
        int pCount = unSafe.getInt(parent, pCountFieldOffset);
        System.out.println(pCount);
        // 父类对象属性操作 (子类和父类，针对父类字段的内存布局一致，因此偏移量可以复用)
        Children children = new Children();
        int childPCount = unSafe.getInt(children, pCountFieldOffset);
        System.out.println(childPCount);
        // 类属性操作 (类属性依附于类本身)
        Field levelField = Children.class.getDeclaredField("level");
        long levelFieldOffset = unSafe.staticFieldOffset(levelField);
        boolean level = unSafe.getBoolean(Children.class, levelFieldOffset);
        System.out.println(level);
        // 获取静态字段所属对象，即字段所属类，Children的Class对象
        Object o = unSafe.staticFieldBase(levelField);
        System.out.println(o == Children.class);
        boolean aBoolean = unSafe.getBoolean(o, levelFieldOffset);
        System.out.println(aBoolean);
        // 对象数组属性操作
        int arrayBaseOffset = unSafe.arrayBaseOffset(int[].class);
        int indexScale = unSafe.arrayIndexScale(int[].class);
        Field arrayField = children.getClass().getDeclaredField("array");
        long arrayOffset = unSafe.objectFieldOffset(arrayField);
        int[] array = (int[]) unSafe.getObject(children, arrayOffset);
        StringBuilder arrayStr = new StringBuilder();
        for (int i=0;i<4;i++){
            arrayStr.append(unSafe.getInt(array, arrayBaseOffset + (long) indexScale * i) + "=>");
        }
        System.out.println(arrayStr);
        // 传入错误对象/不传入对象测试，直接获取绝对偏移量的数据
//        int anInt = unSafe.getInt(pCountFieldOffset);
//        System.out.println(anInt);
    }

    public static void putObjectFieldValue() throws Exception {
        // 对象属性操作
        Field pCountField = Parent.class.getDeclaredField("pCount");
        long pCountFieldOffset = unSafe.objectFieldOffset(pCountField);
        Parent parent = new Parent();
        int pCount1 = unSafe.getInt(parent, pCountFieldOffset);
        System.out.println(pCount1);
        unSafe.putInt(parent, pCountFieldOffset, 999);
        int pCount2 = unSafe.getInt(parent, pCountFieldOffset);
        System.out.println(pCount2);
        // 类属性操作 (类属性依附于类本身)
        Field levelField = Children.class.getDeclaredField("level");
        long levelFieldOffset = unSafe.staticFieldOffset(levelField);
        boolean level1 = unSafe.getBoolean(Children.class, levelFieldOffset);
        System.out.println(level1);
        unSafe.putBoolean(Children.class, levelFieldOffset, true);
        boolean level2 = unSafe.getBoolean(Children.class, levelFieldOffset);
        System.out.println(level2);
        // 对象数组属性操作
        Children children = new Children();
        int arrayBaseOffset = unSafe.arrayBaseOffset(int[].class);
        int indexScale = unSafe.arrayIndexScale(int[].class);
        Field arrayField = children.getClass().getDeclaredField("array");
        long arrayOffset = unSafe.objectFieldOffset(arrayField);
        int[] array = (int[]) unSafe.getObject(children, arrayOffset);
        StringBuilder arrayStr = new StringBuilder();
        for (int i=0;i<4;i++){
            arrayStr.append(unSafe.getInt(array, arrayBaseOffset + (long) indexScale * i)).append("=>");
        }
        System.out.println(arrayStr);
        StringBuilder arrayStr2 = new StringBuilder();
        for (int i=0;i<4;i++){
            unSafe.putInt(array, arrayBaseOffset + (long) indexScale * i, i*100);
            arrayStr2.append(unSafe.getInt(array, arrayBaseOffset + (long) indexScale * i)).append("=>");
        }
        System.out.println(arrayStr2);
    }

    public static void main(String[] args) throws Exception {
        // 初始化
        init();
        // 通过内存地址直接操作对象属性
        getObjectFieldValue();
        putObjectFieldValue();
    }

    public static class Parent {
        private int pCount = 10;
    }

    public static class Children extends Parent {
        private static boolean level = false;
        private int[] array = {9,22,0,-7};
    }

}
```

![image-20220218113143098](http://rocks526.top/lzx/image-20220218113143098.png)

### 2.4 内存分配与释放

```java
   // 分配内存
    public native long allocateMemory(long var1);
    // 用于扩容或缩容
    public native long reallocateMemory(long var1, long var3);
    public native void setMemory(Object var1, long var2, long var4, byte var6);
    public void setMemory(long var1, long var3, byte var5) {
        this.setMemory((Object)null, var1, var3, var5);
    }
    // 拷贝内存
    public native void copyMemory(Object var1, long var2, Object var4, long var5, long var7);
    public void copyMemory(long var1, long var3, long var5) {
        this.copyMemory((Object)null, var1, (Object)null, var3, var5);
    }
    // 释放内存
    public native void freeMemory(long var1);
```

allocateMemory、reallocateMemory、freeMemory 与 setMemory 分别是对 C 函数 malloc、realloc、free 和 memset 的封装，这样该类就提供了动态获取/释放本地方法区内存的功能：

- malloc：用于分配一个全新的未使用的连续内存，但该内存不会初始化，即不会被清零
- realloc：用于内存的缩容或扩容，有两个参数，从 malloc 返回的地址和要调整的大小，该函数和 malloc 一样，不会初始化，它能保留之前放到内存里的值，很适合用于扩容
- free：用于释放内存，该方法只有一个地址参数
- memset：一般用于初始化内存，可以设置初始化内存的值，一般初始值会设置成 0，即清零操作

```java
    private static void memoryOperator() {
        long address = unSafe.allocateMemory(10);
        unSafe.setMemory(address, 10, (byte) 1);
        /**
         * 1的二进制码为00000001，int为四个字节，U.getInt将读取四个字节，
         * 读取的字节为00000001 00000001 00000001 00000001
         */
        int i = 0b00000001000000010000000100000001;
        System.out.println(i == unSafe.getInt(address));
        unSafe.freeMemory(address);
    }
```

### 2.5 类相关信息

```java
    // 检查给定的类是否需要初始化
    public native boolean shouldBeInitialized(Class<?> var1);
    // 确保给定的类已被初始化，如果没有初始化，会进行初始化
    public native void ensureClassInitialized(Class<?> var1);
```

- shouldBeInitialized：判断给定的类是否需要被初始化，如果初始化过，返回false
- ensureClassInitialized：确保给定的类进行初始化，如果没有初始化过，则进行初始化

```java
 
    private static Unsafe unSafe;

private static void ClassOperator() {
        System.out.println(unSafe.shouldBeInitialized(Parent.class));
        System.out.println(unSafe.shouldBeInitialized(Children.class));
        unSafe.ensureClassInitialized(Children.class);
        System.out.println(unSafe.shouldBeInitialized(Parent.class));
        System.out.println(unSafe.shouldBeInitialized(Children.class));
    }

    public static class Parent {
        private int pCount = 10;

        static {
            System.out.println("Parent init!!!");
        }

    }

    public static class Children extends Parent {
        private static boolean level = false;
        private int[] array = {9,22,0,-7};

        static {
            System.out.println("Children init!!");
        }

    }
```

![image-20220218112941510](http://rocks526.top/lzx/image-20220218112941510.png)

### 2.6 CAS方法

```java
    // 如果变量的值为预期值，则更新变量的值，该操作为原子操作
    public final native boolean compareAndSwapObject(Object var1, long var2, Object var4, Object var5);
    public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
    public final native boolean compareAndSwapLong(Object var1, long var2, long var4, long var6);
```

- 针对CPU的CAS操作的封装，只有当前值等于期待值时进行更新
- 四个参数分为为：要修改的对象，要修改的字段的偏移量，期待的值，更新后的值

```java
    private static void casOperator() throws NoSuchFieldException {
        Field field = Parent.class.getDeclaredField("pCount");
        long offset = unSafe.objectFieldOffset(field);
        Parent parent = new Parent();
        System.out.println(parent.pCount);
        boolean updateFlag1 = unSafe.compareAndSwapInt(parent, offset, 0, 1);
        System.out.println(updateFlag1);
        boolean updateFlag2 = unSafe.compareAndSwapInt(parent, offset, 10, 1);
        System.out.println(updateFlag2);
    }
```

![image-20220218114549989](http://rocks526.top/lzx/image-20220218114549989.png)

### 2.7 源码

```java
package sun.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public final class Unsafe {
    private static final Unsafe theUnsafe;
    public static final int INVALID_FIELD_OFFSET = -1;
    public static final int ARRAY_BOOLEAN_BASE_OFFSET;
    public static final int ARRAY_BYTE_BASE_OFFSET;
    public static final int ARRAY_SHORT_BASE_OFFSET;
    public static final int ARRAY_CHAR_BASE_OFFSET;
    public static final int ARRAY_INT_BASE_OFFSET;
    public static final int ARRAY_LONG_BASE_OFFSET;
    public static final int ARRAY_FLOAT_BASE_OFFSET;
    public static final int ARRAY_DOUBLE_BASE_OFFSET;
    public static final int ARRAY_OBJECT_BASE_OFFSET;
    public static final int ARRAY_BOOLEAN_INDEX_SCALE;
    public static final int ARRAY_BYTE_INDEX_SCALE;
    public static final int ARRAY_SHORT_INDEX_SCALE;
    public static final int ARRAY_CHAR_INDEX_SCALE;
    public static final int ARRAY_INT_INDEX_SCALE;
    public static final int ARRAY_LONG_INDEX_SCALE;
    public static final int ARRAY_FLOAT_INDEX_SCALE;
    public static final int ARRAY_DOUBLE_INDEX_SCALE;
    public static final int ARRAY_OBJECT_INDEX_SCALE;
    public static final int ADDRESS_SIZE;

    // 本地方法
    private static native void registerNatives();

    
    static {
        // 注册
        registerNatives();
        Reflection.registerMethodsToFilter(Unsafe.class, new String[]{"getUnsafe"});
        theUnsafe = new Unsafe();
        ARRAY_BOOLEAN_BASE_OFFSET = theUnsafe.arrayBaseOffset(boolean[].class);
        ARRAY_BYTE_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
        ARRAY_SHORT_BASE_OFFSET = theUnsafe.arrayBaseOffset(short[].class);
        ARRAY_CHAR_BASE_OFFSET = theUnsafe.arrayBaseOffset(char[].class);
        ARRAY_INT_BASE_OFFSET = theUnsafe.arrayBaseOffset(int[].class);
        ARRAY_LONG_BASE_OFFSET = theUnsafe.arrayBaseOffset(long[].class);
        ARRAY_FLOAT_BASE_OFFSET = theUnsafe.arrayBaseOffset(float[].class);
        ARRAY_DOUBLE_BASE_OFFSET = theUnsafe.arrayBaseOffset(double[].class);
        ARRAY_OBJECT_BASE_OFFSET = theUnsafe.arrayBaseOffset(Object[].class);
        ARRAY_BOOLEAN_INDEX_SCALE = theUnsafe.arrayIndexScale(boolean[].class);
        ARRAY_BYTE_INDEX_SCALE = theUnsafe.arrayIndexScale(byte[].class);
        ARRAY_SHORT_INDEX_SCALE = theUnsafe.arrayIndexScale(short[].class);
        ARRAY_CHAR_INDEX_SCALE = theUnsafe.arrayIndexScale(char[].class);
        ARRAY_INT_INDEX_SCALE = theUnsafe.arrayIndexScale(int[].class);
        ARRAY_LONG_INDEX_SCALE = theUnsafe.arrayIndexScale(long[].class);
        ARRAY_FLOAT_INDEX_SCALE = theUnsafe.arrayIndexScale(float[].class);
        ARRAY_DOUBLE_INDEX_SCALE = theUnsafe.arrayIndexScale(double[].class);
        ARRAY_OBJECT_INDEX_SCALE = theUnsafe.arrayIndexScale(Object[].class);
        ADDRESS_SIZE = theUnsafe.addressSize();
    }
    
    // 私有化
    private Unsafe() {
    }

    
    // 单例
    @CallerSensitive
    public static Unsafe getUnsafe() {
        Class var0 = Reflection.getCallerClass();
        // 检查是否系统加载，应用不可获取
        if (!VM.isSystemDomainLoader(var0.getClassLoader())) {
            throw new SecurityException("Unsafe");
        } else {
            return theUnsafe;
        }
    }

    // 获取指定对象中指定偏移量的字段或数组元素的值，如果对象为null，则获取内存中该地址的值
    public native int getInt(Object var1, long var2);
    // 设置指定对象中指定偏移量的字段或数组元素的值
    public native void putInt(Object var1, long var2, int var4);
    public native Object getObject(Object var1, long var2);
    public native void putObject(Object var1, long var2, Object var4);
    public native boolean getBoolean(Object var1, long var2);
    public native void putBoolean(Object var1, long var2, boolean var4);
    public native byte getByte(Object var1, long var2);
    public native void putByte(Object var1, long var2, byte var4);
    public native short getShort(Object var1, long var2);
    public native void putShort(Object var1, long var2, short var4);
    public native char getChar(Object var1, long var2);
    public native void putChar(Object var1, long var2, char var4);
    public native long getLong(Object var1, long var2);
    public native void putLong(Object var1, long var2, long var4);
    public native float getFloat(Object var1, long var2);
    public native void putFloat(Object var1, long var2, float var4);
    public native double getDouble(Object var1, long var2);
    public native void putDouble(Object var1, long var2, double var4);
    /** @deprecated */
    @Deprecated
    public int getInt(Object var1, int var2) {
        return this.getInt(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putInt(Object var1, int var2, int var3) {
        this.putInt(var1, (long)var2, var3);
    }
    /** @deprecated */
    @Deprecated
    public Object getObject(Object var1, int var2) {
        return this.getObject(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putObject(Object var1, int var2, Object var3) {
        this.putObject(var1, (long)var2, var3);
    }
    /** @deprecated */
    @Deprecated
    public boolean getBoolean(Object var1, int var2) {
        return this.getBoolean(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putBoolean(Object var1, int var2, boolean var3) {
        this.putBoolean(var1, (long)var2, var3);
    }
    /** @deprecated */
    @Deprecated
    public byte getByte(Object var1, int var2) {
        return this.getByte(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putByte(Object var1, int var2, byte var3) {
        this.putByte(var1, (long)var2, var3);
    }
    /** @deprecated */
    @Deprecated
    public short getShort(Object var1, int var2) {
        return this.getShort(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putShort(Object var1, int var2, short var3) {
        this.putShort(var1, (long)var2, var3);
    }
    /** @deprecated */
    @Deprecated
    public char getChar(Object var1, int var2) {
        return this.getChar(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putChar(Object var1, int var2, char var3) {
        this.putChar(var1, (long)var2, var3);
    }
    /** @deprecated */
    @Deprecated
    public long getLong(Object var1, int var2) {
        return this.getLong(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putLong(Object var1, int var2, long var3) {
        this.putLong(var1, (long)var2, var3);
    }
    /** @deprecated */
    @Deprecated
    public float getFloat(Object var1, int var2) {
        return this.getFloat(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putFloat(Object var1, int var2, float var3) {
        this.putFloat(var1, (long)var2, var3);
    }
    /** @deprecated */
    @Deprecated
    public double getDouble(Object var1, int var2) {
        return this.getDouble(var1, (long)var2);
    }
    /** @deprecated */
    @Deprecated
    public void putDouble(Object var1, int var2, double var3) {
        this.putDouble(var1, (long)var2, var3);
    }
    public native byte getByte(long var1);
    public native void putByte(long var1, byte var3);
    public native short getShort(long var1);
    public native void putShort(long var1, short var3);
    public native char getChar(long var1);
    public native void putChar(long var1, char var3);
    public native int getInt(long var1);
    public native void putInt(long var1, int var3);
    public native long getLong(long var1);
    public native void putLong(long var1, long var3);
    public native float getFloat(long var1);
    public native void putFloat(long var1, float var3);
    public native double getDouble(long var1);
    public native void putDouble(long var1, double var3);
    public native long getAddress(long var1);
    public native void putAddress(long var1, long var3);
    
    // 分配内存
    public native long allocateMemory(long var1);
    // 用于扩容或缩容
    public native long reallocateMemory(long var1, long var3);
    public native void setMemory(Object var1, long var2, long var4, byte var6);
    public void setMemory(long var1, long var3, byte var5) {
        this.setMemory((Object)null, var1, var3, var5);
    }
    // 拷贝内存
    public native void copyMemory(Object var1, long var2, Object var4, long var5, long var7);
    public void copyMemory(long var1, long var3, long var5) {
        this.copyMemory((Object)null, var1, (Object)null, var3, var5);
    }
    // 释放内存
    public native void freeMemory(long var1);

    // 获取字段偏移量
    /** @deprecated */
    @Deprecated
    public int fieldOffset(Field var1) {
        return Modifier.isStatic(var1.getModifiers()) ? (int)this.staticFieldOffset(var1) : (int)this.objectFieldOffset(var1);
    }
    /** @deprecated */
    @Deprecated
    public Object staticFieldBase(Class<?> var1) {
        Field[] var2 = var1.getDeclaredFields();

        for(int var3 = 0; var3 < var2.length; ++var3) {
            if (Modifier.isStatic(var2[var3].getModifiers())) {
                return this.staticFieldBase(var2[var3]);
            }
        }

        return null;
    }
    public native long staticFieldOffset(Field var1);
    public native long objectFieldOffset(Field var1);
    public native Object staticFieldBase(Field var1);
    public native int arrayBaseOffset(Class<?> var1);
    public native int arrayIndexScale(Class<?> var1);

    // 检查给定的类是否需要初始化
    public native boolean shouldBeInitialized(Class<?> var1);
    // 确保给定的类已被初始化，如果没有初始化，会进行初始化
    public native void ensureClassInitialized(Class<?> var1);

    // 获取本地指针所占用的字节大小，值为 4 或者 8。32 位虚拟机返回 4，64 位虚拟机默认返回 8，开启指针压缩功能（-XX:-UseCompressedOops）则返回 4
    public native int addressSize();
    // 本地内存页大小，值为 2 的 N 次方
    public native int pageSize();

    // 告诉虚拟机定义一个类，加载类不做安全检查，默认情况下，参数类加载器(ClassLoader)和保护域(ProtectionDomain)来自调用者类
    public native Class<?> defineClass(String var1, byte[] var2, int var3, int var4, ClassLoader var5, ProtectionDomain var6);

    // 定义一个匿名类，这里说的和我们代码里写的匿名内部类不是一个东西 	https://www.zhihu.com/question/51132462
    public native Class<?> defineAnonymousClass(Class<?> var1, byte[] var2, Object[] var3);

    // 分配实例的内存空间，但不会执行构造函数。如果没有执行初始化，则会执行初始化
    public native Object allocateInstance(Class<?> var1) throws InstantiationException;

    // 获取对象内置锁(即 synchronized 关键字获取的锁)，必须通过 monitorExit 方法释放锁
    // (synchronized 代码块在编译后会产生两个指令:monitorenter,monitorexit)
    /** @deprecated */
    @Deprecated
    public native void monitorEnter(Object var1);

    // 释放锁
    /** @deprecated */
    @Deprecated
    public native void monitorExit(Object var1);

    // 尝试获取对象内置锁，通过返回 true 和 false 表示是否成功获取锁
    /** @deprecated */
    @Deprecated
    public native boolean tryMonitorEnter(Object var1);

    // 不通知验证器(verifier)直接抛出异常(此处 verifier 具体含义未知，没有找到相关资料)
    public native void throwException(Throwable var1);

    // 如果变量的值为预期值，则更新变量的值，该操作为原子操作
    public final native boolean compareAndSwapObject(Object var1, long var2, Object var4, Object var5);
    public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
    public final native boolean compareAndSwapLong(Object var1, long var2, long var4, long var6);

    // 获取给定变量的引用值，该操作有 volatile 加载语意，其他方面和 getObject(Object, long) 一样
    public native Object getObjectVolatile(Object var1, long var2);
    // 将引用值写入给定的变量，该操作有 volatile 加载语意，会立即清除其他线程CPU缓存，其他方面和 putObject(Object, long, Object) 一样
    public native void putObjectVolatile(Object var1, long var2, Object var4);
    public native int getIntVolatile(Object var1, long var2);
    public native void putIntVolatile(Object var1, long var2, int var4);
    public native boolean getBooleanVolatile(Object var1, long var2);
    public native void putBooleanVolatile(Object var1, long var2, boolean var4);
    public native byte getByteVolatile(Object var1, long var2);
    public native void putByteVolatile(Object var1, long var2, byte var4);
    public native short getShortVolatile(Object var1, long var2);
    public native void putShortVolatile(Object var1, long var2, short var4);
    public native char getCharVolatile(Object var1, long var2);
    public native void putCharVolatile(Object var1, long var2, char var4);
    public native long getLongVolatile(Object var1, long var2);
    public native void putLongVolatile(Object var1, long var2, long var4);
    public native float getFloatVolatile(Object var1, long var2);
    public native void putFloatVolatile(Object var1, long var2, float var4);
    public native double getDoubleVolatile(Object var1, long var2);
    public native void putDoubleVolatile(Object var1, long var2, double var4);
    // putObjectVolatile(Object, long, Object)的另一个版本(有序的/延迟的)，它不保证其他线程能立即看到修改，但如果其他线程调用getXXXVolatile会看到最新值
    public native void putOrderedObject(Object var1, long var2, Object var4);
    public native void putOrderedInt(Object var1, long var2, int var4);
    public native void putOrderedLong(Object var1, long var2, long var4);

    // 释放当前阻塞的线程。如果当前线程没有阻塞，则下一次调用 park 不会阻塞。这个操作是"非安全"的
    // 是因为调用者必须通过某种方式保证该线程没有被销毁
    public native void unpark(Object var1);

    /**
    *  阻塞当前线程，当发生如下情况时返回：
    *  1、调用 unpark 方法
    *  2、线程被中断
    *  3、时间过期
    *  4、spuriously
    **/
    public native void park(boolean var1, long var2);

    // 获取一段时间内，运行的任务队列分配到可用处理器的平均数(平常说的 CPU 使用率)
    public native int getLoadAverage(double[] var1, int var2);

    // 下面的方法包含基于 CAS 的 Java 实现，用于不支持本地指令的平台
    // 在给定的字段或数组元素的当前值原子性的增加给定的值
    public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

        return var5;
    }
    public final long getAndAddLong(Object var1, long var2, long var4) {
        long var6;
        do {
            var6 = this.getLongVolatile(var1, var2);
        } while(!this.compareAndSwapLong(var1, var2, var6, var6 + var4));

        return var6;
    }
    public final int getAndSetInt(Object var1, long var2, int var4) {
        int var5;
        do {
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var4));

        return var5;
    }
    public final long getAndSetLong(Object var1, long var2, long var4) {
        long var6;
        do {
            var6 = this.getLongVolatile(var1, var2);
        } while(!this.compareAndSwapLong(var1, var2, var6, var4));

        return var6;
    }
    public final Object getAndSetObject(Object var1, long var2, Object var4) {
        Object var5;
        do {
            var5 = this.getObjectVolatile(var1, var2);
        } while(!this.compareAndSwapObject(var1, var2, var5, var4));

        return var5;
    }
    
    // 确保该栏杆前的读操作不会和栏杆后的读写操作发生重排序
    public native void loadFence();
    // 确保该栏杆前的写操作不会和栏杆后的读写操作发生重排序
    public native void storeFence();
    // 确保该栏杆前的读写操作不会和栏杆后的读写操作发生重排序
    public native void fullFence();

    // 抛出非法访问错误，仅用于VM内部
    private static void throwIllegalAccessError() {
        throw new IllegalAccessError();
    }

}
```





