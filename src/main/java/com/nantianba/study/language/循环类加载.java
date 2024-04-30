package com.nantianba.study.language;

public class 循环类加载 {
    public static void main(String[] args) {
        A a = new A();

        System.out.println("A.b = " + A.b);
    }

    static class A {
        private static B b = new B();
    }

    static class B {
        private static A a = new A();
    }



}
