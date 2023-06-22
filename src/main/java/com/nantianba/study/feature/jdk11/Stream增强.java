package com.nantianba.study.feature.jdk11;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stream增强 {
    public static void main(String[] args) {
        List<String> list = Stream.of("a", "b", "c")
                .takeWhile(s -> !s.equals("b"))
                //tolist是jdk16的新特性
                .toList();

        System.out.println(list);

        List<String> list1 = Stream.of("a", "b", "c")
                .dropWhile(s -> !s.equals("b"))
                .toList();

        System.out.println(list1);

        List<Integer> list2 = Stream.iterate(0, i -> i < 10, i -> i + 3)
                .toList();

        System.out.println(list2);

        String s = Stream.iterate(0, n -> n + 3)
                .limit(10)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        System.out.println(s);

        String s1 = Stream.iterate(0, n -> n <= 27, n -> n + 3)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        System.out.println(s1);
    }
}
