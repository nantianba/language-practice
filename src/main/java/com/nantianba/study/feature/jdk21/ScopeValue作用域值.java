package com.nantianba.study.feature.jdk21;

import jdk.incubator.concurrent.ScopedValue;

public class ScopeValue作用域值 {
    public static void main(String[] args) throws InterruptedException {
        ScopedValue<String> commonScope = ScopedValue.newInstance();

        ScopedValue.where(commonScope, "common", () -> {
            ScopedValue.where(commonScope, "common2", () -> {
                System.out.println(Thread.currentThread() + "\t" + commonScope.get());
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread() + "\t" + commonScope.get());
        });

        Thread.sleep(200);
        System.out.println(Thread.currentThread() + "\t" + commonScope.orElse(null));
    }
}
