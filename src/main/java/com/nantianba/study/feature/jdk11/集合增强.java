package com.nantianba.study.feature.jdk11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class 集合增强 {
    public static void main(String[] args) {
        var list = List.of("a", "b", "c");
        var copy = List.copyOf(list);
        System.out.println(list);
        System.out.println(copy);

        var set = Set.of("a", "b", "c");
        var copySet = Set.copyOf(set);
        System.out.println(set);
        System.out.println(copySet);

        var map = Map.of("a", 1, "b", 2, "c", 3);
        Map<String, Integer> copyMap = Map.copyOf(map);
        System.out.println(map);
        System.out.println(copyMap);

        var entries = Map.ofEntries(
                Map.entry("a", 1),
                Map.entry("b", 2),
                Map.entry("c", 3)
        );

        System.out.println(entries);

//不可变
        var list1 = List.of("Java", "Python", "C");
        var copy1 = List.copyOf(list1);
        System.out.println(list1 == copy1); // true

        var list2 = new ArrayList<String>();
        var copy2 = List.copyOf(list2);
        System.out.println(list2 == copy2); // false
    }
}
