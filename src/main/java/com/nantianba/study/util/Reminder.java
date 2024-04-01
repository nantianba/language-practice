package com.nantianba.study.util;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class Reminder {
    public static void main(String[] args) throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 59);
//        calendar.set(Calendar.SECOND, 0);

        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 50);
        Date reminderTime = calendar.getTime();

        while (true) {
            Date now = new Date();
            if (now.after(reminderTime)) {
                break;
            }

            Thread.sleep(1000);
            System.out.println(STR."当前时间：\{now}，提醒时间：\{reminderTime}");
        }

        // 创建一个 JFrame 对象
        JFrame frame = new JFrame("提醒时间快到了");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        //屏幕居中
        frame.setLocationRelativeTo(null);
        //设置窗口内容为：请打开手机
        JLabel label = new JLabel("请打开手机");
        //label 居中
        label.setHorizontalAlignment(SwingConstants.CENTER);

        frame.add(label);

        // 设置窗口总是在当前所有窗口之前显示
        frame.setAlwaysOnTop(true);

        // 显示窗口
        frame.setVisible(true);
    }

}