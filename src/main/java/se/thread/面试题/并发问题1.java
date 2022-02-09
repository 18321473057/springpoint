package se.thread.面试题;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: yangcs
 * @Date: 2022/1/18 8:43
 * @Description:
 */
public class 并发问题1 {

    public static void main(String[] args) {
        final Counter counter = new Counter();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                counter.inc();
            }).start();
        }


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(counter);



    }

}


class Counter {
//    private volatile int count = 0;
    private AtomicInteger count = new AtomicInteger(0);

    public  void inc() {
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        count++;
        count.incrementAndGet();
    }

    @Override
    public String toString() {
        return "Counter{" +
                "count=" + count +
                '}';
    }
}