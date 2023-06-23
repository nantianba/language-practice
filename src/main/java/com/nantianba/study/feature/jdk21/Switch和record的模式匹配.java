package com.nantianba.study.feature.jdk21;

public class Switch和record的模式匹配 {
    public static void main(String[] args) {
        Second<I, I> second = new Second<>(new D(), new C());
        checkSwitch(second);
        second = new Second<>(new C(), new D());
        checkSwitch(second);
    }

    private static void checkSwitch(Second<I, I> second) {
        switch (second) {
            case Second<I, I>(C c, D d) -> System.out.printf("c => %s,d => %s\n", c, d);
            case Second<I, I>(D d, C c) -> System.out.printf("d => %s,c => %s\n", d, c);
            default -> System.out.println("default");
        }
    }

    record Second<T extends I, U extends I>(T t, U u) {
    }

    sealed interface I permits C, D {
    }

    static final class C implements I {
    }

    static final class D implements I {
    }

}
