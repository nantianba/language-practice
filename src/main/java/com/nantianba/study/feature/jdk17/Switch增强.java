package com.nantianba.study.feature.jdk17;

import java.util.Random;

public class Switch增强 {
    public static void main(String[] args) {
        var lang = "java";

        //尖尖角
        var simple = switch (lang) {
            case "java" -> "j";
            case "go" -> "g";
            default -> "<non>";
        };

        System.out.println("simple = " + simple);

        //复杂尖尖角  yield
        lang = new Random().nextBoolean() ? "java" : "groovy";
        var complex = switch (lang) {
            case "java", "scala" -> "jvm";
            case "c", "cpp" -> "c";
            case "go" -> "g";
            default -> {
                if (lang.equals("groovy")) {
                    yield "<unknown>";
                } else {
                    yield "<non>";
                }
            }
        };

        //可以推断类型的switch表达式
        System.out.println("complex = " + complex);
        Object[] objects = {1, 2L, 3.0d, "4"};
        for (Object o : objects) {
            String s = switch (o) {
                case Integer i -> String.format("int %d", i);
                case Long l -> String.format("long %d", l);
                case Double d -> String.format("double %f", d);
                case String str -> String.format("String %s", str);
                default -> o.toString();
            };

            System.out.println(s);
        }
    }
}
