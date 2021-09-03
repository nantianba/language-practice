package com.nantianba.study;

import java.util.Random;

public class 条件判断 {
    public static void main(String[] args) {
        int i = new Random().nextBoolean()
                ? method1()
                : method2();

        System.out.println(i);
    }

    private static int method2() {
        System.out.println("method2");
        return 2;
    }

    private static int method1() {
        System.out.println("method1");

        return 1;
    }
}
