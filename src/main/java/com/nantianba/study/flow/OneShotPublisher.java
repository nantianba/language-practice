package com.nantianba.study.flow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

class OneShotPublisher implements Flow.Publisher<Boolean> {
    private final ExecutorService executor = ForkJoinPool.commonPool(); // daemon-based
    private boolean subscribed; // true after first subscribe
    public synchronized void subscribe(Flow.Subscriber<? super Boolean> subscriber) {
        if (subscribed)
            subscriber.onError(new IllegalStateException()); // only one allowed
        else {
            subscribed = true;
            subscriber.onSubscribe(new OneShotSubscription(subscriber, executor));
        }
    }
    static class OneShotSubscription implements Flow.Subscription {
        private final Flow.Subscriber<? super Boolean> subscriber;
        private final ExecutorService executor;
        private Future<?> future; // to allow cancellation
        private boolean completed;
        OneShotSubscription(Flow.Subscriber<? super Boolean> subscriber,
                            ExecutorService executor) {
            this.subscriber = subscriber;
            this.executor = executor;
        }
        public synchronized void request(long n) {
            if (n != 0 && !completed) {
                completed = true;
                if (n < 0) {
                    IllegalArgumentException ex = new IllegalArgumentException();
                    executor.execute(() -> subscriber.onError(ex));
                } else {
                    future = executor.submit(() -> {
                        subscriber.onNext(Boolean.TRUE);
                        subscriber.onComplete();
                    });
                }
            }
        }
        public synchronized void cancel() {
            completed = true;
            if (future != null) future.cancel(false);
        }
    }
}