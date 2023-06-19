package com.nantianba.study.feature.jdk17;

public class 文本块 {
    public static void main(String[] args) {
        var block = """
                lang: java
                version: 13
                dbname: mysql
                ip: 192.168.140.2
                usr: root
                pwd: 1000
                """;

        //JDK11中的字符串lines
        System.out.println("block.lines().count() = " + block.lines().count());

        var block2 = """
        lang: java\
        version: 13\
        dbname: mysql\
        ip: 192.168.140.2\
        usr: root\
        pwd: 1000
        """;
        System.out.println("block2.lines().count() = " + block2.lines().count());

    }
}
