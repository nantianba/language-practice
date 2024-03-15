package com.nantianba.study.feature.jdk21;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class 虚拟线程死锁 {
    private static final ReentrantLock lockA = new ReentrantLock();
    private static final ReentrantLock lockB = new ReentrantLock();
    private static final ReentrantLock lockT = new ReentrantLock();

    private static final int CPUs = Runtime.getRuntime().availableProcessors();
    private static final CountDownLatch latch = new CountDownLatch(CPUs);
    private static final Object lock = new Object();

    private static final Object one = new Object();
    private static final Semaphore semaphore = new Semaphore(0);

    /**
     * Continuation栈存在native方法调用、外部函数调用或者当持有监视器或者等待监视器的时候，虚拟线程会Pin到平台线程，导致虚拟线程无法从平台线程卸载
     */
    public static void main(String[] args) throws Exception {
        boolean vt = true;

        async(() -> {
            synchronized (one) {
                while (true) {
                    try {
                        semaphore.acquire();

                        System.out.println("test compensate");

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, vt);

        lockA.lock();
        // VT 1
        Runnable runnable = () -> {
            lockB.lock();
            lockA.lock();
            lockA.unlock();
            lockB.unlock();
        };
        async(runnable, vt);


        Thread.sleep(1000);

        System.out.println("Starting pinned virtual threads");

        // VT 2..CPUs
        for (int i = 0; i < CPUs; i++) {
            Runnable runnable1 = () -> {
//                lockT.lock();
//                lockB.lock();
//                lockB.unlock();
//                System.out.println("Exiting synchronized block");
//                latch.countDown();
//                lockT.unlock();
                synchronized (lock) {
                    System.out.println("Entering synchronized block");
//                    try {
//                        lock.wait(100);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                    lockB.lock();
                    lockB.unlock();
                    System.out.println("Exiting synchronized block");
                    latch.countDown();
                }
            };
            async(runnable1, vt);
        }

        // VT1 先对B加了锁，随后VT2的同步块阻塞占用了全部线程，导致VT1无法继续执行解锁B
        lockA.unlock();
        System.out.println("Unlocked lockA");
        int i = 2;
        while (!latch.await(1, TimeUnit.SECONDS)) {
            System.out.println("blocking...");
            semaphore.release();

            for (Thread thread : vts) {
                System.out.println(thread);
            }
        }
        System.out.println("All threads have completed");

        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            System.out.println(thread.getName());
        }

        Thread.sleep(40000);

        System.out.println("40s later");

        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            System.out.println(thread.getName());
        }
    }

    static List<Thread> vts = new LinkedList<>();

    private static void async(Runnable runnable1, boolean vt) {
        if (vt) {
            Thread thread = Thread.ofVirtual().start(runnable1);
            vts.add(thread);
        } else {
            new Thread(runnable1).start();
        }
    }
}
