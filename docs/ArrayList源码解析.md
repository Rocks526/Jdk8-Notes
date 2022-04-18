# 一：ArrayList概述

### 1.1 ArrayList介绍

ArrayList底层是`基于数组的封`装，`支持自动扩容`，`下标检查`等特性，是开发中最常用的集合。

### 1.2 常用API

```java
package com.lizhaoxuan.util;

import com.lizhaoxuan.vo.User;

import java.io.*;
import java.util.*;

/**
 * ArrayList测试
 * @author lizhaoxuan
 */
public class ArrayListTest {

    public static void main(String[] args) throws Exception {
        // 新增测试
        addTest();
        // 删除测试
        deleteTest();
        // 查询测试
        findTest();
        // 迭代遍历测试
        iteratorTest();
        // 序列化测试
        serialTest();
        // 克隆测试
        cloneTest();
        // 切分数组
        subListTest();
    }

    private static void subListTest() {
        System.out.println("=================== [subListTest] =================================");
        ArrayList<Long> list = new ArrayList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
        List<Long> subList = list.subList(1, 3); // [8L, 0L]
        System.out.println(subList);
        // 修改测试
        boolean add = subList.add(22L);
        System.out.println(add);        // true
        boolean remove = subList.remove(22L);       // true
        System.out.println(remove);
        // 获取
        int size = subList.size();      // 2
        System.out.println(size);
        Long aLong = subList.get(0);        // 8L
        System.out.println(aLong);
        // 再次切分
        List<Long> longs = subList.subList(0, 1);       // 8L
        System.out.println(longs);
    }

    private static void cloneTest() {
        System.out.println("=================== [cloneTest] =================================");
        // 基础类型测试
        ArrayList<Long> list = new ArrayList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
        Object clone = list.clone();
        System.out.println(clone);      // [3, 8, 0, 8, 33, 7, 8]
        System.out.println(clone == list);  // false
        if (clone instanceof ArrayList){
            ArrayList<Long> cloneObj = (ArrayList<Long>) clone;
            cloneObj.removeAll(Collections.singletonList(8L));
            System.out.println(cloneObj);       // [3, 0, 33, 7]
            System.out.println(list);   // [3, 8, 0, 8, 33, 7, 8]
        }
        // 引用类型测试
        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder().id(1L).name("张三").build());
        users.add(User.builder().id(3L).name("张三3").build());
        users.add(User.builder().id(2L).name("张三2").build());
        users.add(User.builder().id(4L).name("张三4").build());
        users.add(User.builder().id(9L).name("张三9").build());
        Object userClone = users.clone();
        System.out.println(userClone);      // [User(id=1, name=张三), User(id=3, name=张三3), User(id=2, name=张三2), User(id=4, name=张三4), User(id=9, name=张三9)]
        System.out.println(userClone == users);  // false
        if (userClone instanceof ArrayList){
            ArrayList<User> userCloneObj = (ArrayList<User>) userClone;
            userCloneObj.removeAll(Collections.singletonList(User.builder().id(1L).name("张三").build()));
            System.out.println(userCloneObj);       // [User(id=3, name=张三3), User(id=2, name=张三2), User(id=4, name=张三4), User(id=9, name=张三9)]
            System.out.println(users);   // [User(id=1, name=张三), User(id=3, name=张三3), User(id=2, name=张三2), User(id=4, name=张三4), User(id=9, name=张三9)]
            userCloneObj.forEach(u -> u.setName(u.getName()+"Modify!!!"));
            System.out.println(userCloneObj);       // [User(id=3, name=张三3Modify!!!), User(id=2, name=张三2Modify!!!), User(id=4, name=张三4Modify!!!), User(id=9, name=张三9Modify!!!)]
            System.out.println(users);   // [User(id=1, name=张三), User(id=3, name=张三3Modify!!!), User(id=2, name=张三2Modify!!!), User(id=4, name=张三4Modify!!!), User(id=9, name=张三9Modify!!!)]
        }
        // 结论：ArrayList是新生成的对象，但里面元素的引用是原有的引用
    }

    private static void serialTest() throws Exception {
        System.out.println("=================== [serialTest] =================================");
        ArrayList<Long> list = new ArrayList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
        // 序列化
        FileOutputStream fileOutputStream = new FileOutputStream("array-serial.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(list);
        // 反序列化
        FileInputStream fileInputStream = new FileInputStream("array-serial.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object readObject = objectInputStream.readObject();
        System.out.println(readObject);     // [3, 8, 0, 8, 33, 7, 8]
        if (readObject instanceof ArrayList){
            ArrayList<Long> v = (ArrayList<Long>) readObject;
            System.out.println(v == list);      // false
            System.out.println(v);      // [3, 8, 0, 8, 33, 7, 8]
        }
    }

    private static void iteratorTest() {
        System.out.println("=================== [iteratorTest] =================================");
        ArrayList<Long> list = new ArrayList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
        // 基础迭代，单向
        StringBuilder findStr = new StringBuilder();
        Iterator<Long> iterator = list.iterator();
        while (iterator.hasNext()){
            Long next = iterator.next();
            findStr.append(next).append(" -> ");
            // 删除值为8的元素
            if (next.equals(8L)){
                iterator.remove();
            }
        }
        System.out.println("迭代器遍历顺序为: " + findStr.substring(0, findStr.length() - 4));        // 3 -> 8 -> 0 -> 8 -> 33 -> 7 -> 8
        // list迭代器，支持双向，从下标2开始
        StringBuilder findStr2 = new StringBuilder();
        ListIterator<Long> iterator1 = list.listIterator(0);
        while (iterator1.hasNext()){
            Long next = iterator1.next();
            findStr2.append(next).append(" -> ");
            if (next.equals(33L)){
                // 等于33时，打印下当前下标并移除33，并往前遍历两个位置
                int nextIndex = iterator1.nextIndex();
                System.out.println("value=33,index=" + nextIndex);
                iterator1.remove();
                iterator1.previous();
                iterator1.previous();
            }
        }
        System.out.println("迭代器遍历顺序为: " + findStr2.substring(0, findStr2.length() - 4));        // 3 -> 0 -> 33 -> 3 -> 0 -> 7
    }


    private static void findTest() {
        System.out.println("=================== [findTest] =================================");
        ArrayList<Long> list = new ArrayList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
        // 大小
        int size = list.size();     // 7
        System.out.println(size);
        // 空判断
        boolean empty = list.isEmpty();     // false
        System.out.println(empty);
        // 包含
        boolean contains = list.contains(7L);       // true
        System.out.println(contains);
        // 根据下标获取
        Long value = list.get(2);       // 0L
        System.out.println(value);
        // 获取元素第一个出现的下标位置
        int index = list.indexOf(8L);       // 1
        System.out.println(index);
        // 获取元素最后一个出现的下标位置
        int index2 = list.lastIndexOf(8L);       // 6
        System.out.println(index2);
        // 转换数组
        Object[] objects = list.toArray();      // [3, 8, 0, 8, 33, 7, 8]
        System.out.println(Arrays.toString(objects));
        // 转换范围数组
        Long[] longs = list.toArray(new Long[]{});  // [3, 8, 0, 8, 33, 7, 8]
        System.out.println(Arrays.toString(longs));
    }

    private static void deleteTest() {
        System.out.println("=================== [deleteTest] =================================");
        ArrayList<Long> list = new ArrayList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
        // 移除指定元素
        boolean remove = list.remove(1L);       // false ==> [3, 8, 0, 8, 33, 7, 8]
        System.out.println(remove + " ==> " + list);
        boolean remove2 = list.remove(8L);      // true ==> [3, 0, 8, 33, 7, 8]
        System.out.println(remove2 + " ==> " + list);
        // 移除指定下标元素
        Long removeNode = list.remove(1);       // 0 ==> [3, 8, 33, 7, 8]
        System.out.println(removeNode + " ==> " + list);
        // 批量移除
        boolean removeAll = list.removeAll(Arrays.asList(7L, 8L, 6L));      // true ==> [3, 33]
        System.out.println(removeAll + " ==> " + list);
        // 按照条件批量移除
        boolean removeIf = list.removeIf(v -> v > 10);      // true ==> [3]
        System.out.println(removeIf + " ==> " + list);
        // 批量替换 ==> 对于大于5的元素全部乘以5，小于等于5的乘以2
        list.replaceAll(v -> v > 5 ? v*5 : v*2);            // [6]
        System.out.println(list);
    }

    private static void addTest() {
        System.out.println("=================== [addTest] =================================");
        ArrayList<Long> list = new ArrayList<>();
        // 末尾新增
        list.add(1L);       // [1]
        System.out.println(list);
        // 执行下标新增
        list.add(0, 2L);        // [2,1]
        System.out.println(list);
        // 批量新增
        list.addAll(Collections.singletonList(4L));     // [2,1,4]
        System.out.println(list);
        // 指定下标 批量新增
        list.addAll(1, Arrays.asList(1L, 9L, 3L));      // [2,1,9,3,1,4]
        System.out.println(list);
        // 通过下标修改  ==> 无法通过下标新增，会抛出IndexOutOfBoundsException
        list.set(2, 4L);        // [2,1,4,3,1,4]
        System.out.println(list);
    }


}
```

# 二：ArrayList核心源码分析

### 2.1 类图

![img](http://rocks526.top/lzx/20190721142329326.png)

实现了 4 个接口，分别是：

- List接口：提供数组的添加、删除、修改、迭代遍历等操作
- Cloneable接口：表示 ArrayList 支持克隆
- Serializable接口：表示 ArrayList 支持序列化的功能
- RandomAccess接口：表示 ArrayList 支持快速的随机访问

继承了 `java.util.AbstractList` 抽象类，而 AbstractList 提供了 List 接口的骨架实现，大幅度的减少了实现`迭代遍历`相关操作的代码。

注：不过实际上，由于效率问题，ArrayList 大量重写了 AbstractList 提供的方法实现。所以，AbstractList 对于 ArrayList 意义不大，更多的是 AbstractList 其它子类享受了这个福利。

### 2.2 属性

ArrayList 的属性很少，仅仅 2 个，示意图如下：

![ArrayList](http://rocks526.top/lzx/02.png)

- `elementData` 属性：元素数组。其中，图中红色空格代表我们已经添加元素，白色空格代表我们并未使用
- `size` 属性：数组大小。注意，`size` 代表的是 ArrayList 已使用 `elementData` 的元素的数量，对于开发者看到的 `size()` 也是该大小。并且，当我们添加新的元素时，恰好其就是元素添加到 `elementData` 的位置（下标）。当然，我们知道 ArrayList 真正的大小是 `elementData` 的大小

```java
    // 序列化ID
    private static final long serialVersionUID = 8683452581122892189L;

    // 初始容量
    private static final int DEFAULT_CAPACITY = 10;

    // 当数组为空时的值，避免频繁创建数组对象 ==> 主动声明初始容量为0时使用，add时扩容从0开始，1.5扩容
    private static final Object[] EMPTY_ELEMENTDATA = {};

    // 当数组为空时的值，避免频繁创建数组对象 ==> 默认构造方法时使用，add时扩容直接从10开始
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    // 底层数组，用于真正存储数据
    // transient代表序列化时忽略此属性，ArrayList重写了readObject和writeObject方法，定制了序列化的行为
    transient Object[] elementData; // non-private to simplify nested class access

    // 当前集合的大小
    private int size;
```

### 2.3 构造方法

ArrayList 一共有三个构造方法，分别如下：

- `#ArrayList(int initialCapacity)`

`#ArrayList(int initialCapacity)` 构造方法，根据传入的初始化容量，创建 ArrayList 数组。如果我们在使用时，如果预先指到数组大小，一定要使用该构造方法，`可以避免数组扩容提升性能`，同时也是合理使用内存。

```java
    // 构造方法，指定初始容量
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            // 直接创建目标大小数组，避免频繁扩容
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            // 空数组，内置的instance，避免频繁创建数组对象
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }
```

比较特殊的是，如果初始化容量为 0 时，使用 `EMPTY_ELEMENTDATA` 空数组。在添加元素的时候，会进行扩容创建需要的数组。

- `#ArrayList(Collection<? extends E> c)`

`#ArrayList(Collection<? extends E> c)` 构造方法，使用传入的 `c` 集合，作为 ArrayList 的 elementData。

```java
    // 构造方法，根据指定集合创建ArrayList
    public ArrayList(Collection<? extends E> c) {
        // 转换数组
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // 数组非空
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                // 数组拷贝一下 ==> c.toArray返回的数组虽然是Object[]，但其内部可能是真正的泛型类型，因此重新拷贝一下
                // Jdk8返回的是泛型数组，Jdk9改为范围Object[]
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // 空数组
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
```

整体逻辑比较简单，可能数组拷贝那块比较费解，它是用于解决 [JDK-6260652](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6260652) 的 Bug 。它在 JDK9 中被解决，也就是说，JDK8 还会存在该问题，bug复现如下：

```java
public static void test02() {
    List<Integer> list = Arrays.asList(1, 2, 3);
    Object[] array = list.toArray(); // JDK8 返回 Integer[] 数组，JDK9+ 返回 Object[] 数组。
    System.out.println("array className ：" + array.getClass().getSimpleName());

    // 此处，在 JDK8 和 JDK9+ 表现不同，前者会报 ArrayStoreException 异常，后者不会。
    array[0] = new Object();
}
```

- `#ArrayList()`

无参数构造方法 `#ArrayList()` ，也是使用最多的构造方法，代码如下：

```java
    // 默认构造方法，初始容量10，数组为空数组，第一次add时再真正创建数组
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
```

构造的时候不创建数组，只设置一个空数组引用，在第一次add时再进行创建数组，可以有效节省内存。

注：之所以用`DEFAULTCAPACITY_EMPTY_ELEMENTDATA`而不是`EMPTY_ELEMENTDATA`，是为了区分出是空参构造，还是构造方法传入了初始容量为0，针对空参构造，第一次扩容直接为10，而后者会从0开始1.5倍扩容。

### 2.4 添加元素

- `#add(E e)` 

顺序添加单个元素到数组末尾，代码如下：

```java
    // 集合末尾新增元素
    public boolean add(E e) {
        // 容量检查
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        // 新增
        elementData[size++] = e;
        return true;
    }

    // 指定下标新增元素
    public void add(int index, E element) {
        // index检查
        rangeCheckForAdd(index);
        // 容量检查
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        // index之后的数据拷贝
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        // 新增元素
        elementData[index] = element;
        // 更新size
        size++;
    }
```

整体逻辑都比较简单，需要注意的就是`ensureCapacityInternal`方法，此处会进行容量检查，当容量不足添加新的元素时，会自动扩容。

### 2.5 动态扩容

- 扩容

ArrayList基于数组的封装，提供动态扩容的特性。

真正的扩容逻辑在`#grow()` 方法里，整个的扩容过程，首先创建一个新的更大的数组，一般是`1.5倍` ，然后将原数组复制到新数组中，最后返回新数组，具体代码如下：

```java
    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            // 默认构造方法初始化，计算需要的最小容量，最低从10开始
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    // 扩容操作
    private void ensureExplicitCapacity(int minCapacity) {
        // 变更++
        modCount++;

        // 扩容
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    // 数组最大长度
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    // 扩容操作，要求最新容量为minCapacity
    private void grow(int minCapacity) {
        // 之前的容量
        int oldCapacity = elementData.length;
        // 扩容1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            // 扩容1.5倍之后还是不足，则扩容到minCapacity
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            // 数组越界
            newCapacity = hugeCapacity(minCapacity);
        // 拷贝数组到新数组
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0)
            throw new OutOfMemoryError();
        // 最大长度修正
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
```

1. minCapacity参数代表要求的最小容量，扩容后的容量大于等于此值
2. 当第一次add时，由于oldCapacity为0，因此计算的newCapacity也是0，则扩容后的大小取决于minCapacity
3. 当使用默认构造方法时，使用的是`DEFAULTCAPACITY_EMPTY_ELEMENTDATA`，传入的minCapacity为10，即一次扩容到10个元素
4. 当使用带初始化参数的构造方法，并且设置初始容量为0时，则使用的是`EMPTY_ELEMENTDATA`，此时会扩容到1个元素

注：后续扩容都会基于旧的容量扩大1.5倍。

- 缩容

既然有数组扩容方法，那么是否有缩容方法呢？在 `#trimToSize()` 方法中，会创建大小恰好够用的新数组，并将原数组复制到其中。代码如下：

```java
    // 数组缩容，节省内存
    public void trimToSize() {
        // 修改次数++
        modCount++;
        if (size < elementData.length) {
            // 当前容量小于数组大小，进行缩容
            elementData = (size == 0)
                    // 如果是空数组，不创建新的，直接使用EMPTY_ELEMENTDATA
              ? EMPTY_ELEMENTDATA
                    // 数组拷贝，拷贝elementData从0到size的数据到一个新数组
              : Arrays.copyOf(elementData, size);
        }
    }
```

注：在remove的时候，并不会自动触发缩容，此方法是交给使用方自行调用的。

### 2.6 批量添加

`#addAll(Collection<? extends E> c)` 方法，批量添加多个元素。在我们明确知道会添加多个元素时，推荐使用该该方法而不是添加单个元素，避免可能多次扩容。代码如下：

```java
    // 批量新增
    public boolean addAll(Collection<? extends E> c) {
        // 转数组
        Object[] a = c.toArray();
        // 计算容量是否足够，不足则扩容
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        // 数组搬移，将c整个拷贝进原数组
        System.arraycopy(a, 0, elementData, size, numNew);
        // 修改数量
        size += numNew;
        return numNew != 0;
    }

    // 从指定下标开始批量插入
    public boolean addAll(int index, Collection<? extends E> c) {
        // 下标检查
        rangeCheckForAdd(index);

        // 容量计算
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        // 数组搬移计算
        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        // 拷贝目标数组进原数组
        System.arraycopy(a, 0, elementData, index, numNew);
        // 更新大小
        size += numNew;
        return numNew != 0;
    }
```

1. 整体逻辑也比较简单，添加时会一次计算好需要的整体容量，一次性完成扩容，避免多次扩容导致的数据搬移。

2. 在从指定下标插入时，首先调整原数组位置，再批量拷贝新增数组到原数组里

### 2.7 删除元素

- 根据下标移除

`#remove(int index)` 方法，移除指定位置的元素，并返回该位置的原元素。代码如下：

```java
    // 移除指定下标元素
    public E remove(int index) {
        // index检查
        rangeCheck(index);

        // 变更计数
        modCount++;
        // 获取旧值
        E oldValue = elementData(index);

        // 计算要搬移的元素个数
        int numMoved = size - index - 1;
        if (numMoved > 0)
            // 元素搬移
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        // 末尾元素设置为null，进行GC
        elementData[--size] = null; // clear to let GC do its work

        // 返回旧值
        return oldValue;
    }
```

逻辑较为简单，先记录旧值，再将后续的元素往前覆盖，清除最后一个元素即可。

- 移除指定元素

`#remove(Object o)` 方法，移除首个为 `o` 的元素，并返回是否移除到。代码如下：

```java
    // 移除匹配的第一个元素
    public boolean remove(Object o) {
        if (o == null) {
            // 移除null
            for (int index = 0; index < size; index++)
                // 查找目标元素下标
                if (elementData[index] == null) {
                    // 根据下标移除
                    fastRemove(index);
                    return true;
                }
        } else {
            // 非null
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    // 根据元素下标快速移除
    private void fastRemove(int index) {
        // 变更记录+1
        modCount++;
        // 计算后面要搬移的元素个数
        int numMoved = size - index - 1;
        if (numMoved > 0)
            // 元素搬移
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        // 末尾元素设置为null，进行gc
        elementData[--size] = null; // clear to let GC do its work
    }
```

首先遍历查找目标元素，获取第一次出现的下标，再根据下标进行移除，逻辑和上面类似。

### 2.8 批量删除

- 范围移除

先来看 `#removeRange(int fromIndex, int toIndex)` 方法，批量移除 `[fromIndex, toIndex)` 的多个元素，注意不包括 `toIndex` 的元素。代码如下：

```java
   // 根据下标范围移除
    protected void removeRange(int fromIndex, int toIndex) {
        // 变更记录+1
        modCount++;

        // 计算要搬移的元素个数
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // 更新大小，并将多余的元素清除
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }
```

和上面删除差不多的逻辑，都是先搬移再置null。

注：此方法里没有做下标合法性检查，因此是protect方法，只能其他public方法调用，调用之前确定下标合法。

- 批量移除

`#removeAll(Collection<?> c)` 方法，批量移除指定的多个元素。具体代码如下：

```java
    // 移除c中包含的所有元素
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }
    /**
     * @param c 参数数组
     * @param complement    参数数组包含此元素时保留，还是不包含时保留
     * @return
     */
    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        // r是遍历原数组的下标，w是保留新数组的下标
        int r = 0, w = 0;
        boolean modified = false;
        try {
            // 遍历原数组
            for (; r < size; r++)
                // 检查符合条件的元素
                if (c.contains(elementData[r]) == complement)
                    // 覆盖到原数组里
                    elementData[w++] = elementData[r];
        } finally {
            // 正常情况，r必然等于size
            // 此处是为了兼容c在contain方法抛出异常的情况
            if (r != size) {
                // 抛出异常后面还没有遍历的元素，全部认为是满足条件的，拷贝到w的后面
                System.arraycopy(elementData, r,
                                 elementData, w,
                                 size - r);
                // 更新w的数量
                w += size - r;
            }
            // 此处代表有元素不满足条件，只有w个元素满足条件，并且此时这些满足条件的元素已经被拷贝到0到w的位置了
            if (w != size) {
                // 将不满足条件的元素清除
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                // 更新modCount和size
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }
```

整体逻辑不难，写的比较难理解。核心思路还是先搬移再置null，之所以抽出`batchRemove`方法是为了和`retainAll`方法共用，将`参数集合包含元素时是否进行移除`这个条件封装为complement变量。

- 取交集保留

`#retainAll(Collection<?> c)` 方法，求 `elementData` 数组和指定多个元素的交集。简单来说，恰好和 `#removeAll(Collection<?> c)` 相反，移除不在 `c` 中的元素。代码如下：

```java
    // 保留与c数组的交集部分，与removeAll相反
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }
```

`batchRemove`方法在上面介绍了，理解上面后就很好理解。

- 根据条件移除

`removeIf(Predicate<? super E> filter)`方法，移除满足filter条件的所有元素，具体代码如下：

```java
    // 根据条件移除
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // 记录移除数量
        int removeCount = 0;
        // 记录移除的所有下标
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                // 满足条件，记录一下，等会统一移除
                removeSet.set(i);
                removeCount++;
            }
        }
        // 并发检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }


        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            // 存在要移除的元素
            final int newSize = size - removeCount;
            // 将不需要移除的元素全部从0开始往前覆盖
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
                // 获取不需要移除的元素
                i = removeSet.nextClearBit(i);
                // 保存
                elementData[j] = elementData[i];
            }
            // 移除多余的元素
            for (int k=newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            // 更新size
            this.size = newSize;
            // 并发检查
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            modCount++;
        }

        return anyToRemove;
    }
```

整体逻辑为：先记录所有要移除的元素，再遍历保存需要保存的元素，最后将其他元素置null。过程中使用了位图记录所有需要移除的元素下标。 

### 2.9 查找元素

- `#indexOf(Object o)`

`#indexOf(Object o)` 方法，查找首个为指定元素的位置。代码如下：

```java
    // 查找某个元素在集合中出现的第一个索引下标
    public int indexOf(Object o) {
        if (o == null) {
            // 查null
            for (int i = 0; i < size; i++)
                // 遍历查找
                if (elementData[i]==null)
                    return i;
        } else {
            // 非null
            for (int i = 0; i < size; i++)
                // 遍历
                if (o.equals(elementData[i]))
                    return i;
        }
        // 没找到
        return -1;
    }

```

代码逻辑比较简单，就是针对null和非null的情况遍历查询，没查询到返回-1。

- `#contains(Object o)`

而 `#contains(Object o)` 方法，就是基于indexOf()方法实现，代码如下：

```java
    // 判断集合是否包含某个元素
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
```

只要返回的下标大于0，即扎到了目标元素，也就是包含目标元素。

- `#lastIndexOf(Object o)`

有时我们需要查找最后一个为指定元素的位置，所以会使用到 `#lastIndexOf(Object o)` 方法。代码如下：

```java
    // 查找某个元素在集合中出现的最后一个索引下标
    public int lastIndexOf(Object o) {
        // 遍历
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        // 未找到
        return -1;
    }
```

代码逻辑和indexOf一致，不过是从尾到头遍历而已。

### 2.10 下标操作

- `#get(int index)`

`#get(int index)` 方法，获得指定位置的元素。代码如下：

```java
    // 根据下标获取
    public E get(int index) {
        // index检查
        rangeCheck(index);
        // 获取
        return elementData(index);
    }
    // 访问时的下标越界检查
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    E elementData(int index) {
        return (E) elementData[index];
    }
```

根据index访问元素，是数组的特性，时间复杂度O(1)。

- `#set(int index, E element)`

`#set(int index, E element)` 方法，设置指定位置的元素。代码如下：

```java
    // 设置指定下标的元素
    public E set(int index, E element) {
        // index检查
        rangeCheck(index);
        // 获取旧值
        E oldValue = elementData(index);
        // 更新
        elementData[index] = element;
        // 返回旧值
        return oldValue;
    }
```

根据下标进行更新，时间复杂度O（1）。

### 2.11 转换数组

- `#toArray()`

`#toArray()` 方法，将 ArrayList 转换成 `Object[]` 数组。代码如下：

```java
    // 转换数组
    public Object[] toArray() {
        // 直接拷贝
        return Arrays.copyOf(elementData, size);
    }
```

直接调用Arrays进行拷贝，底层调用System的内存地址拷贝方法，c++实现。

- `#toArray(T[] a)`

在上面的`toArray()`中，返回的是Object[]，需要进行强制类型转换。实际场景下，我们可能想要指定 `T` 泛型的数组，那么我们就需要使用到 `#toArray(T[] a)` 方法。代码如下：

```java
    // 转换泛型数组
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // 拷贝完整数组
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        // 拷贝完整数组，并且长度和a保持一致
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
```

需要注意的是，如果传入的数组比较小，则会返回一个拷贝好的新数组，反之，直接拷贝进传入的数组里。

### 2.12 相等判断

- `#hashCode()`

`#hashCode()` 方法，求 ArrayList 的哈希值。代码如下：

```java
    // 计算哈希值
    public int hashCode() {
        int hashCode = 1;
        // 和每个元素都有关
        for (E e : this)
            hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
        return hashCode;
    }
```

会对每个元素的哈希值综合计算，比较有意思的一点在于这个31，之所以要乘31主要有两点原因：

1. 31是一个中等质数，一般哈希冲突较低

2. 31可以被 JVM 优化，`31 * i = (i << 5) - i`

详细参考：https://www.iocoder.cn/Fight/Why-did-the-String-hashCode-method-select-the-number-31-as-a-multiplier/?vip&self

- `#equals(Object o)`

`#equals(Object o)` 方法，判断是否相等。代码如下：

```java
    // 相等比较，要求两个集合元素和顺序完全一致则相等
    public boolean equals(Object o) {
        // 引用判断
        if (o == this)
            return true;
        // 类型判断
        if (!(o instanceof List))
            return false;

        // 获取迭代器
        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        // 逐一对比
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }
```

相等的比较也比较简单，首先根据引用和类型快速判断一下，判断不出时，进行元素逐个对比。

注：比较有意思的是，这个`hashCode和equals方法都定义在AbstractList父类里，ArrayList和LinkedList都没有进行重写，而现在默认的判断逻辑是不关心具体实现子类的，只关心集合元素是否一致`。因此下面的结果，其实都是true。

```java
    private static void equalsTest() {
        ArrayList<Integer> array = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        System.out.println(array.equals(linkedList));       // true
        array.add(1);
        linkedList.add(1);
        System.out.println(array.equals(linkedList));       // true
    }
```

### 2.13 清空数组

`#clear()` 方法，清空数组。代码如下：

```java
   // 清空集合元素
    public void clear() {
        // 变更记录+1
        modCount++;

        // 逐个遍历，将元素设置为null，帮助GC
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        // 更新元素个数
        size = 0;
    }
```

清空数组的操作也比较有意思，清空数组其实在实现上，只要更新size这个字段即可，因为其他API都会受这个字段控制，`之所以要清除所有元素，是为了清除元素和ArrayList之间的引用，让Jvm尽快进行GC，如果不清除元素，必须该下标位置再次被设置新值时，才会切断引用`。

### 2.14 序列化

- `#writeObject(java.io.ObjectOutputStream s)`

`#writeObject(java.io.ObjectOutputStream s)` 方法，实现 ArrayList 的序列化。代码如下：

```java
    // 序列化
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
        // 保存开始序列化时的变更记录
        int expectedModCount = modCount;

        // 写入非静态属性、非 transient 属性
        s.defaultWriteObject();

        // 写入 size ，主要为了与 clone 方法的兼容 (size非transient属性，其实上面的方法已经写入了)
        s.writeInt(size);

        // 逐个写入 elementData 数组的元素 (elementData数组开辟的空间可能并没有用完，原生的序列化会把后面的null也写入，为了节省空间，因此重写了序列化方法，只写入有数据的[0-size)元素)
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }

        // 序列化过程中有其他线程修改过数组，抛出异常
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }
```

ArrayList重写了序列化方案，主要原因是`容器的容量一般都比当前实际存储的元素多，而默认序列化方案会将整个数组进行序列化，因此为了节省内存，重写了序列化方案，只序列化已存储的值`。

注：size字段是个比较奇怪的点，size字段并未被transient修饰，因此调用默认序列化时就会写入序列化文件，但后面又重新写了一次，根据相关注释，只说是为了和clone()方法保持一致。

- `readObject(java.io.ObjectInputStream s)`

`#readObject(java.io.ObjectOutputStream s)` 方法，实现 ArrayList 的反序列化。代码如下：

```java
    // 反序列化
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {

        // 先设置为空数组
        elementData = EMPTY_ELEMENTDATA;

        // 读取非静态属性、非 transient 属性
        s.defaultReadObject();

        // 读取 size ，不过忽略返回值，不用 (默认方法里已经读取出size并设置了)
        s.readInt(); // ignored

        if (size > 0) {
            // 数组扩容，确保内存足够
            ensureCapacityInternal(size);

            // 按顺序逐步读取所有元素并赋值
            Object[] a = elementData;
            for (int i=0; i<size; i++) {
                a[i] = s.readObject();
            }
        }
    }
```

按照序列化的顺序反过来即可，比较有意思的在于，并非调用add方法逐个新增回去，而是`先扩容，再逐个给数组直接赋值，避免了频繁扩容可能导致的开销`，和addAll方法思路类似。

### 2.15 克隆

- `#clone()`

`#clone()` 方法，克隆 ArrayList 对象。代码如下：

```java
    // 克隆， 返回的ArrayList是一个新的实例，但里面的元素还是之前的引用
    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }
```

整体逻辑就是先克隆ArrayList，再拷贝一份elementData，需要注意的是，`elementData` 是重新拷贝出来的新的数组，和原数组不是同一个，但`数组里元素的引用是同一个`。

### 2.16 切分数组

- `#subList(int fromIndex, int toIndex)`

`#subList(int fromIndex, int toIndex)` 方法，创建 ArrayList 的子数组。代码如下：

```java
    // 数组切分
    public List<E> subList(int fromIndex, int toIndex) {
        // 下标检查
        subListRangeCheck(fromIndex, toIndex, size);
        // 切分
        return new SubList(this, 0, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }

    // 依旧没有采用父类，自己重写
    private class SubList extends AbstractList<E> implements RandomAccess {
        // 原数组引用
        private final AbstractList<E> parent;
        // 原数组开始下标
        private final int parentOffset;
        // 此数组开始下标
        private final int offset;
        // 此数组大小
        int size;

        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayList.this.modCount;
        }

        // 修改元素，会影响原数组
        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        // 获取下标元素
        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }

        // 获取大小
        public int size() {
            checkForComodification();
            return this.size;
        }

        // 新增，会影响原数组
        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        // 移除，会影响原数组
        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        // 范围移除，会影响原数组
        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            parent.removeRange(parentOffset + fromIndex,
                               parentOffset + toIndex);
            this.modCount = parent.modCount;
            this.size -= toIndex - fromIndex;
        }


        // 批量新增，会影响原数组
        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }


        // 批量新增，会影响原数组
        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;

            checkForComodification();
            parent.addAll(parentOffset + index, c);
            this.modCount = parent.modCount;
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = ArrayList.this.modCount;

                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                @SuppressWarnings("unchecked")
                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    final int size = SubList.this.size;
                    int i = cursor;
                    if (i >= size) {
                        return;
                    }
                    final Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length) {
                        throw new ConcurrentModificationException();
                    }
                    while (i != size && modCount == expectedModCount) {
                        consumer.accept((E) elementData[offset + (i++)]);
                    }
                    // update once at end of iteration to reduce heap write traffic
                    lastRet = cursor = i;
                    checkForComodification();
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        ArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkForComodification();

                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (expectedModCount != ArrayList.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

        public Spliterator<E> spliterator() {
            checkForComodification();
            return new ArrayListSpliterator<E>(ArrayList.this, offset,
                                               offset + this.size, this.modCount);
        }
    }
```

大部分的逻辑都在SubList类里实现，需要注意的是，`SubList 不是一个只读数组`，而是和根数组 `root` 共享相同的 `elementData` 数组，只是说限制了 `[fromIndex, toIndex)` 的范围，即对SubList数组的修改会影响到原数组。

### 2.17 迭代器操作

- `iterator()`

`#iterator()` 方法，创建迭代器。一般情况下，我们使用迭代器遍历 ArrayList、LinkedList 等等 List 的实现类。代码如下：

```java
    // 返回List迭代器
    public Iterator<E> iterator() {
        return new Itr();
    }

    // List迭代器的实现
    private class Itr implements Iterator<E> {

        // 游标
        int cursor = 0;

        // 最近一次调用next或prev的元素的下标，如果删除，则置为-1
        int lastRet = -1;

        // 检测是否存在并发修改，实现fast-fail机制
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        // 获取下一个元素
        public E next() {
            checkForComodification();
            try {
                int i = cursor;
                // 获取值
                E next = get(i);
                // 更新游标和上一个元素下标
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                // 检查是否并发导致
                checkForComodification();
                // 非并发导致，抛出异常
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            // 上次操作元素下标检查
            if (lastRet < 0)
                throw new IllegalStateException();
            // 并发操作检查
            checkForComodification();

            try {
                // 移除指定下标的元素
                AbstractList.this.remove(lastRet);
                if (lastRet < cursor)
                    // 游标更新
                    cursor--;
                // 删除成功，lastRet置为-1
                lastRet = -1;
                // list调用remove更新了modCount，因此更新expectedModCount
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                // lastRet是之前访问过的元素，因此必然存在，如果下标越界，必然是其他线程操作了
                throw new ConcurrentModificationException();
            }
        }

        // 快速失败策略，每次遍历之前检查是否存在并发操作
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
```

内部逻辑通过 Itr 迭代器实现，Itr 实现 `java.util.Iterator` 接口，是 ArrayList 的内部类。

虽然说 AbstractList 也提供了一个 Itr 的实现，但是 ArrayList 为了更好的性能，所以自己实现了。

- `listIterator()`

`listIterator()`返回的是一个用于list的增强迭代器，可以支持双向遍历、修改、从指定下标开始遍历等操作。

```java
    // 返回List迭代器
    public ListIterator<E> listIterator() {
        // 从头开始遍历
        return listIterator(0);
    }
    // 从指定下标返回迭代器
    public ListIterator<E> listIterator(final int index) {
        rangeCheckForAdd(index);

        return new ListItr(index);
    }

    // Itr的增强
    private class ListItr extends Itr implements ListIterator<E> {

        // 支持从指定下标开始遍历
        ListItr(int index) {
            cursor = index;
        }

        // 判断前面是否还有元素
        public boolean hasPrevious() {
            return cursor != 0;
        }

        // 向前遍历
        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                E previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        // 当前元素的下标
        public int nextIndex() {
            return cursor;
        }

        // 上个元素的下标
        public int previousIndex() {
            return cursor-1;
        }

        // 更新刚刚遍历的元素
        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractList.this.set(lastRet, e);
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        // 新增元素
        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                AbstractList.this.add(i, e);
                lastRet = -1;
                cursor = i + 1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
```

# 三：ArrayList总结

### 3.1 ArrayList特性总结

- ArrayList是基于`数组`实现的List实现类，除了数组的`随机下标访问`特性外，ArrayList支持`自动扩容`、`下标检查`、`并发快速失败`、`迭代器`等特性
- ArrayList自动扩容时，正常情况都是`扩容1.5倍`，同时也支持`手动扩容`、`缩容`。
- ArrayList `随机访问时间复杂度是 O(1)` ，`查找指定元素的平均时间复杂度是 O(n) `。
- ArrayList 做了很多细节处的优化，例如：
  - 在初始化时，采用`懒加载策略`，都是直接使用一个空数组，当新增时再创建，节省内存
  - 在序列化时，`重写序列化策略`，只序列化已添加元素的数据，忽略数组空的位置
  - 通过`位移操作`替代运算符
  - 批量操作时，都是先计算容量等，再进行一次性拷贝，尽可能`减少数组拷贝次数`

### 3.2 ArrayList完整源码附录

```java
package java.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 基于数组的扩展，支持动态扩容、下标检查、克隆等特性
 * 非线程安全，当并发访问时，支持快速失败策略
 */
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{

    // 序列化ID
    private static final long serialVersionUID = 8683452581122892189L;

    // 初始容量
    private static final int DEFAULT_CAPACITY = 10;

    // 当数组为空时的值，避免频繁创建数组对象 ==> 主动声明初始容量为0时使用，add时扩容从0开始，1.5扩容
    private static final Object[] EMPTY_ELEMENTDATA = {};

    // 当数组为空时的值，避免频繁创建数组对象 ==> 默认构造方法时使用，add时扩容直接从10开始
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    // 底层数组，用于真正存储数据
    // transient代表序列化时忽略此属性，ArrayList重写了readObject和writeObject方法，定制了序列化的行为
    transient Object[] elementData; // non-private to simplify nested class access

    // 当前集合的大小
    private int size;

    // 构造方法，指定初始容量
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            // 直接创建目标大小数组，避免频繁扩容
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            // 空数组，内置的instance，避免频繁创建数组对象
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }

    // 默认构造方法，初始容量10，数组为空数组，第一次add时再真正创建数组
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    // 构造方法，根据指定集合创建ArrayList
    public ArrayList(Collection<? extends E> c) {
        // 转换数组
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // 数组非空
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                // 数组拷贝一下 ==> c.toArray返回的数组虽然是Object[]，但其内部可能是真正的泛型类型，因此重新拷贝一下
                // Jdk8返回的是泛型数组，Jdk9改为范围Object[]
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // 空数组
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    // 数组缩容，节省内存
    public void trimToSize() {
        // 修改次数++
        modCount++;
        if (size < elementData.length) {
            // 当前容量小于数组大小，进行缩容
            elementData = (size == 0)
                    // 如果是空数组，不创建新的，直接使用EMPTY_ELEMENTDATA
              ? EMPTY_ELEMENTDATA
                    // 数组拷贝，拷贝elementData从0到size的数据到一个新数组
              : Arrays.copyOf(elementData, size);
        }
    }

    // 数组容量检查，不足时进行扩容
    public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                // 初始化时指定容量为0，则扩容从0开始
            ? 0
                // 空参构造方法，初始容量从10开始
            : DEFAULT_CAPACITY;

        if (minCapacity > minExpand) {
            // 需要的容量大于当前的容量，进行库容
            ensureExplicitCapacity(minCapacity);
        }
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            // 默认构造方法初始化，计算需要的最小容量，最低从10开始
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    // 扩容操作
    private void ensureExplicitCapacity(int minCapacity) {
        // 变更++
        modCount++;

        // 扩容
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    // 数组最大长度
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    // 扩容操作，要求最新容量为minCapacity
    private void grow(int minCapacity) {
        // 之前的容量
        int oldCapacity = elementData.length;
        // 扩容1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            // 扩容1.5倍之后还是不足，则扩容到minCapacity
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            // 数组越界
            newCapacity = hugeCapacity(minCapacity);
        // 拷贝数组到新数组
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0)
            throw new OutOfMemoryError();
        // 最大长度修正
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    // 返回集合元素个数
    public int size() {
        return size;
    }

    // 判断集合是否为空
    public boolean isEmpty() {
        return size == 0;
    }


    // 判断集合是否包含某个元素
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    // 查找某个元素在集合中出现的第一个索引下标
    public int indexOf(Object o) {
        if (o == null) {
            // 查null
            for (int i = 0; i < size; i++)
                // 遍历查找
                if (elementData[i]==null)
                    return i;
        } else {
            // 非null
            for (int i = 0; i < size; i++)
                // 遍历
                if (o.equals(elementData[i]))
                    return i;
        }
        // 没找到
        return -1;
    }

    // 查找某个元素在集合中出现的最后一个索引下标
    public int lastIndexOf(Object o) {
        // 遍历
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        // 未找到
        return -1;
    }


    // 克隆， 返回的ArrayList是一个新的实例，但里面的元素还是之前的引用
    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }


    // 转换数组
    public Object[] toArray() {
        // 直接拷贝
        return Arrays.copyOf(elementData, size);
    }

    // 转换泛型数组
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // 拷贝完整数组
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        // 拷贝完整数组，并且长度和a保持一致
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    // ================================= 下标访问操作 =======================================

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    // 根据下标获取
    public E get(int index) {
        // index检查
        rangeCheck(index);
        // 获取
        return elementData(index);
    }

    // 设置指定下标的元素
    public E set(int index, E element) {
        // index检查
        rangeCheck(index);
        // 获取旧值
        E oldValue = elementData(index);
        // 更新
        elementData[index] = element;
        // 返回旧值
        return oldValue;
    }

    // 集合末尾新增元素
    public boolean add(E e) {
        // 容量检查
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        // 新增
        elementData[size++] = e;
        return true;
    }

    // 指定下标新增元素
    public void add(int index, E element) {
        // index检查
        rangeCheckForAdd(index);
        // 容量检查
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        // index之后的数据拷贝
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        // 新增元素
        elementData[index] = element;
        // 更新size
        size++;
    }

    // 移除指定下标元素
    public E remove(int index) {
        // index检查
        rangeCheck(index);

        // 变更计数
        modCount++;
        // 获取旧值
        E oldValue = elementData(index);

        // 计算要搬移的元素个数
        int numMoved = size - index - 1;
        if (numMoved > 0)
            // 元素搬移
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        // 末尾元素设置为null，进行GC
        elementData[--size] = null; // clear to let GC do its work

        // 返回旧值
        return oldValue;
    }

    // 移除匹配的第一个元素
    public boolean remove(Object o) {
        if (o == null) {
            // 移除null
            for (int index = 0; index < size; index++)
                // 查找目标元素下标
                if (elementData[index] == null) {
                    // 根据下标移除
                    fastRemove(index);
                    return true;
                }
        } else {
            // 非null
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    // 根据元素下标快速移除
    private void fastRemove(int index) {
        // 变更记录+1
        modCount++;
        // 计算后面要搬移的元素个数
        int numMoved = size - index - 1;
        if (numMoved > 0)
            // 元素搬移
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        // 末尾元素设置为null，进行gc
        elementData[--size] = null; // clear to let GC do its work
    }


    // 清空集合元素
    public void clear() {
        // 变更记录+1
        modCount++;

        // 逐个遍历，将元素设置为null，帮助GC
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        // 更新元素个数
        size = 0;
    }


    // 批量新增
    public boolean addAll(Collection<? extends E> c) {
        // 转数组
        Object[] a = c.toArray();
        // 计算容量是否足够，不足则扩容
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        // 数组搬移，将c整个拷贝进原数组
        System.arraycopy(a, 0, elementData, size, numNew);
        // 修改数量
        size += numNew;
        return numNew != 0;
    }

    // 从指定下标开始批量插入
    public boolean addAll(int index, Collection<? extends E> c) {
        // 下标检查
        rangeCheckForAdd(index);

        // 容量计算
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        // 数组搬移计算
        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        // 拷贝目标数组进原数组
        System.arraycopy(a, 0, elementData, index, numNew);
        // 更新大小
        size += numNew;
        return numNew != 0;
    }


    // 根据下标范围移除
    protected void removeRange(int fromIndex, int toIndex) {
        // 变更记录+1
        modCount++;

        // 计算要搬移的元素个数
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // 更新大小，并将多余的元素清除
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }


    // 访问时的下标越界检查
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // 新增时的下标越界检查
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    // 下标越界时的异常提示
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }


    // 移除c中包含的所有元素
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }


    // 保留与c数组的交集部分，与removeAll相反
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    /**
     * @param c 参数数组
     * @param complement    参数数组包含此元素时保留，还是不包含时保留
     * @return
     */
    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        // r是遍历原数组的下标，w是保留新数组的下标
        int r = 0, w = 0;
        boolean modified = false;
        try {
            // 遍历原数组
            for (; r < size; r++)
                // 检查符合条件的元素
                if (c.contains(elementData[r]) == complement)
                    // 覆盖到原数组里
                    elementData[w++] = elementData[r];
        } finally {
            // 正常情况，r必然等于size
            // 此处是为了兼容c在contain方法抛出异常的情况
            if (r != size) {
                // 抛出异常后面还没有遍历的元素，全部认为是满足条件的，拷贝到w的后面
                System.arraycopy(elementData, r,
                                 elementData, w,
                                 size - r);
                // 更新w的数量
                w += size - r;
            }
            // 此处代表有元素不满足条件，只有w个元素满足条件，并且此时这些满足条件的元素已经被拷贝到0到w的位置了
            if (w != size) {
                // 将不满足条件的元素清除
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                // 更新modCount和size
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }


    // 序列化
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
        // 保存开始序列化时的变更记录
        int expectedModCount = modCount;

        // 写入非静态属性、非 transient 属性
        s.defaultWriteObject();

        // 写入 size ，主要为了与 clone 方法的兼容 (size非transient属性，其实上面的方法已经写入了)
        s.writeInt(size);

        // 逐个写入 elementData 数组的元素 (elementData数组开辟的空间可能并没有用完，原生的序列化会把后面的null也写入，为了节省空间，因此重写了序列化方法，只写入有数据的[0-size)元素)
        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }

        // 序列化过程中有其他线程修改过数组，抛出异常
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    // 反序列化
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {

        // 先设置为空数组
        elementData = EMPTY_ELEMENTDATA;

        // 读取非静态属性、非 transient 属性
        s.defaultReadObject();

        // 读取 size ，不过忽略返回值，不用 (默认方法里已经读取出size并设置了)
        s.readInt(); // ignored

        if (size > 0) {
            // 数组扩容，确保内存足够
            ensureCapacityInternal(size);

            // 按顺序逐步读取所有元素并赋值
            Object[] a = elementData;
            for (int i=0; i<size; i++) {
                a[i] = s.readObject();
            }
        }
    }

    // 从指定下标返回list迭代器
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    // 从开始下标返回List迭代器
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    // 返回默认迭代器，只支持单向
    public Iterator<E> iterator() {
        return new Itr();
    }

    // 基础迭代器实现，没有使用抽象类的迭代器
    private class Itr implements Iterator<E> {
        int cursor;       // 游标，下一个要遍历元素的下标
        int lastRet = -1; // 上一个遍历元素的下标，-1代表还没有元素被遍历
        int expectedModCount = modCount;    // 变更记录数 检测是否存在并发修改，实现fast-fail机制

        public boolean hasNext() {
            // 判断是否还有下一个
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            // 并发检查
            checkForComodification();
            int i = cursor;
            if (i >= size)
                // 下标检查
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                // 刚才确定过，元素下标小于size，是存在的，此时必然是并发操作导致的
                throw new ConcurrentModificationException();
            // 更新游标
            cursor = i + 1;
            // 更新lastRet 并 返回元素
            return (E) elementData[lastRet = i];
        }

        // 移除刚才遍历的元素
        public void remove() {
            // 下标检查，必须先遍历过元素，才能再移除
            if (lastRet < 0)
                throw new IllegalStateException();
            // 并发检查
            checkForComodification();

            try {
                // 移除
                ArrayList.this.remove(lastRet);
                // 更新游标
                cursor = lastRet;
                // 更新记录下标
                lastRet = -1;
                // 更新变更记录数
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                // lastRet是之前访问过的元素，因此必然存在，如果下标越界，必然是其他线程操作了
                throw new ConcurrentModificationException();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size) {
                // 所有元素都遍历过了，直接返回
                // 注意这个坑，这个方法并非遍历所有元素，而是针对还没有遍历过的元素
                return;
            }
            final Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            // 针对还没遍历的元素，逐个遍历，交给consumer处理
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // 更新游标和记录下标
            cursor = i;
            lastRet = i - 1;
            // 并发检查
            checkForComodification();
        }

        // 并发检查
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }


    // 针对List迭代器，一样重写了父类的
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        // 前面是否还有元素
        public boolean hasPrevious() {
            return cursor != 0;
        }

        // 下一个要遍历元素的下标
        public int nextIndex() {
            return cursor;
        }

        // 上一个遍历过的元素的下标
        public int previousIndex() {
            return cursor - 1;
        }

        // 向前遍历
        @SuppressWarnings("unchecked")
        public E previous() {
            // 并发检查
            checkForComodification();
            // 下标检查
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            // 更新游标、下标、返回元素
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            // 下标检查
            if (lastRet < 0)
                throw new IllegalStateException();
            // 并发检查
            checkForComodification();

            try {
                // 修改刚刚遍历的元素的数据
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            // 并发检查
            checkForComodification();

            try {
                // 新增元素
                int i = cursor;
                ArrayList.this.add(i, e);
                // 更新游标等
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }


    // 数组切分
    public List<E> subList(int fromIndex, int toIndex) {
        // 下标检查
        subListRangeCheck(fromIndex, toIndex, size);
        // 切分
        return new SubList(this, 0, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }

    // 依旧没有采用父类，自己重写
    private class SubList extends AbstractList<E> implements RandomAccess {
        // 原数组引用
        private final AbstractList<E> parent;
        // 原数组开始下标
        private final int parentOffset;
        // 此数组开始下标
        private final int offset;
        // 此数组大小
        int size;

        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayList.this.modCount;
        }

        // 修改元素，会影响原数组
        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        // 获取下标元素
        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }

        // 获取大小
        public int size() {
            checkForComodification();
            return this.size;
        }

        // 新增，会影响原数组
        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        // 移除，会影响原数组
        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        // 范围移除，会影响原数组
        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            parent.removeRange(parentOffset + fromIndex,
                               parentOffset + toIndex);
            this.modCount = parent.modCount;
            this.size -= toIndex - fromIndex;
        }


        // 批量新增，会影响原数组
        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }


        // 批量新增，会影响原数组
        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;

            checkForComodification();
            parent.addAll(parentOffset + index, c);
            this.modCount = parent.modCount;
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = ArrayList.this.modCount;

                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                @SuppressWarnings("unchecked")
                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    final int size = SubList.this.size;
                    int i = cursor;
                    if (i >= size) {
                        return;
                    }
                    final Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length) {
                        throw new ConcurrentModificationException();
                    }
                    while (i != size && modCount == expectedModCount) {
                        consumer.accept((E) elementData[offset + (i++)]);
                    }
                    // update once at end of iteration to reduce heap write traffic
                    lastRet = cursor = i;
                    checkForComodification();
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        ArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkForComodification();

                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (expectedModCount != ArrayList.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

        public Spliterator<E> spliterator() {
            checkForComodification();
            return new ArrayListSpliterator<E>(ArrayList.this, offset,
                                               offset + this.size, this.modCount);
        }
    }

    // 遍历所有元素，交给action处理
    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        // 记录变更次数
        final int expectedModCount = modCount;
        @SuppressWarnings("unchecked")
        final E[] elementData = (E[]) this.elementData;
        final int size = this.size;
        // 逐个遍历处理
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            action.accept(elementData[i]);
        }
        // 并发检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    // 切分器
    @Override
    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator<>(this, 0, -1, 0);
    }

    // TODO 使用较少，后续更新
    /** Index-based split-by-two, lazily initialized Spliterator */
    static final class ArrayListSpliterator<E> implements Spliterator<E> {

        private final ArrayList<E> list;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index
        private int expectedModCount; // initialized when fence set

        /** Create new spliterator covering the given  range */
        ArrayListSpliterator(ArrayList<E> list, int origin, int fence,
                             int expectedModCount) {
            this.list = list; // OK if null unless traversed
            this.index = origin;
            this.fence = fence;
            this.expectedModCount = expectedModCount;
        }

        private int getFence() { // initialize fence to size on first use
            int hi; // (a specialized variant appears in method forEach)
            ArrayList<E> lst;
            if ((hi = fence) < 0) {
                if ((lst = list) == null)
                    hi = fence = 0;
                else {
                    expectedModCount = lst.modCount;
                    hi = fence = lst.size;
                }
            }
            return hi;
        }

        public ArrayListSpliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : // divide range in half unless too small
                new ArrayListSpliterator<E>(list, lo, index = mid,
                                            expectedModCount);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null)
                throw new NullPointerException();
            int hi = getFence(), i = index;
            if (i < hi) {
                index = i + 1;
                @SuppressWarnings("unchecked") E e = (E)list.elementData[i];
                action.accept(e);
                if (list.modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi, mc; // hoist accesses and checks from loop
            ArrayList<E> lst; Object[] a;
            if (action == null)
                throw new NullPointerException();
            if ((lst = list) != null && (a = lst.elementData) != null) {
                if ((hi = fence) < 0) {
                    mc = lst.modCount;
                    hi = lst.size;
                }
                else
                    mc = expectedModCount;
                if ((i = index) >= 0 && (index = hi) <= a.length) {
                    for (; i < hi; ++i) {
                        @SuppressWarnings("unchecked") E e = (E) a[i];
                        action.accept(e);
                    }
                    if (lst.modCount == mc)
                        return;
                }
            }
            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return (long) (getFence() - index);
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }
    }


    // 根据条件移除
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        // 记录移除数量
        int removeCount = 0;
        // 记录移除的所有下标
        final BitSet removeSet = new BitSet(size);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            @SuppressWarnings("unchecked")
            final E element = (E) elementData[i];
            if (filter.test(element)) {
                // 满足条件，记录一下，等会统一移除
                removeSet.set(i);
                removeCount++;
            }
        }
        // 并发检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        
        
        final boolean anyToRemove = removeCount > 0;
        if (anyToRemove) {
            // 存在要移除的元素
            final int newSize = size - removeCount;
            // 将不需要移除的元素全部从0开始往前覆盖
            for (int i=0, j=0; (i < size) && (j < newSize); i++, j++) {
                // 获取不需要移除的元素
                i = removeSet.nextClearBit(i);
                // 保存
                elementData[j] = elementData[i];
            }
            // 移除多余的元素
            for (int k=newSize; k < size; k++) {
                elementData[k] = null;  // Let gc do its work
            }
            // 更新size
            this.size = newSize;
            // 并发检查
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            modCount++;
        }

        return anyToRemove;
    }

    
    // 根据条件表达式替换
    @Override
    @SuppressWarnings("unchecked")
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        // 记录当前变更次数
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            // 逐个遍历替换
            elementData[i] = operator.apply((E) elementData[i]);
        }
        // 并发检查
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        final int expectedModCount = modCount;
        // 直接调用Arrays的sort方法进行排序
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        modCount++;
    }
}
```



