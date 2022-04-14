package com.rocks.test.lang;

public class StringTest {

    public static void main(String[] args) {
        // new String方式
        String s1 = new String("Rocks");
        String s2 = new String("Rocks");
        // 堆里两个对象 引用不相等 这两个对象的引用都指向常量池的Rocks
        System.out.println(s1 == s2);  // false

        // intern方式  都是从字符串常量池取 因此引用相等
        String s3 = "Rocks526".intern();
        String s4 = "Rocks526".intern();
        System.out.println(s3 == s4);   // true

        // 直接声明方式  直接放入常量池 也相等
        String s5 = "Rocks666";
        String s6 = "Rocks666";
        System.out.println(s5 == s6);  // true


        // format
        String format = String.format("名字:%s, 年龄:%d", "Rocks", 22);
        System.out.println(format);

    }
}
