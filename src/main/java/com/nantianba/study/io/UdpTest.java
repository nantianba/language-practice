package com.nantianba.study.io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpTest {
    public static void main(String[] args) throws InterruptedException {
        Thread.startVirtualThread(UDPReceiver::init );

        Thread.sleep(1000);

        UDPSender.start();

        Thread.sleep(1000);
    }

    public static class UDPSender {
        public static void start() {
            try {
                // 创建数据报套接字
                DatagramSocket socket = new DatagramSocket();

                // 要发送的消息和目标主机地址
                String message = "Hello, UDP!";
                InetAddress address = InetAddress.getByName("127.0.0.1");
                int port = 8888;

                // 创建发送的数据报
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);

                // 发送数据报
                socket.send(sendPacket);

                // 关闭套接字
                socket.close();

                System.out.println("Send Socket closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class UDPReceiver {
        public static void init() {
            try {
                DatagramSocket socket = new DatagramSocket(8888);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                socket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String source=receivePacket.getSocketAddress().toString();
                System.out.println("Received message: " + message);
                System.out.println("Received source: " + source);

                socket.close();

                System.out.println("Receive Socket closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
