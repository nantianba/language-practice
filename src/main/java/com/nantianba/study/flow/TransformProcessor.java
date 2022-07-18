package com.nantianba.study.flow;

import com.nantianba.study.util.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

public class TransformProcessor<S, T> extends SubmissionPublisher<T> implements Flow.Processor<S, T> {
    final Function<? super S, ? extends T> function;
    Flow.Subscription subscription;

    TransformProcessor(Executor executor, int maxBufferCapacity, Function<? super S, ? extends T> function) {
        super(executor, maxBufferCapacity);
        this.function = function;
    }

    TransformProcessor(Function<? super S, ? extends T> function) {
        this.function = function;
    }

    public void onSubscribe(Flow.Subscription subscription) {
        (this.subscription = subscription).request(1);
        Logger.println("TransformProcessor 建立订阅关系");
    }

    public void onNext(S item) {
        submit(function.apply(item));
        subscription.request(1);
        Logger.println("TransformProcessor 接收数据: " + item);
    }

    public void onError(Throwable ex) {
        Logger.println("TransformProcessor 数据接收出现异常，error :" + ex.getMessage());
        this.closeExceptionally(ex);
        this.subscription.cancel();
    }

    public void onComplete() {
        Logger.println("TransformProcessor 数据处理完成");
        this.close();
    }
}

