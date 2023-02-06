package com.nantianba.study.thread;

public class ThreadLocalTest {
    public static void main(String[] args) {
        getAndSet();
    }
    public static void getAndSet(){
        System.getProperties().forEach((o, o2) -> {
            System.out.println(o+" = "+o2);
        });
        System.out.println("Runtime.getRuntime().availableProcessors() = " + Runtime.getRuntime().availableProcessors());
    }
}
