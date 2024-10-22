package com.nantianba.study.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeSet;

public class ScheduleReminder {
    public static void main(String[] args) throws InterruptedException, AWTException {
        String[] checkPoint = {"09:00", "09:30", "10:15", "11:00", "11:45",
                "13:30", "14:15", "15:00", "15:45", "16:30", "17:30", "18:00"};

        TreeSet<String> set = Arrays.stream(checkPoint).map(t -> STR."\{t}:00").collect(TreeSet::new, TreeSet::add, TreeSet::addAll);

        String deskDir = args[0];
        String lastHint = "";

        while (true) {
            tryDeleteLnkFile(deskDir);
            Date now = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateFormat.setLenient(false);

            String format = dateFormat.format(now);

            if (set.contains(format)) {
                show();
            } else {
                //下一次提醒时间
                String ceiling = set.ceiling(format);
                if (ceiling != null && !lastHint.equals(ceiling)) {
                    System.out.println(STR."下一次提醒时间:\{ceiling}");
                    lastHint = ceiling;
                }
            }

            Thread.sleep(1000);
        }
    }

    private static void show() throws AWTException {
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 显示窗口
        frame.setVisible(true);

        //1min后自动关闭窗口
        new Thread(() -> {
            try {
                Thread.sleep(6000);
                if (frame.isShowing()) {
                    frame.dispose();
                }
            } catch (Exception _) {
            }
        }).start();
    }

    private static void tryDeleteLnkFile(String deskDir) {
        String[] files = new File(deskDir)
                .list((_, name) -> name.endsWith(".lnk"));
        if (files != null) {
            for (String file : files) {
                new File(deskDir, file).delete();
            }
        }
    }
}