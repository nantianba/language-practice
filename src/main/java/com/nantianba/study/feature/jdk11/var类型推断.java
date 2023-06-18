package com.nantianba.study.feature.jdk11;

public class var类型推断 {
    public static void main(String[] args) {
        var a = 1;
        var b = "hello";
        var c = 1.0;
        var d = new Object();
        var e = new int[]{1, 2, 3};
        var f = "hello";
        var g= ((CharSequence) f);

        for (var i : e) {
            System.out.print("");
        }

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(e);
        System.out.println(f);
        System.out.println(g);
    }
}
