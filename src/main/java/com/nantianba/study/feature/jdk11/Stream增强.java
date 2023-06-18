package com.nantianba.study.feature.jdk11;

import java.util.List;
import java.util.stream.Stream;

public class Stream增强 {
    public static void main(String[] args) {
        List<String> list = Stream.of("a", "b", "c")
                .takeWhile(s -> !s.equals("b"))
                .toList();

        System.out.println(list);

        List<String> list1 = Stream.of("a", "b", "c")
                .dropWhile(s -> !s.equals("b"))
                .toList();

        System.out.println(list1);

        List<Integer> list2 = Stream.iterate(0, i -> i < 10, i -> i + 3)
                .toList();

        System.out.println(list2);
    }
}
