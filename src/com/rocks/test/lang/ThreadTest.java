package com.rocks.test.lang;

/**
 * 线程测试
 */
public class ThreadTest {

    // 自定义线程实现方式一
    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("MyThread running!");
        }
    }

    // 自定义线程实现方式二
    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("MyRunnable running!");
        }
    }

    // 创建 && 启动线程
    public static void main(String[] args) {
        new MyThread().start();
        new Thread(new MyRunnable()).start();
    }

}
