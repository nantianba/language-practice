package com.nantianba.study.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ScheduleReminder {
    public static void main(String[] args) throws InterruptedException, AWTException {
        String[] checkPoint = {"09:00", "09:30", "10:15", "11:00", "11:45",
                "13:30", "14:15", "15:00", "15:45", "16:30", "17:30", "18:00"};

        while (true) {
            Date now = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateFormat.setLenient(false);

            String format = dateFormat.format(now);
            System.out.println(format);

            if (Arrays.stream(checkPoint).anyMatch(t -> (STR."\{t}:00").equals(format))) {
                System.out.println("Time to take a break");
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_WINDOWS);
                robot.keyPress(KeyEvent.VK_D);
                robot.keyRelease(KeyEvent.VK_D);
                robot.keyRelease(KeyEvent.VK_WINDOWS);

                // 创建一个 JFrame 对象
                JFrame frame = new JFrame("提醒时间到了");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 200);
                //屏幕居中
                frame.setLocationRelativeTo(null);
                //设置窗口内容为：请打开手机
                JLabel label = new JLabel("CLOSE");
                //label 居中
                label.setHorizontalAlignment(SwingConstants.CENTER);

                frame.add(label);

                // 设置窗口总是在当前所有窗口之前显示
                frame.setAlwaysOnTop(true);

                // 显示窗口
                frame.setVisible(true);
            }

            Thread.sleep(1000);
        }
    }

}