package com.nantianba.study.flow;

import com.nantianba.study.util.ClassRunnerUtils;
import com.nantianba.study.util.Logger;

import java.util.concurrent.*;

public class FlowTest {
    public static void main(String[] args) {
        ClassRunnerUtils.run(FlowTest.class);
    }

    private static void testSlowConsumer() throws InterruptedException, ExecutionException {
        PeriodicPublisher<String> publisher = new PeriodicPublisher<>(ForkJoinPool.commonPool(), 3, () -> {
            String message = "message" + System.currentTimeMillis() ;
            Logger.println("PeriodicPublisher:" + message);
            return message;
        }, 100, TimeUnit.MILLISECONDS);

        SlowSubscriber subscriber = new SlowSubscriber("Slow Consumer", 100);

        publisher.subscribe(subscriber);

        Thread.sleep(1500);
        publisher.close();

        subscriber.getFuture().get();
    }

    private static void testTooSlowConsumer() throws InterruptedException, ExecutionException {
        PeriodicPublisher<String> publisher = new PeriodicPublisher<>(ForkJoinPool.commonPool(), 3, () -> {
            String message = "message" + System.currentTimeMillis() ;
            Logger.println("PeriodicPublisher:" + message);
            return message;
        }, 10, TimeUnit.MILLISECONDS);

        SlowSubscriber subscriber = new SlowSubscriber("Slow Consumer", 100);

        publisher.subscribe(subscriber);

        Thread.sleep(1500);
        publisher.close();

        subscriber.getFuture().get();
    }

    private static void testTimedPublisher() throws InterruptedException, ExecutionException {
        PeriodicPublisher<String> publisher = new PeriodicPublisher<>(() -> {
            String message = "message" + System.currentTimeMillis() / 1000;
            Logger.println("PeriodicPublisher:" + message);
            return message;
        }, 1, TimeUnit.SECONDS);

        FlowSubscriber subscriber = new FlowSubscriber("阅者");

        Flow.Processor<String, String> processor = new TransformProcessor<>(item -> item + "<Transformed>");
        publisher.subscribe(processor);
        processor.subscribe(subscriber);

        Thread.sleep(5500);
        publisher.close();

        subscriber.getFuture().get();
    }

    private static void testError() throws ExecutionException, InterruptedException {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        Flow.Processor<String, String> processor = new TransformProcessor<>(item -> item + "<Transformed>");
        FlowSubscriber subscriber = new FlowSubscriber("阅者");

        publisher.subscribe(processor);
        processor.subscribe(subscriber);

        for (int i = 0; i < 5; i++) {
            String msg = "hello flow: " + i;
            Logger.println("发布者发送数据" + i);
            publisher.submit(msg);
        }

        publisher.closeExceptionally(new RuntimeException("urgent error"));

        subscriber.getFuture().get();
    }

    private static void testComplete() throws ExecutionException, InterruptedException {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        Flow.Processor<String, String> processor = new TransformProcessor<>(item -> item + "<Transformed>");
        FlowSubscriber subscriber = new FlowSubscriber("订阅者");

        publisher.subscribe(processor);
        processor.subscribe(subscriber);

        for (int i = 0; i < 5; i++) {
            String msg = "hello flow: " + i;
            Logger.println("发布者发送数据" + i);
            publisher.submit(msg);
        }

        Logger.println("publisher.estimateMaximumLag() = " + publisher.estimateMaximumLag());
        publisher.close();

        subscriber.getFuture().get();
    }

    private static void testIsReplay() throws ExecutionException, InterruptedException {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        Flow.Processor<String, String> processor = new TransformProcessor<>(item -> item + "<Transformed>");
        FlowSubscriber subscriber = new FlowSubscriber("订阅者1");
        FlowSubscriber subscriber2 = new FlowSubscriber("订阅者2");

        publisher.subscribe(processor);
        processor.subscribe(subscriber);

        for (int i = 0; i < 2; i++) {
            String msg = "hello flow: " + i;
            Logger.println("发布者发送数据" + i);
            publisher.submit(msg);
        }

        processor.subscribe(subscriber2);

        for (int i = 2; i < 3; i++) {
            String msg = "hello flow: " + i;
            Logger.println("发布者发送数据" + i);
            publisher.submit(msg);
        }

        Logger.println("publisher.estimateMaximumLag() = " + publisher.estimateMaximumLag());
        publisher.close();

        subscriber.getFuture().get();
        subscriber2.getFuture().get();
    }
}
