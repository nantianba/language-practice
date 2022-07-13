package com.nantianba.study.flow;

import com.nantianba.study.util.ClassRunnerUtils;
import com.nantianba.study.util.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.SubmissionPublisher;

public class FlowTest {
    public static void main(String[] args) {
        ClassRunnerUtils.run(FlowTest.class);
    }

    private static void testError() throws ExecutionException, InterruptedException {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        FlowProcessor processor = new FlowProcessor();
        FlowSubscriber subscriber = new FlowSubscriber();

        publisher.subscribe(processor);
        processor.subscribe(subscriber);

        //发布者开始发布数据
        for (int i = 0; i < 10; i++) {
            String msg = "hello flow: " + i;
            Logger.println("发布者发送数据" + i);
            publisher.submit(msg);
        }

        publisher.closeExceptionally(new RuntimeException("urgent error"));

        subscriber.getFuture().get();
    }

    private static void testComplete() throws ExecutionException, InterruptedException {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        FlowProcessor processor = new FlowProcessor();
        FlowSubscriber subscriber = new FlowSubscriber();

        publisher.subscribe(processor);
        processor.subscribe(subscriber);

        //发布者开始发布数据
        for (int i = 0; i < 10; i++) {
            String msg = "hello flow: " + i;
            Logger.println("发布者发送数据" + i);
            publisher.submit(msg);
        }

        publisher.close();

        subscriber.getFuture().get();
    }
}
