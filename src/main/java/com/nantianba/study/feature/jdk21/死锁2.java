package com.nantianba.study.feature.jdk21;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

public class 死锁2 {

    public static void main(String args[]) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

        int i1 = 10000;
        Thread[] futures = new Thread[i1];
        AtomicLong atomicLong = new AtomicLong();
        Lock lock = new java.util.concurrent.locks.ReentrantLock();

        for (int i = 0; i < i1; i++) {
            int finalI1 = i;
            Thread thread = Thread.startVirtualThread(() -> {
                synchronized (死锁2.class) {
                    System.out.println("enter");
                    queue.poll();

                    try {
                        lock.lock();
                        if (finalI1 == 1000) {
                            try {
                                Thread.sleep(6000);
                            } catch (InterruptedException e) {
                                System.out.println("interrupt:" + Thread.currentThread().isInterrupted());
                                Thread.currentThread().interrupt();
                            }
                            dead();
                        }
                    } finally {
                        lock.unlock();
                        if (finalI1 == 1000) {
                            System.out.println("hhhhhh");
                        }
                    }
                    try {
                        死锁2.class.wait(10);
//                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                }

                atomicLong.incrementAndGet();
            });
            futures[i] = thread;
        }

        System.out.println(Thread.activeCount());
        Thread.sleep(3000);
        futures[1000].interrupt();
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

    private static void dead() {
        dead();
    }
}
