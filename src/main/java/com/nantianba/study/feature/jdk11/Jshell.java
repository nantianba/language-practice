package com.nantianba.study.feature.jdk11;

import java.io.IOException;

public class Jshell {
    public static void main(String[] args) throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"jshell"}, new String[]{"file.encoding=UTF-8"});

        new Thread(() -> {
            System.out.println("开始，已重定向jshell进程输出到控制台");
            try {
                process.getInputStream().transferTo(System.out);
                System.out.println("jshell进程结束");

            } catch (IOException e) {
            }
        }).start();

        new Thread(() -> {
            byte[] buf = new byte[1024];

            try {
                int read;
                do {
                    read = System.in.read(buf);
                    if (read > 0) {
                        process.getOutputStream().write(buf, 0, read);
                        process.getOutputStream().flush();
                    }
                }
                while (read != -1);

                System.out.println("控制台输入结束");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
