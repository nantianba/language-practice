package com.nantianba.study.thread;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.nio.channels.Pipe;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadState {
    final static Object syncLock = new Object();

    public static void main(String[] args) throws InterruptedException, IOException {
        final CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean lock = new AtomicBoolean();

        final Pipe pipe = Pipe.open();

        final Pipe.SinkChannel sink = pipe.sink();
        final Pipe.SourceChannel source = pipe.source();



        final Thread thread = new Thread(() -> {

            synchronized (syncLock) {
                System.out.println(Thread.currentThread().getState());

                System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int i = 0;
            while (lock.get()) {
                i++;
            }
        });
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                synchronized (syncLock) {
                    thread.start();
                    Thread.sleep(1000);
                }
                Thread.sleep(2000);
                latch.countDown();
            } catch (InterruptedException e) {

            }
        }).start();


        new Thread(() -> {
            try {
                Thread.sleep(5000);
                lock.set(false);
            } catch (InterruptedException e) {
            }
        }).start();
        for (int i = 0; i < 12; i++) {
            System.out.println(thread.getState());

            System.out.println(Arrays.toString(thread.getStackTrace()));

            Thread.sleep(500);
        }
    }
}
