package com.nantianba.study.computer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class 锟斤拷 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        byte[] bytes = "锟斤拷".getBytes();

        for (byte b : bytes) {
            System.out.print(HexFormat.of().toHexDigits(b));
            System.out.print(" ");
        }

        System.out.println( );
        System.out.println(new String(bytes, "gbk"));
    }
}
