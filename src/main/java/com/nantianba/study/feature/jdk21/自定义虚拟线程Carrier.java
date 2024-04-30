package com.nantianba.study.feature.jdk21;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class 自定义虚拟线程Carrier {
    private static final ExecutorService virtualWorkerPool1;
    private static final AtomicLong counter = new AtomicLong(0);

    static {
        try {
            ExecutorService carrier = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 256, 30L, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("VtCarrier-" + counter.getAndIncrement());

                    return thread;
                }
            });

            Thread.Builder.OfVirtual ofVirtual = Thread.ofVirtual();
            Class<?> klass = Class.forName("java.lang.ThreadBuilders$VirtualThreadBuilder");
            VarHandle varHandle = MethodHandles.privateLookupIn(klass, MethodHandles.lookup()).findVarHandle(klass, "scheduler", Executor.class);
            varHandle.set(ofVirtual, carrier);

            ThreadFactory factory = ofVirtual.factory();
            virtualWorkerPool1 = Executors.newThreadPerTaskExecutor(factory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, InterruptedException {
        startVirtualThread(() -> {
            System.out.println(Thread.currentThread());

            try {
                Thread.sleep(Duration.ofSeconds(2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread());

        });

        Thread.sleep(Duration.ofSeconds(5));

        for (Thread s : Thread.getAllStackTraces().keySet()) {
            System.out.println(s);
        }

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(0,1);
            startVirtualThread(()->{
                System.out.println(Thread.currentThread());
            });
        }

        for (Thread s : Thread.getAllStackTraces().keySet()) {
            System.out.println(s);
        }
    }

    public static void startVirtualThread(Runnable runnable) {
        virtualWorkerPool1.submit(runnable);
    }

}
