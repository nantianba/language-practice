package com.nantianba.study.feature.jdk21;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class 虚拟线程 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 方式一：直接启动虚拟线程，因为默认参数原因这样启动的虚拟线程名称为空字符串
        Thread virtualThread1 = Thread.startVirtualThread(() -> {
            Thread thread = Thread.currentThread();
            System.out.printf("线程名称:%s,是否虚拟线程:%s\n", thread.getName(), thread.isVirtual());
        });

// 方式二：Builder模式构建
        Thread virtualThread2 = Thread.ofVirtual().allowSetThreadLocals(false)
                .name("VirtualWorker-", 0)
                .inheritInheritableThreadLocals(false)
                .unstarted(() -> {
                    Thread thread = Thread.currentThread();
                    System.out.printf("线程名称:%s,是否虚拟线程:%s\n", thread.getName(), thread.isVirtual());
                });
        virtualThread2.start();

// 方式三：Factory模式构建
        ThreadFactory factory = Thread.ofVirtual().allowSetThreadLocals(false)
                .name("VirtualFactoryWorker-", 0)
                .inheritInheritableThreadLocals(false)
                .factory();

        Thread virtualWorker = factory.newThread(() -> {
            Thread thread = Thread.currentThread();
            System.out.printf("线程名称:%s,是否虚拟线程:%s\n", thread.getName(), thread.isVirtual());
        });
        virtualWorker.start();
// 可以构建"虚拟线程池"
        ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory);
        executorService.submit(() -> {
            Thread thread = Thread.currentThread();
            System.out.printf("线程名称:%s,是否虚拟线程:%s\n", thread.getName(), thread.isVirtual());
        }).get();
    }
}
