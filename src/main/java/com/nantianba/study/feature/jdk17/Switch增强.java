package com.nantianba.study.feature.jdk17;

import java.util.Random;

public class Switch增强 {
    public static void main(String[] args) {
        var lang = "java";

        var simple = switch (lang) {
            case "java" -> "j";
            case "go" -> "g";
            default -> "<non>";
        };

        System.out.println("simple = " + simple);

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

        System.out.println("complex = " + complex);
    }
}
