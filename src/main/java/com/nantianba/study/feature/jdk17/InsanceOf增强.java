package com.nantianba.study.feature.jdk17;

import java.util.Random;

public class InsanceOf增强 {
    public static void main(String[] args) {
        Object o = new Random().nextInt() % 2 == 0 ? "java16" : 1000.0d;

        if (o instanceof String s) {
            System.out.println(s.length());
        } else if (o instanceof Double d) {
            System.out.println(d.intValue());
        }
    }
}
