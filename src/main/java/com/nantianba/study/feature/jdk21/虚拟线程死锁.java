package com.nantianba.study.feature.jdk21;


import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class 虚拟线程死锁 {
    private static final ReentrantLock lockA = new ReentrantLock();
    private static final ReentrantLock lockB = new ReentrantLock();
    private static final ReentrantLock lockT = new ReentrantLock();

    private static final int CPUs = Runtime.getRuntime().availableProcessors()*5;
    private static final CountDownLatch latch = new CountDownLatch(CPUs);
    private static final Object lock = new Object();
    static DeadlockFixer deadlockFixer = new DeadlockFixer();


    /**
     * Continuation栈存在native方法调用、外部函数调用或者当持有监视器或者等待监视器的时候，虚拟线程会Pin到平台线程，导致虚拟线程无法从平台线程卸载
     */
    public static void main(String[] args) throws Exception {
        boolean vt = true;

        deadlockFixer.init();

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
        while (!latch.await(10, TimeUnit.MILLISECONDS)) {
            System.out.println("Deadlock detected");
            deadlockFixer.tryCompensateWorker();
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
            自定义虚拟线程Carrier.startVirtualThread(runnable1);
        } else {
            new Thread(runnable1).start();
        }
    }

    private static class DeadlockFixer {

        private final Object pinCarrier = new Object();
        private final Semaphore semaphore = new Semaphore(0);


        public void init() {
            String pa = System.getProperty("jdk.virtualThreadScheduler.parallelism");
            if (pa != null) {
//                System.setProperty("jdk.virtualThreadScheduler.parallelism", String.valueOf(Integer.parseInt(pa) + 1));
//                System.setProperty("jdk.virtualThreadScheduler.minRunnable", String.valueOf(Integer.parseInt(pa) + 1));
                System.setProperty("jdk.virtualThreadScheduler.minRunnable", String.valueOf(256));
//                System.setProperty("jdk.virtualThreadScheduler.minRunnable", String.valueOf(Integer.parseInt(pa)));
            } else {
//                System.setProperty("jdk.virtualThreadScheduler.parallelism", String.valueOf(Runtime.getRuntime().availableProcessors() + 1));
//                System.setProperty("jdk.virtualThreadScheduler.minRunnable", String.valueOf(Runtime.getRuntime().availableProcessors() + 1));
                System.setProperty("jdk.virtualThreadScheduler.minRunnable", String.valueOf(256));
//                System.setProperty("jdk.virtualThreadScheduler.minRunnable", String.valueOf(Runtime.getRuntime().availableProcessors()));
            }
            AtomicInteger i = new AtomicInteger();
            自定义虚拟线程Carrier.startVirtualThread(() -> {
                //始终占据一个Carrier线程
                synchronized (pinCarrier) {
                    while (true) {
                        try {
                            semaphore.acquire();

                            i.incrementAndGet();
                            //需要和VM参数配合，
//                            pinCarrier.wait(0, 1);
                            System.out.println("try saving deadlock");
                        } catch (InterruptedException e) {

                        }
                    }
                }
            });
        }

        public void tryCompensateWorker() {
            if (semaphore.hasQueuedThreads()) {
                semaphore.release();
            }
        }

    }
}
