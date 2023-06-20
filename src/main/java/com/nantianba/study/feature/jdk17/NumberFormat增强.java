package com.nantianba.study.feature.jdk17;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormat增强 {
    public static void main(String[] args) {
        //中文环境
        String result = NumberFormat.getCompactNumberInstance(Locale.CHINESE, NumberFormat.Style.SHORT)
                .format(10);
        System.out.println(result);
        result = NumberFormat.getCompactNumberInstance(Locale.CHINESE, NumberFormat.Style.SHORT)
                .format(10000);
        System.out.println(result);
        result = NumberFormat.getCompactNumberInstance(Locale.CHINESE, NumberFormat.Style.SHORT)
                .format(1000030);
        System.out.println(result);
        result = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG)
                .format(1000330000);
        System.out.println(result);

        result = NumberFormat.getPercentInstance()
                .format(0.134);
        System.out.println(result);
        result = NumberFormat.getPercentInstance(Locale.US)
                .format(0.134d);
        System.out.println(result);


        result = NumberFormat.getIntegerInstance()
                .format(123.09);
        System.out.println(result);


        result = NumberFormat.getCurrencyInstance(Locale.CHINA)
                .format(0.13);
        System.out.println(result);
        result = NumberFormat.getCurrencyInstance(Locale.US)
                .format(0.13);
        System.out.println(result);
        result = NumberFormat.getCurrencyInstance(Locale.JAPAN)
                .format(0.13);
        System.out.println(result);
    }
}
