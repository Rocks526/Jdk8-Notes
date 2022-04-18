package com.lizhaoxuan.util;

import com.lizhaoxuan.vo.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * LinkedList测试
 * 注：整个测试代码，完全是从ArrayList拷贝而来，相当于这两个类的API完全兼容
 * @author lizhaoxuan
 */
public class LinkedListTest {

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

        // ============ 队列测试 =====================
        queueTest();
        // ============ 栈测试 =====================
        stackTest();
    }

    private static void stackTest() {
        System.out.println("=================== [stackTest] =================================");
        LinkedList<Long> stack = new LinkedList<>();
        System.out.println("stack header = " + stack.peek() + ", stack = " + stack);    // stack header = null, stack = []
        stack.push(3L);
        stack.push(8L);
        System.out.println("stack header = " + stack.peek() + ", stack = " + stack);    // stack header = 8, stack = [8, 3]
        System.out.println("stack pop header = " + stack.pop() + ", stack = " + stack);     // stack pop header = 8, stack = [3]
    }

    private static void queueTest() {
        System.out.println("=================== [queueTest] =================================");
        LinkedList<Long> queue = new LinkedList<>();
        boolean offer = queue.offer(1L);
        System.out.println("add result = " + offer + ", list = " + queue); // add result = true, list = [1]
        boolean offer2 = queue.offer(5L);
        System.out.println("add result = " + offer2 + ", list = " + queue); // add result = true, list = [1, 5]
        Long peek = queue.peek();
        System.out.println("queue header = " + peek);       // queue header = 1
        Long pop = queue.poll();
        System.out.println("queue first element = " + pop + ", queue = " + queue);       // queue first element = 1, queue = [5]
        queue.addFirst(9L);
        queue.addLast(8L);
        System.out.println("queue = " + queue);      // queue = [9, 5, 8]
        Long last = queue.pollLast();
        System.out.println("queue last element = " + last + ", queue = " + queue);       // queue last element = 8, queue = [9, 5]
    }

    private static void subListTest() {
        System.out.println("=================== [subListTest] =================================");
        LinkedList<Long> list = new LinkedList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
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
        LinkedList<Long> list = new LinkedList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
        Object clone = list.clone();
        System.out.println(clone);      // [3, 8, 0, 8, 33, 7, 8]
        System.out.println(clone == list);  // false
        if (clone instanceof LinkedList){
            LinkedList<Long> cloneObj = (LinkedList<Long>) clone;
            cloneObj.removeAll(Collections.singletonList(8L));
            System.out.println(cloneObj);       // [3, 0, 33, 7]
            System.out.println(list);   // [3, 8, 0, 8, 33, 7, 8]
        }
        // 引用类型测试
        LinkedList<User> users = new LinkedList<>();
        users.add(User.builder().id(1L).name("张三").build());
        users.add(User.builder().id(3L).name("张三3").build());
        users.add(User.builder().id(2L).name("张三2").build());
        users.add(User.builder().id(4L).name("张三4").build());
        users.add(User.builder().id(9L).name("张三9").build());
        Object userClone = users.clone();
        System.out.println(userClone);      // [User(id=1, name=张三), User(id=3, name=张三3), User(id=2, name=张三2), User(id=4, name=张三4), User(id=9, name=张三9)]
        System.out.println(userClone == users);  // false
        if (userClone instanceof LinkedList){
            LinkedList<User> userCloneObj = (LinkedList<User>) userClone;
            userCloneObj.removeAll(Collections.singletonList(User.builder().id(1L).name("张三").build()));
            System.out.println(userCloneObj);       // [User(id=3, name=张三3), User(id=2, name=张三2), User(id=4, name=张三4), User(id=9, name=张三9)]
            System.out.println(users);   // [User(id=1, name=张三), User(id=3, name=张三3), User(id=2, name=张三2), User(id=4, name=张三4), User(id=9, name=张三9)]
            userCloneObj.forEach(u -> u.setName(u.getName()+"Modify!!!"));
            System.out.println(userCloneObj);       // [User(id=3, name=张三3Modify!!!), User(id=2, name=张三2Modify!!!), User(id=4, name=张三4Modify!!!), User(id=9, name=张三9Modify!!!)]
            System.out.println(users);   // [User(id=1, name=张三), User(id=3, name=张三3Modify!!!), User(id=2, name=张三2Modify!!!), User(id=4, name=张三4Modify!!!), User(id=9, name=张三9Modify!!!)]
        }
        // 结论：LinkedList是新生成的对象，但里面元素的引用是原有的引用
    }

    private static void serialTest() throws Exception {
        System.out.println("=================== [serialTest] =================================");
        LinkedList<Long> list = new LinkedList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
        // 序列化
        FileOutputStream fileOutputStream = new FileOutputStream("linkedlist-serial.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(list);
        // 反序列化
        FileInputStream fileInputStream = new FileInputStream("array-serial.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object readObject = objectInputStream.readObject();
        System.out.println(readObject);     // [3, 8, 0, 8, 33, 7, 8]
        if (readObject instanceof LinkedList){
            LinkedList<Long> v = (LinkedList<Long>) readObject;
            System.out.println(v == list);      // false
            System.out.println(v);      // [3, 8, 0, 8, 33, 7, 8]
        }
    }

    private static void iteratorTest() {
        System.out.println("=================== [iteratorTest] =================================");
        LinkedList<Long> list = new LinkedList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
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
        LinkedList<Long> list = new LinkedList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
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
        LinkedList<Long> list = new LinkedList<>(Arrays.asList(3L, 8L, 0L, 8L, 33L, 7L, 8L));
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
        LinkedList<Long> list = new LinkedList<>();
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
