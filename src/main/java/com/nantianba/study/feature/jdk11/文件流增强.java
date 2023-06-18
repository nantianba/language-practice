package com.nantianba.study.feature.jdk11;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class 文件流增强 {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.out.println("请输入内容,最后Ctrl+D结束：");
        //将会直到文件流结束才会结束阻塞
        System.in.transferTo(outputStream);
        System.out.println("输入结束");

        Thread.sleep(100);
        System.out.println("输出结果：");
        String ans = outputStream.toString();
        System.out.println("[" + ans + "]");

        System.out.println("*".repeat(20));

        Path tempFile= Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "hello world");
        String readString = Files.readString(tempFile);
        System.out.println(readString);


    }
}
