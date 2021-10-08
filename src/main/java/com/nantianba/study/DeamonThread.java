package com.nantianba.study;

public class DeamonThread {
    public static void main(String[] args) throws InterruptedException {
        final Thread thread1 = new Thread(() -> {
            System.out.println("default daemon=" + Thread.currentThread().isDaemon());
        });

        thread1.start();

        final Thread thread = new Thread(() -> {
            System.out.println("1 = " + 1);

            while (true) {
                ;
            }
        });

        thread.setDaemon(true);

        thread.start();

        Thread.sleep(1000);
    }
}
