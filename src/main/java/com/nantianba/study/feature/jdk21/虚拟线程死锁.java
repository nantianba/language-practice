package com.nantianba.study.feature.jdk21;

import com.nantianba.study.feature.jdk21.safe_vt.VirtualThreads;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class 虚拟线程死锁 {
    private static final ReentrantLock lockA = new ReentrantLock();
    private static final ReentrantLock lockB = new ReentrantLock();
    private static final ReentrantLock lockT = new ReentrantLock();

    private static final int CPUs = Runtime.getRuntime().availableProcessors()*5;
    private static final CountDownLatch latch = new CountDownLatch(CPUs);
    private static final Object lock = new Object();


    /**
     * Continuation栈存在native方法调用、外部函数调用或者当持有监视器或者等待监视器的时候，虚拟线程会Pin到平台线程，导致虚拟线程无法从平台线程卸载
     */
    public static void main(String[] args) throws Exception {
        //打印jvm版本
        System.out.println(System.getProperty("java.version"));

        boolean vt = true;

//        deadlockFixer.init();

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
        while (!latch.await(4, TimeUnit.SECONDS)) {
            System.err.println("Deadlock detected");
        }
        System.out.println("All threads have completed");
        Set<Thread> threads1 = Thread.getAllStackTraces().keySet();
        System.out.println(threads1.size());
        System.out.println("************");
        System.out.println(Thread.getAllStackTraces().keySet().stream().map(Thread::getName).filter(name -> name.contains("Fork")).sorted().collect(Collectors.joining("\n")));

        Thread.sleep(40000);

        System.out.println("40s later");
        Set<Thread> threads2 = Thread.getAllStackTraces().keySet();
        System.out.println(threads2.size());

        Thread.sleep(1000);
        System.out.println("*********");

        Set<Thread> threads3 = Thread.getAllStackTraces().keySet();
        System.out.println(threads3.size());

        System.out.println(threads1.removeAll(threads3));
        System.out.println(threads1);

        System.out.println();
    }

    private static void async(Runnable runnable1, boolean vt) {
        if (vt) {
//            Thread.startVirtualThread(runnable1);
            VirtualThreads.start(runnable1);
        } else {
            new Thread(runnable1).start();
        }
    }

}
