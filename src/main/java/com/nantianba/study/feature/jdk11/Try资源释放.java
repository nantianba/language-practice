package com.nantianba.study.feature.jdk11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Try资源释放 {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = Try资源释放.class.getResourceAsStream("/TestFile.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try (reader; inputStream) {
            System.out.println(reader.readLine());
        }
    }
}
