package com.lizhaoxuan.lang;

/**
 * String类测试
 * @author lizhaoxuan
 */
public class StringTest {

    public static void main(String[] args) {
        // String的创建
        createTest();
    }

    private static void createTest() {
        String v = "This is a String!";
        String v2 = new String("This is a String!");
        String v3 = v + v2;
        String v4 = v + "!!";
        String v5 = v + "!!";
        System.out.println(v == v2); // false
        System.out.println(v4 == v5);   // false
    }

}
