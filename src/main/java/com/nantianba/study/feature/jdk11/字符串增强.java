package com.nantianba.study.feature.jdk11;

import java.lang.reflect.Field;
import java.util.HexFormat;

public class 字符串增强 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        System.out.println("*".repeat(20));
        System.out.println("  me  ".strip());
        System.out.println("  me  ".stripLeading());
        System.out.println("  me  ".stripTrailing());


        System.out.println("*".repeat(20));


        String str = """
                孙悟空
                """;

        System.out.println("反射修改前：" + str);

//        --add-opens java.base/java.lang=ALL-UNNAMED
        Field field = String.class.getDeclaredField("value");
        field.setAccessible(true);
        byte[] value = (byte[]) field.get(str);

        System.out.println(HexFormat.of().formatHex(value));

        value[0] = 0x7a;

        System.out.println("反射修改后："+str);

    }
}
