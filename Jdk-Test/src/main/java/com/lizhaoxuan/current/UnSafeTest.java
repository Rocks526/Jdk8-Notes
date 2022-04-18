package com.lizhaoxuan.current;

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
            arrayStr.append(unSafe.getInt(array, arrayBaseOffset + (long) indexScale * i)).append("=>");
        }
        System.out.println(arrayStr);
        // 传入错误对象/不传入对象测试，直接获取绝对偏移量的数据 (操作系统中，一个进程是不能访问其他进程的内存的，所以传入 getInt 中的绝对地址必须是当前 JVM 管理的内存地址，否则进程会退出)
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

    public static void main(String[] args) throws Exception {
        // 初始化
        init();
        // 通过内存地址直接操作对象属性
        getObjectFieldValue();
        putObjectFieldValue();
        // 内存申请与释放
        memoryOperator();
        // 类操作
        classOperator();
        // cas
        casOperator();
    }

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

    private static void classOperator() {
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

}
