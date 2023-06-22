package com.nantianba.study.feature.jdk17;

public class String增强 {
    public static void main(String[] args) {
        //trandform JDK12
        String string = "hello".transform(s -> s + " world")
                .transform(String::toUpperCase);

        System.out.println(string);

        "Java\nPython\nC".lines()
                .forEach(System.out::println);

        //缩进
        System.out.println("Java\nPython\nC".indent(3));
    }
}
