package com.nantianba.study.flow;

import com.nantianba.study.util.Logger;
import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

class FlowSubscriber implements Flow.Subscriber<String> {
    private Flow.Subscription subscription;
    @Getter
    private CompletableFuture<String> future = new CompletableFuture<>();

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        Logger.println("Subscriber 建立订阅关系");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        Logger.println("订阅者接收消息: " + item);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        Logger.println("订阅者数据接收出现异常，error :" + throwable.getMessage());
        this.subscription.cancel();
        future.complete("订阅者 end error:" + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        Logger.println("订阅者数据处理完成");
        future.complete("订阅者 end complete");
    }
}
