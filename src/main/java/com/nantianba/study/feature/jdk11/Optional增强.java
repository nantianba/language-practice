package com.nantianba.study.feature.jdk11;

import java.util.Optional;

public class Optional增强 {
    public static void main(String[] args) {
        //ifPresentOrElse
        Optional.empty()
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("empty"));

        String s1 = Optional.empty()
                .map(s -> "value")
                .orElseThrow();


    }
}
