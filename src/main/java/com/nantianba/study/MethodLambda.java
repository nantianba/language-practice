package com.nantianba.study;

import java.util.function.Function;

public class MethodLambda {
    public static void main(String[] args) {
        int a = new A().getA();

        System.out.println("a = " + a);
        for(int i = 0; i < 2; i++) {

            final Function<A, Integer> getA = A::getA;
            final Function<A, Integer> getA1 = A::getA;

            System.out.println("getA == getA1 = " + (getA == getA1));
            System.out.println("getA.equals(getA1) = " + getA.equals(getA1));

            System.out.println(getA);
            System.out.println(getA1);

            System.out.println("getA.getClass().equals(getA1.getClass()) = " + getA.getClass().equals(getA1.getClass()));
        }
    }

    static class A {
       private int getA() {
            return 0;
        }
    }
}
