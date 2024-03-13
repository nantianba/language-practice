package com.nantianba.study.feature.jdk21;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class 死锁2 {

    public static void main(String args[]) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

        int i1 = 10000;
        Thread[] futures = new Thread[i1];
        AtomicLong atomicLong = new AtomicLong();

        for (int i = 0; i < i1; i++) {
            int finalI = i;
            Thread thread = Thread.startVirtualThread(() -> {
                synchronized (死锁2.class) {
                    System.out.println("enter");
                    queue.poll();
                    try {
//                        死锁2.class.wait(10);
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                }

                atomicLong.incrementAndGet();
            });
            futures[i] = thread;
        }

        System.out.println(Thread.activeCount());
        for (int i = 0; i < i1; i++) {
            futures[i].join();
        }

        //打印进程内所有线程名称
        Thread.getAllStackTraces().keySet().forEach(thread -> {
            System.out.println(thread.getName());
        });

        System.out.println(Thread.activeCount());
        System.out.println(atomicLong.get());
        System.exit(0);
    }
}
