package com.nantianba.study.feature.jdk21;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

public class 大量虚拟线程 {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100000; i++) {
            Thread.startVirtualThread(() -> {
                try {
                    System.out.println(Thread.currentThread().toString());
                    System.out.println(Thread.currentThread().getThreadGroup().getName());
                    Thread.sleep(Duration.ofMinutes(2));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Thread.sleep(Duration.ofSeconds(10));



        System.out.println(Thread.activeCount());

        long totalledMemory = Runtime.getRuntime().totalMemory();
        String memory = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT).format(totalledMemory);
        long freedMemory = Runtime.getRuntime().freeMemory();
        String freeMemory = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT).format(freedMemory);
        System.out.println(memory);
        System.out.println(freeMemory);

        Thread.sleep(Duration.ofMinutes(10));
    }
}
