package com.nantianba.study;

import java.io.File;
import java.util.Arrays;

public class 为什么用lambda和方法引用替代匿名类 {
    private static final String THIS_CLASS_NAME = 为什么用lambda和方法引用替代匿名类.class.getSimpleName();

    public static void main(String[] args) {
        A a = new A();
        new Thread(new Runnable() {
            @Override
            public void run() {
                a.getByAnonymousClass();
            }
        });
        new Thread(() -> a.getByLambda());
        new Thread(a::getByMethidRef);

        var resourcePath = A.class.getResource("").getPath();

        File classDir = new File(resourcePath);

        System.out.println("All classes created by this class:");
        Arrays.stream(classDir.list())
                .filter(s -> s.startsWith(THIS_CLASS_NAME))
                .forEach(System.out::println);

    }

    static class A {
        int getByMethidRef() {
            return 1;
        }

        int getByLambda() {
            return 2;
        }

        int getByAnonymousClass() {
            return 3;
        }
    }
}
