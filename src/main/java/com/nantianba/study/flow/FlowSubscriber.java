package com.nantianba.study.flow;

import com.nantianba.study.util.Logger;

import java.util.Objects;
import java.util.concurrent.Flow;

class FlowSubscriber implements Flow.Subscriber<String> {
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        Logger.println("Subscriber 建立订阅关系");
        //发布订阅关系
        this.subscription = subscription;
        //请求一个数据
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {

        Logger.println("订阅者接收消息: " + item);
        if (Objects.equals(item, "stop")) {
            //不再接收数据，调用cancel
            this.subscription.cancel();
        } else {
            //接收数据后 再请求一个数据
            this.subscription.request(1);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Logger.println("订阅者数据接收出现异常，error :" + throwable.getMessage());
        this.subscription.cancel();
    }

    @Override
    public void onComplete() {
        Logger.println("订阅者数据处理完成");
    }
}
