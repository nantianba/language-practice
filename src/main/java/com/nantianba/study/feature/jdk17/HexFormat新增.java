package com.nantianba.study.feature.jdk17;

import java.util.HexFormat;

public class HexFormat新增 {
    public static void main(String[] args) {
        System.out.println("HexFormat.fromHexDigit('F') = " + HexFormat.fromHexDigit('F'));
        System.out.println("HexFormat.fromHexDigits(\"FF\") = " + HexFormat.fromHexDigits("FF"));
        System.out.println("HexFormat.of().toHexDigits(255) = " + HexFormat.of().toHexDigits(255));
        System.out.println("HexFormat.of().withUpperCase().toHexDigits(255) = " + HexFormat.of().withUpperCase().toHexDigits(255));

    }
}
