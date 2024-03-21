package com.nantianba.study.feature.jdk11;

public class TimeOnSpin {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 200000000; i++) {
                Thread.onSpinWait();
            }
            System.out.println("done 2");
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 200000000; i++) {
                ;
            }
            System.out.println("done 1");
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
