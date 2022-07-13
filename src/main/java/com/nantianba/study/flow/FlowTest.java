package com.nantianba.study.flow;

import com.nantianba.study.util.Logger;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class FlowTest {
    public static void main(String[] args) {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        FlowProcessor processor = new FlowProcessor();
        Flow.Subscriber<String> subscriber = new FlowSubscriber();

        publisher.subscribe(processor);
        processor.subscribe(subscriber);

        //发布者开始发布数据
        for (int i = 0; i < 10; i++) {
            String msg = "hello flow: " + i;
            Logger.println("发布者发送数据" + i);
            publisher.submit(msg);
        }

        publisher.close();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
