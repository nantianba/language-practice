package com.nantianba.study.flow;

import com.nantianba.study.util.Logger;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

class FlowSubscriber implements Flow.Subscriber<String> {
    private final String name;
    private Flow.Subscription subscription;
    @Getter
    private CompletableFuture<String> future = new CompletableFuture<>();

    FlowSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

        Logger.println(name + "建立订阅关系");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        Logger.println(name + "接收消息: " + item);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        Logger.println(name + "数据接收出现异常，error :" + throwable.getMessage());
        this.subscription.cancel();
        future.complete(name + " end error:" + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        Logger.println(name + "数据处理完成");
        future.complete(name + " end complete");
    }
}
