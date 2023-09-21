package com.nantianba.study.feature.jdk21;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class 结构化并发 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        捕获第一个异常并关闭任务范围以中断未完成的线程
        var scope = new StructuredTaskScope.ShutdownOnFailure();
        try (scope) {
            StructuredTaskScope.Subtask<User> user = scope.fork(结构化并发::findUser);
            StructuredTaskScope.Subtask<Order> order = scope.fork(结构化并发::fetchOrder);
            scope.join();
            System.out.println(scope);
            scope.throwIfFailed();
            System.out.println(new Response(user.get(), order.get()));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        //捕获第一个结果并关闭任务范围以中断未完成的线程
        var scope2 = new StructuredTaskScope.ShutdownOnSuccess<String>();
        try (scope2) {
            scope2.fork(() -> {
                Thread.sleep(400);
                return "second";
            });
            scope2.fork(() -> {
                Thread.sleep(200);
                return "first";
            });
            scope2.join();

            System.out.println(scope2);

            System.out.println(scope2.result());
        }
    }

    record User(String name, Long id) {
    }

    record Order(String orderNo, Long id) {
    }

    record Response(User user, Order order) {
    }

    private static User findUser() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(Thread.currentThread());
        throw new UnsupportedOperationException("findUser");
    }

    private static Order fetchOrder() throws InterruptedException {
        Thread.sleep(50);
        System.out.println(Thread.currentThread());
        throw new UnsupportedOperationException("fetchOrder");
    }
}
