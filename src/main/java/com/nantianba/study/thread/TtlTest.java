package com.nantianba.study.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@SuppressWarnings("AlibabaThreadPoolCreation")
public class TtlTest {
    private static final ThreadLocal<Long> LOCAL = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
//        test0 和 test1 两者的线程包裹不一样 可以观察stack的区别
        test0();
        System.out.println("*".repeat(20));
        test1();
        System.out.println("*".repeat(20));
        teset2();
        System.out.println("*".repeat(20));
        test3();
        System.out.println("*".repeat(20));
        test4();
        System.out.println("*".repeat(20));
        test5();
        System.out.println("*".repeat(20));

        poolSafe3.shutdown();
        poolSafe4.shutdown();
    }

    private static void test0() throws InterruptedException {
        CompletableFuture.runAsync(() -> {
            A a = new A();
            LOCAL.set(a.key);
            log(a.key + " " + LOCAL.get());
            CompletableFuture.runAsync(() -> {
                Arrays.stream(Thread.currentThread().getStackTrace()).map(StackTraceElement::toString).forEach(System.out::println);
                log(a.key + " " + LOCAL.get());
            }, poolSafe);
        }, poolSafe);

        Thread.sleep(1000);

        log(LOCAL.get());
    }

    private static void test1() throws InterruptedException {
        CompletableFuture.runAsync(() -> {
            A a = new A();
            LOCAL.set(a.key);
            log(a.key + " " + LOCAL.get());
            CompletableFuture.runAsync(() -> {
                Arrays.stream(Thread.currentThread().getStackTrace()).map(StackTraceElement::toString).forEach(System.out::println);
                log(a.key + " " + LOCAL.get());
            });
        });

        Thread.sleep(1000);

        log(LOCAL.get());
    }

    private static void teset2() throws InterruptedException {
        CompletableFuture.runAsync(() -> {
            A a = new A();

            LOCAL.set(a.key);
            IntStream.range(0, 10).
                    parallel()
                    .forEach(i -> {
                        log(a.key + " " + LOCAL.get());
                    });
        });

        Thread.sleep(1000);
    }

    static ForkJoinPool pool = new ForkJoinPool(10);

    /**
     * 加javaagent和不加结果完全不同
     */
    private static void test3() throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                A a = new A();
                LOCAL.set(a.key);
                CompletableFuture.runAsync(() -> {
                    CompletableFuture.delayedExecutor(1000 - finalI, TimeUnit.MILLISECONDS).execute(() -> {
                        boolean ok = a.key == LOCAL.get();
                        if (ok) {
                            log(a.key + " " + LOCAL.get() + "\t");
                        } else {
                            error(a.key + " " + LOCAL.get() + "\t");
                        }
                    });
                }, pool);
            });
        }

        Thread.sleep(3000);
    }

    static Executor poolSafe = TtlExecutors.getTtlExecutor(new ForkJoinPool(10));
    static Executor poolSafe2 = TtlExecutors.getTtlExecutor(new ForkJoinPool(10));

    private static void test4() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1000);

        for (int i = 0; i < 1000; i++) {
            latch.countDown();
            CompletableFuture.runAsync(() -> {
                A a = new A();
                LOCAL.set(a.key);

                CompletableFuture.runAsync(() -> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    boolean ok = a.key == LOCAL.get();
                    if (ok) {
                        log(a.key + " " + LOCAL.get() + "\t");
                    } else {
                        error(a.key + " " + LOCAL.get() + "\t");
                    }
                }, poolSafe2);

            }, poolSafe);
        }

        Thread.sleep(3000);

    }

    static ScheduledExecutorService poolSafe3 = TtlExecutors.getTtlScheduledExecutorService(new ScheduledThreadPoolExecutor(10));
    static ExecutorService poolSafe4 = TtlExecutors.getTtlExecutorService(new ForkJoinPool(10));

    private static void test5() throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            int finalI = i;

            CompletableFuture.runAsync(() -> {
                A a = new A();
                LOCAL.set(a.key);

                CompletableFuture.runAsync(() -> {
                    poolSafe3.schedule((() -> {
                        boolean ok = a.key == LOCAL.get();
                        if (ok) {
                            log(a.key + " " + LOCAL.get() + "\t");
                        } else {
                            error(a.key + " " + LOCAL.get() + "\t");
                        }
                    }), 1000 - finalI, TimeUnit.MILLISECONDS);
                }, poolSafe4);
            }, poolSafe4);
        }

        Thread.sleep(3000);

    }

    private static void log(Object x) {
        System.out.println(Thread.currentThread().getName() + "\t" + x);
    }

    private static void error(Object x) {
        System.err.println(Thread.currentThread().getName() + "\t" + x);
    }

    private static class A {
        private static final AtomicLong counter = new AtomicLong(1);
        private final long key = counter.incrementAndGet();
    }
}
