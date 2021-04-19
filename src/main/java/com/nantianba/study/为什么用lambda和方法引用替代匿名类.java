package com.nantianba.study;

import java.io.File;
import java.util.Arrays;

public class 为什么用lambda和方法引用替代匿名类 {
    private static final String THIS_CLASS_NAME = 为什么用lambda和方法引用替代匿名类.class.getSimpleName();

    public static void main(String[] args) {
        A a = new A();
        Runnable anonymousClass = new Runnable() {
            @Override
            public void run() {
                a.get();
            }
        };

        Runnable lambda = () -> a.get();
        Runnable methodReference = a::get;

        System.out.println("anonymousClass.getClass() = " + anonymousClass.getClass());
        System.out.println("lambda.getClass() = " + lambda.getClass());
        System.out.println("methodReference.getClass() = " + methodReference.getClass());

        var pathOfThisClass = A.class.getResource("").getPath();

        File classDir = new File(pathOfThisClass);

        System.out.println();
        System.out.println("All classes created by this class:");
        System.out.println();
        Arrays.stream(classDir.list())
                .filter(s -> createdByThisClass(s))
                .forEach(System.out::println);

    }

    private static boolean createdByThisClass(String s) {
        return s.startsWith(THIS_CLASS_NAME);
    }

    static class A {
        int get() {
            return 0;
        }
    }
}
