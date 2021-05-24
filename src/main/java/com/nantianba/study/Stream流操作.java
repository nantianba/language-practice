package com.nantianba.study;

import com.nantianba.study.util.ClassRunnerUtils;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Stream流操作 {

    public static void main(String[] args) {
        ClassRunnerUtils.run(Stream流操作.class);
    }

    private static void collect() {
        final Map<Boolean, List<Integer>> partitioningByMap = Stream.of(1, 2, 3).mapToInt(Integer::intValue)
                .boxed()
                .collect(partitioningBy(i -> i % 2 == 0));

        System.out.println("partitioningByMap = " + partitioningByMap);

        final Map<Boolean, Long> partitioningCountingByMap = Stream.of(1, 2, 3)
                .collect(partitioningBy(i -> i % 2 == 0, counting()));

        System.out.println("partitioningCountingByMap = " + partitioningCountingByMap);

        final String joinString = Stream.of(1, 2, 3).map(String::valueOf)
                .collect(joining(",", "[", "]"));

        System.out.println("joinString = " + joinString);

        final IntSummaryStatistics statistics = Stream.of(1, 2, 3, 4)
                .peek(i -> System.out.println("peek->" + i))
                .collect(summarizingInt(Integer::intValue));

        System.out.println("statistics = " + statistics);
    }

    public static void peek() {
        Stream.of(1, 2)
                .peek(i -> System.out.println("peek round 1->" + i))
                .forEach(i -> System.out.println("peek round 2=>" + i));
        ;
    }

    public static void flatMap() {
        Stream.of("1,2,3")
                .flatMap(s -> Stream.of(s.split(",")))
                .forEach(System.out::println);
    }

    public static void reduce() {
        final String s = Stream.generate(new Random()::nextInt)
                .limit(4)
                .map(String::valueOf)
                .reduce(String::concat).get();


        System.out.println("reduce string concat = " + s);

        System.out.println("sum = " + IntStream.range(0, 10).reduce(Integer::sum).getAsInt());
        System.out.println("min = " + IntStream.range(0, 10).reduce(Integer::min).getAsInt());
        System.out.println("max = " + IntStream.range(0, 10).reduce(Integer::max).getAsInt());
    }

    public static void toArray(){
        final int[] array = IntStream.range(0, 10)
                .skip(2)
                .limit(3)
                .toArray();

        System.out.println(Arrays.toString(array));
    }
}
