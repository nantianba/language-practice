package com.nantianba.study;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericInfo {
    public static void main(String[] args) {
        final A<String> a = new A<String>() {
        };

        for (Type genericInterface : a.getClass().getGenericInterfaces()) {
            System.out.println("genericInterface = " + genericInterface);
        }

        System.out.println("a.getClass().getGenericSuperclass() = " + a.getClass().getGenericSuperclass());

        final ParameterizedType genericSuperclass = (ParameterizedType) a.getClass().getGenericSuperclass();

        System.out.println("genericSuperclass = " + genericSuperclass.getActualTypeArguments()[0].getTypeName());
        final A<String> b = new A<>();

        for (Type genericInterface : b.getClass().getGenericInterfaces()) {
            System.out.println("genericInterface = " + genericInterface);
        }

        System.out.println("b.getClass().getGenericSuperclass() = " + b.getClass().getGenericSuperclass());


    }
}

class A<T> {
    T object;

    public T getObject() {
        return object;
    }
}
