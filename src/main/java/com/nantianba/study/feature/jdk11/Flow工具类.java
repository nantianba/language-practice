package com.nantianba.study.feature.jdk11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.IntStream;

public class Flow工具类 {
    public static void main(String[] args) {

        CompletableFuture<Void> subTask;
        CompletableFuture<Void> subTask2;
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        try (publisher) {
            //订阅

            subTask = publisher
                    .consume(i -> System.out.println("订阅者1" + Thread.currentThread().getName() + "\t" + i));
            subTask2 = publisher
                    .consume(i -> System.out.println("订阅者2" + Thread.currentThread().getName() + "\t" + i));

            IntStream.rangeClosed(1, 3)
                    //发布
                    .forEach(publisher::submit);
        }
       CompletableFuture.allOf(subTask, subTask2).join();
    }
}
