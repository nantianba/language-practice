package com.nantianba.study.feature.jdk11;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HexFormat;

public class 字符串增强 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, UnsupportedEncodingException {
        String[] charsets = {"UTF-8", "GBK", "GB2312", "ISO-8859-1", "UTF-16", "UTF-16BE", "UTF-16LE", "UTF-32", "UTF-32BE", "UTF-32LE", "US-ASCII", "ASCII", "Unicode", "UnicodeBigUnmarked", "UnicodeLittleUnmarked"};
        for (String charset : charsets) {
            for (int i = 0; i < 3; i++) {
                byte[] bytes = "浚\uE0E2氚从呕萸".getBytes();
                System.out.println(charset + "\t" + new String(Arrays.copyOfRange(bytes,i,bytes.length), charset));
            }
        }
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

        System.out.println("反射修改后：" + str);

    }
}
