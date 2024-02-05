package com.nantianba.study.feature.jdk21;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class 虚拟线程死锁 {
    private static final ReentrantLock lockA = new ReentrantLock();
    private static final ReentrantLock lockB = new ReentrantLock();
    private static final ReentrantLock lockT = new ReentrantLock();

    private static final int CPUs = Runtime.getRuntime().availableProcessors();
    private static final CountDownLatch latch = new CountDownLatch(CPUs);

    /**
     * Continuation栈存在native方法调用、外部函数调用或者当持有监视器或者等待监视器的时候，虚拟线程会Pin到平台线程，导致虚拟线程无法从平台线程卸载
     */
    public static void main(String[] args) throws Exception {
        boolean vt = true;

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

        // VT 2..CPUs+1
        for (int i = 0; i < CPUs; i++) {
            Runnable runnable1 = () -> {
                lockT.lock();
                lockB.lock();
                lockB.unlock();
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Exiting synchronized block");
                latch.countDown();
                lockT.unlock();
//                synchronized (虚拟线程死锁.class) {
//                    lockB.lock();
//                    lockB.unlock();
//                    System.out.println("Exiting synchronized block");
//                    latch.countDown();
//                }
            };
            async(runnable1, vt);
        }

        // VT1 先对B加了锁，随后VT2的同步块阻塞占用了全部线程，导致VT1无法继续执行解锁B
        lockA.unlock();
        System.out.println("Unlocked lockA");
        latch.await();
    }

    private static void async(Runnable runnable1, boolean vt) {
        if (vt) {
            Thread.startVirtualThread(runnable1);
        } else {
            new Thread(runnable1).start();
        }
    }
}
