package com.nantianba.study.feature.jdk11;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFuture增强 {
    public static void main(String[] args) throws InterruptedException, IOException {
//        orTimeout
        CompletableFuture<Void> targetFuture = CompletableFuture
                .runAsync(() -> {
                    try {
                        Thread.sleep(new Random().nextInt(2000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "\t" + "hello");
                });

        targetFuture
                .orTimeout(1, TimeUnit.SECONDS)
                .handle((aVoid, throwable) -> {
                    if (throwable != null) {
                        System.out.println(Thread.currentThread().getName() + "\t" + "超时了");
                    } else {
                        System.out.println(Thread.currentThread().getName() + "\t" + "正常结束");
                    }
                    return null;
                });

        Thread.sleep(3000);
    }
}
