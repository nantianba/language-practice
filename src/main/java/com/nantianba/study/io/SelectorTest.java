package com.nantianba.study.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorTest {
    private static final Semaphore semaphore = new Semaphore(0);
    private static Selector selector;

    private static Queue<String> messages = new ConcurrentLinkedQueue<>();

    private static void init() {
        try {
            selector = Selector.open();

            new Thread(() -> {
                while (true) {
                    try {
                        if (!selector.isOpen()) {
                            break;
                        }
                        //notation select will always block if there is no keys in its regist events
                        final int select = selector.select();
                        if (select == 0) {
                            continue;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                        SelectionKey selectedKey = iterator.next();
                        final Exchanger exchanger = (Exchanger) selectedKey.attachment();

                        if (exchanger.isServer) {
                            if (selectedKey.isAcceptable()) {
                                try {
                                    final SocketChannel channel = ((ServerSocketChannel) selectedKey.channel()).accept();

                                    System.out.println("server accept");
                                    final ByteBuffer buffer = ByteBuffer.allocate(100);
                                    buffer.put("client received".getBytes());
                                    buffer.flip();

                                    while (buffer.hasRemaining()) {
                                        channel.write(buffer);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (selectedKey.isReadable()) {
                                final SocketChannel channel = (SocketChannel) selectedKey.channel();

                                try {
                                    System.out.println("client read");

                                    final ByteBuffer buffer = ByteBuffer.allocate(100);
                                    while ((channel.read(buffer) > 0)) {
                                    }
                                    buffer.flip();

                                    final String message = new String(buffer.array(), 0, buffer.limit());

                                    messages.offer(message + ":" + exchanger.number);
                                    exchanger.notifyDone();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        iterator.remove();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        init();

        new Server().run();

        final int clientNum = 10;
        for (int i = 0; i < clientNum; i++) {
            new Client().run();
        }

        semaphore.acquire(clientNum);

        final Thread[] list = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
        Thread.currentThread().getThreadGroup()
                .enumerate(list);

        for (Thread thread : list) {
            System.out.println(thread.getName());
        }

        selector.close();

        System.out.println("messages = " + messages);
    }

    static class Exchanger {
        static Exchanger server() {
            final Exchanger exchanger = new Exchanger();
            exchanger.isServer = true;
            return exchanger;
        }

        static Exchanger client(int number) {
            final Exchanger exchanger = new Exchanger();
            exchanger.isServer = false;
            exchanger.number = number;

            return exchanger;
        }

        boolean isServer;

        int number;

        void notifyDone() {
            semaphore.release(1);
        }
    }

    static class Client {
        static AtomicInteger seed = new AtomicInteger(1);

        void run() throws IOException {
            final SocketChannel channel = SocketChannel.open();

            channel.connect(new InetSocketAddress("127.0.0.1", 8082));
            channel.configureBlocking(false);
            System.out.println("connect " + seed.get());

            channel.register(selector.wakeup(), SelectionKey.OP_READ, Exchanger.client(seed.getAndIncrement()));
        }
    }

    static class Server {
        void run() throws IOException {
            final ServerSocketChannel channel = ServerSocketChannel.open();
            channel.bind(new InetSocketAddress(8082));
            channel.configureBlocking(false);

            System.out.println("start listening");
            channel.register(selector.wakeup(), SelectionKey.OP_ACCEPT, Exchanger.server());
        }
    }
}