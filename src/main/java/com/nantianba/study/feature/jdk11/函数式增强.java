package com.nantianba.study.feature.jdk11;

import java.util.function.Function;

public class 函数式增强 {
    public static void main(String[] args) {
        Function<String, Integer> string2int = (var a) -> a.length() + 1;
        System.out.println(string2int.apply("abc"));
    }
}
