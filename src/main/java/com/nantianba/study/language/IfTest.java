package com.nantianba.study.language;

public class IfTest {
    public static void main(String[] args) {
        Object o = 10 % 2 == 0 ? doA() : doB();

        System.out.println(o);
    }

    private static Object doB() {
        System.out.println("doB");

        return 2;
    }

    private static Object doA() {
        System.out.println("DoA");

        return 1;
    }
}
