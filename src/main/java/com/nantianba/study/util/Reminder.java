package com.nantianba.study.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.FormatProcessor.FMT;

public class Reminder {
    public static void main(String[] args) throws InterruptedException, AWTException, ParseException {
        Calendar calendar = parse("yyyy-MM-dd HH:mm:ss", "2024-11-01 09:58:00");

        Date reminderTime = calendar.getTime();

        while (true) {
            Date now = new Date();
            if (now.after(reminderTime)) {
                break;
            }

            Thread.sleep(1000);
            System.out.println(FMT."提醒时间:%tT\{reminderTime},还有\{convertToText((reminderTime.getTime() - now.getTime()))}");
        }

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyPress(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_WINDOWS);

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

    private static Calendar parse(String format, String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault(Locale.Category.FORMAT));
        //2020-04-30 启用严格格式匹配，避免 2020-05-01->(yyyyMMdd)->2019-12-05这种情况出现
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            return calendar;
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误");
        }
    }

    /**
     * **秒
     * **分**秒
     * **小时**分**秒
     * **天**小时**分**秒
     */
    private static String convertToText(long l) {
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minute = (l % (60 * 60 * 1000)) / (60 * 1000);
        long second = (l % (60 * 1000)) / 1000;

        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minute > 0) {
            sb.append(minute).append("分");
        }
        sb.append(second).append("秒");

        return sb.toString();
    }

}