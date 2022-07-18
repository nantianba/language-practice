package com.nantianba.study.flow;

import com.nantianba.study.util.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class PeriodicPublisher<T> extends SubmissionPublisher<T> {
    static AtomicInteger idSeed = new AtomicInteger();

    final ScheduledFuture<?> periodicTask;
    final ScheduledExecutorService scheduler;

    PeriodicPublisher(Executor executor, int maxBufferCapacity, Supplier<? extends T> supplier, long period, TimeUnit unit) {
        super(executor, maxBufferCapacity);
        scheduler = getScheduler();

        periodicTask = scheduler.scheduleAtFixedRate(() -> submit(supplier.get()), 0, period, unit);
    }

    PeriodicPublisher(Supplier<? extends T> supplier, long period, TimeUnit unit) {
        super();
        scheduler = getScheduler();
        periodicTask = scheduler.scheduleAtFixedRate(() -> submit(supplier.get()), 0, period, unit);
    }

    private static ScheduledThreadPoolExecutor getScheduler() {
        return new ScheduledThreadPoolExecutor(1, new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);

                thread.setName("PeriodicPublisher-" + idSeed.incrementAndGet());
                thread.setDaemon(true);

                return thread;
            }
        });
    }

    public void close() {
        Logger.println("PeriodicPublisher close");
        periodicTask.cancel(false);
        scheduler.shutdown();
        super.close();
    }
}
