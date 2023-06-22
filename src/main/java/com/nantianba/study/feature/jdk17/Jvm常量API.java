package com.nantianba.study.feature.jdk17;

import java.lang.constant.ClassDesc;

public class Jvm常量API {
    public static void main(String[] args) {
        //JVM常量API
        "java".describeConstable()
                .ifPresent(System.out::println);

        String.class.describeConstable()
                .ifPresent(System.out::println);

        Integer.class.describeConstable()
                .ifPresent(System.out::println);

        Integer.class.describeConstable()
                .map(ClassDesc::descriptorString)
                .ifPresent(System.out::println);



    }
}
