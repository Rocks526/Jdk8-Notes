# 一：Object类介绍

### 1.1 Object介绍

Object在Java中是独特的存在，它的意义是"万物之始"，它是所有类的父类。

即Object中的方法在所有类中都拥有，可以对其进行重写或者默认实现。

### 1.2 Object类结构

![image-20200318173141809](http://rocks526.top/lzx/image-20200318173141809.png)

- Object类图如上所示，Object没有继承任何接口或者类，是Java所有类的父类。

类的注释表明：

- Object是类层次结构的根
- 每个类都有Object作为超类
- 包括数组，也实现了Object的方法

# 二：Object类属性

Object中没有定义任何属性。

# 三：Object类方法

### 3.1 方法列表

![image-20200318173855123](http://rocks526.top/lzx/image-20200318173855123.png)

### 3.2 registerNatives

```Java
    private static native void registerNatives();
    static {
        registerNatives();
    }
```

被static修饰的本地方法，并且Object在静态代码块中调用了该方法，意味着Java所有类在加载时都会调用该方法。

此方法主要进行一些系统相关的初始化操作。

> Java中的本地方法一般都是一些由C++编写的系统调用相关的代码，可能出于性能或者无法实现跨平台性所以采用C++编写。

### 3.3 getClass

```java
    public final native Class<?> getClass();
```

被final修饰的本地方法，表明该方法不可被重写，只能由对象调用。

此方法用于返回运行时对象的Class对象，即创建该对象的类。

### 3.4 hashCode

```java
    public native int hashCode();
```

本地方法，返回一个int类型的hash值，主要用于基于Hash相关的容器，如HashMap，TreeMap，HashSet等。

理想情况下要求不同实例返回的int值是不同的，相同实例返回相同的int值，但存在Hash冲突，可能不同实例返回相同int值。

### 3.5 equals

```java
    public boolean equals(Object obj) {
        return (this == obj);
    }
```

用于判断两个对象实例是否相等的方法，默认实现是直接比较引用地址，如果有需要可以在子类进行重写。

当自定义类需要用到Hash系列容器中，需要重写equals和hashCode方法，应该满足如下关系：

- 如果调用equals方法得到的结果为true，则两个对象的hashcode值必定相等；
- 如果equals方法得到的结果为false，则两个对象的hashcode值可能相等，可能不等；
- 如果两个对象的hashcode值不等，则equals方法得到的结果必定为false；
- 如果两个对象的hashcode值相等，则equals方法得到的结果可能相等，肯定不等。

> 之所以要满足如上关系，是因为在Hash系列容器中，查找元素时是首先利用Hash值去进行查找，Hash相等再利用equals方法判断。通过这种方式提高效率。

### 3.6 clone

```java
    protected native Object clone() throws CloneNotSupportedException;
```

对象克隆本地方法，用于快速获得一个对象副本。

- 克隆对象所在的类必须实现Cloneable接口，否则会抛出CloneNotSupportedException异常，该接口无实际意义，是一种规范，类似于序列化的Serializable接口。
- 该方法的默认实现是基于浅克隆的，当克隆一个对象时：
  - 创建一个新的对象，和原对象引用不一样
  - 对于新对象的所有引用，默认都持有原引用，使用同一个对象，当新对象直接将属性重新赋值时，会对引用进行修改，原对象不会变，如果调用原引用的方法改变引用对象状态，则会将对象一块改变
  - 默认的clone方法为了解决内存，是不会将引用属性的对象也拷贝一份，只是拷贝引用

示例代码如下：

```java
package com.rocks.test.lang;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 对象克隆测试
 */
public class CloneTest implements Cloneable  {

    private Integer num = 526;

    private int num2 = 10;

    private int[] arr = new int[]{0,1,2};

    private ArrayList<String> arrs = new ArrayList<>();

    public CloneTest(){
        arrs.add("demo1");
        arrs.add("demo2");
    }

    public ArrayList<String> getArrs() {
        return arrs;
    }

    public void setArrs(ArrayList<String> arrs) {
        this.arrs = arrs;
    }

    @Override
    public String toString() {
        return "CloneTest{" +
                "num=" + num +
                ", num2=" + num2 +
                ", arr=" + Arrays.toString(arr) +
                '}';
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }


    public static void main(String[] args) throws CloneNotSupportedException {

        CloneTest obj = new CloneTest();
        CloneTest obj2 = (CloneTest)obj.clone();

        System.out.println("------  对象测试  -----------");
        System.out.println(obj == obj2);

        System.out.println("------  包装类型属性测试  --------");
        System.out.println(obj.num == obj2.num);
        Integer integer = new Integer(526);
        System.out.println(obj.num == integer);
        obj2.setNum(78);
        System.out.println(obj2.getNum());
        System.out.println(obj.getNum());
        System.out.println(obj.num == obj2.num);

        System.out.println("------  包装类型属性测设  --------");
        System.out.println(obj.num2 == obj2.num2);
        obj2.setNum2(8);
        System.out.println(obj2.num2);
        System.out.println(obj.num2);
        System.out.println(obj.num2 == obj2.num2);

        System.out.println("-------  数组测试   -----------");
        System.out.println(obj.arr == obj2.arr);
        obj2.arr[0] = 9;
        System.out.println(obj2.arr == obj.arr);
        System.out.println(obj.arr[0]);
        System.out.println(obj2.arr[0]);

        System.out.println("-------  引用类型测试  -------------");
        ArrayList<String> arr1 = obj.getArrs();
        ArrayList<String> arr2 = obj2.getArrs();
        System.out.println(arr1);
        System.out.println(arr2);
        System.out.println(obj.getArrs() == obj2.getArrs());
        arr2.add("demo3");
        System.out.println(arr1);
        System.out.println(arr2);
        System.out.println(obj.getArrs() == obj2.getArrs());

    }


}
================  测试结果如下:  ========================
------  对象测试  -----------
false
------  包装类型属性测试  --------
true
false
78
526
false
------  包装类型属性测设  --------
true
8
10
false
-------  数组测试   -----------
true
true
9
9
-------  引用类型测试  -------------
[demo1, demo2]
[demo1, demo2]
true
[demo1, demo2, demo3]
[demo1, demo2, demo3]
true
```

### 3.7 toString

```java
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
```

对象打印方法，默认输出的是对象对应的类的全限定名 + @ + Hash值，其中 @ 后面的数值为Hash码的无符号十六进制表示。

此方法用于打印对象状态，实现信息可视化，一般需要重写用来输出属性等有用信息。

### 3.8 notify & wait & notifyAll

```java
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
```

这三个方法是用来配合synchronized实现线程间协同工作的。

- wait：让当前线程放弃持有的synchronized锁，进入当前对象的等待池，可以指定等待时间后自动唤醒，如果不指定，则会一直等待下去，直到其他线程调用该对象的notify方法唤醒该线程或者其他线程调用该线程的Interrupte中断，抛出InterruptedException异常从而推出阻塞。
- notify：随机唤醒当前对象的等待池中的一个线程
- notifyAll：可以唤醒当前对象等待池中的所有线程

> 在Java中，每个对象都有一个唯一与之对应的监视器（Monitor）。在Monitor监视器中，Java虚拟机会为每个对象维护两个“队列”：一个叫Entry Set（锁池），另外一个叫Wait Set（等待池）。synchronized锁即是通过这两个队列完成同步操作。更多内容参考Java并发编程synchronized节。

### 3.9 finalize

```java
    protected void finalize() throws Throwable { }
```

Jvm在进行GC时，回收对象之前会调用对象的此方法。

可以通过重写这个方法，在方法中让该对象重新引用，这样可以让该对象逃生，不被回收。需要注意的是，该方法只会被调用一次，即让对象逃生后，下次GC不会调用该方法，会直接回收对象。

可以进行一些资源的关闭的操作，但是比起try-finally，并无优势，因此此方法很少会被重写使用。