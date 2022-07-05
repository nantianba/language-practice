package com.nantianba.study;

import com.nantianba.study.util.ClassRunnerUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericInfo {
    final static A<String> c = new A<>();

    public static void main(String[] args) throws NoSuchFieldException {
        ClassRunnerUtils.run(GenericInfo.class);
    }

    private static void genericInfoErase() {
        final A<String> b = new A<>();

        for (Type genericInterface : b.getClass().getGenericInterfaces()) {
            System.out.println("genericInterface = " + genericInterface);
        }

        System.out.println("b.getClass().getGenericSuperclass() = " + b.getClass().getGenericSuperclass());
    }

    private static void subClassKeepSignature() {
        final A<String> a = new A<String>() {
        };

        for (Type genericInterface : a.getClass().getGenericInterfaces()) {
            System.out.println("genericInterface = " + genericInterface);
        }

        System.out.println("a.getClass().getGenericSuperclass() = " + a.getClass().getGenericSuperclass());

        final ParameterizedType genericSuperclass = (ParameterizedType) a.getClass().getGenericSuperclass();

        System.out.println("genericSuperclass = " + genericSuperclass.getActualTypeArguments()[0].getTypeName());
    }

    private static void fieldKeepSignature() throws NoSuchFieldException {
        System.out.println("c.getClass().getGenericSuperclass() = " + c.getClass().getGenericSuperclass());

        Field cf = GenericInfo.class.getDeclaredField("c");
        System.out.println("cf.getGenericType() = " + cf.getGenericType());
    }
}

class A<T> {
    T object;

    public T getObject() {
        return object;
    }
}
