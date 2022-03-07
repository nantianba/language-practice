package com.nantianba.study;

import java.util.Arrays;

public class DeamonThread {
    public static void main(String[] args) throws InterruptedException {
        final Thread thread1 = new Thread(() -> {
            System.out.println("default daemon=" + Thread.currentThread().isDaemon());
        });

        final Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        final Thread thread = new Thread(() -> {
            System.out.println("1 = " + 1);

            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();

                System.out.println("threadGroup.getName() = " + threadGroup.getName());

                System.out.println("threadGroup.getParent().getName() = " + threadGroup.getParent().getName());

                final Thread[] list = new Thread[threadGroup.activeCount()];
                threadGroup.enumerate(list);

                System.out.println("list = " + Arrays.toString(list));
            }
        });

        thread.setDaemon(true);

        thread.start();

        Thread.sleep(1000);
    }
}
