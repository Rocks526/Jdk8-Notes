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

