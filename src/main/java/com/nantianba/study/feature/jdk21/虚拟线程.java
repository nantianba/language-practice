package com.nantianba.study.feature.jdk21;

public class 虚拟线程 {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("虚拟线程");
        });
        thread.start();
        System.out.println("主线程");


    }
}
