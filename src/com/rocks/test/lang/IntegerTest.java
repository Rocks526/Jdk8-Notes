package com.rocks.test.lang;

public class IntegerTest {

    public static void main(String[] args) {

        int reverse = Integer.reverse(8);
        System.out.println(reverse); // 268435456

        // valueOf会使用缓存 -127到128
        Integer integer = Integer.valueOf("22");
        Integer integer1 = Integer.valueOf("22");
        System.out.println(integer == integer1); // true

        // parserInt不会使用缓存 但返回int都相等 如果写Integer 自动转包装类 也是相等的
        int i = Integer.parseInt("23");
        int i1 = Integer.parseInt("23");
        System.out.println(i == i1);

    }
}
