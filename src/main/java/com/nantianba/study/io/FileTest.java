package com.nantianba.study.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;

public class FileTest {
    public static void main(String[] args) throws IOException {
        randomAccessFile();
    }

    private static void randomAccessFile() throws IOException {
        final URL url = FileTest.class.getClassLoader().getResource("TestFile.txt");
        final RandomAccessFile file = new RandomAccessFile(url.getPath(), "rw");

        System.out.println("file.length() = " + file.length());
        file.seek(8);

        final ByteBuffer buffer = ByteBuffer.allocate(100);

        file.getChannel().read(buffer);

        buffer.flip();

        final String s = new String(buffer.array(), 0, buffer.limit());

        buffer.clear();


        System.out.println("s = " + s);
    }
}
