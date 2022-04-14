package com.lizhaoxuan.util;

import java.util.*;

/**
 * ArrayList测试
 * @author lizhaoxuan
 */
public class ArrayListTest {

    public static void main(String[] args) {
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

    }

    private static void cloneTest() {

    }

    private static void serialTest() {

    }

    private static void iteratorTest() {

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
