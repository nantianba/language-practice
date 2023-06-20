package com.nantianba.study.language;

import java.time.OffsetDateTime;
import java.util.Optional;

public class IfTest {
    public static void main(String[] args) {
        Object o = 10 % 2 == 0 ? doA() : doB();
        System.out.println("o = " + o);

        System.out.println("*********************************************");
        Object o1 = Optional.empty().map(i -> doA()).or(() -> Optional.of(doB())).get();
        System.out.println("o1 = " + o1);
        System.out.println("*********************************************");
        Object o2 = Optional.of(1).map(i -> doA()).orElse(doB());
        System.out.println("o2 = " + o2);
        System.out.println("*********************************************");
        Object o3 = Optional.of(1).map(i -> doA()).orElseGet(IfTest::doB);

        System.out.println("o3 = " + o3);

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
