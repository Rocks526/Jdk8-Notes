package com.rocks.test.current;

/**
 * 并发问题复现
 * @author lizhaoxuan
 * @date 2022/02/09
 */
public class CurrentBug {

    public static class Counter {
        // 初始值
        private volatile long count = 0;

        // 添加10000
        private void add10K(){
            int index = 0;
            while (index < 10000){
                count ++;
                index ++;
            }
        }
    }

    public static class Singleton {
        // volatile禁止重排序
        static volatile Singleton instance;

        static Singleton getInstance(){
            if (instance == null){
                synchronized (Singleton.class){
                    if (instance == null){
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }

    }

    public static long calc() throws InterruptedException {
        Counter counter = new Counter();
        // 创建两个线程各执行add
        Thread t1 = new Thread(counter::add10K);
        Thread t2 = new Thread(counter::add10K);
        t1.start();
        t2.start();
        // 等待执行结束
        t1.join();
        t2.join();
        return counter.count;
    }

    public static void singleton(){
        Thread t1 = new Thread(() -> System.out.println(Singleton.getInstance() == null ? "F" : "S"));
        Thread t2 = new Thread(() -> System.out.println(Singleton.getInstance() == null ? "F" : "S"));
        Thread t3 = new Thread(() -> System.out.println(Singleton.getInstance() == null ? "F" : "S"));
        t2.start();
        t1.start();
        t3.start();
    }

    public static void main(String[] args) throws InterruptedException {
        // System.out.println(calc());
        singleton();
    }


}
