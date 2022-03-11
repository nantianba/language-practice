package com.nantianba.study.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;

public class FileTest {
    public static void main(String[] args) throws IOException {
        randomAccessFile();
        transferFrom();
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

    private static void transferFrom() throws IOException {
        final URL url = FileTest.class.getClassLoader().getResource("TestFile.txt");
        final RandomAccessFile file = new RandomAccessFile(url.getPath(), "rw");

        final File tempFile = File.createTempFile("testTransfer1", ".txt");
        tempFile.deleteOnExit();
        final File tempFile1 = File.createTempFile("testTransfer2", ".txt");
        tempFile1.deleteOnExit();

        RandomAccessFile file1 = new RandomAccessFile(tempFile.getPath(), "rw");
        final RandomAccessFile file2 = new RandomAccessFile(tempFile1.getPath(), "rw");

        file.getChannel().transferTo(0, file.length(), file1.getChannel());
        file1.close();
        file1= new RandomAccessFile(tempFile.getPath(), "rw");
        file2.getChannel().transferFrom(file1.getChannel(), 0, file.length());

        System.out.println("file1.getChannel().size() = " + file1.getChannel().size());
        System.out.println("file2.getChannel().size() = " + file2.getChannel().size());

        final ByteBuffer allocate = ByteBuffer.allocate((int) file1.length());
        file1.close();
        file1= new RandomAccessFile(tempFile.getPath(), "rw");
        file1.getChannel().read(allocate);

        allocate.flip();
        final byte[] array = allocate.array();
        System.out.println("new String(allocate.array()) = " + new String(array));
    }
}
