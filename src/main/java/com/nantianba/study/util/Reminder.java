package com.nantianba.study.util;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Reminder {
    public static void main(String[] args) {
        Reminder reminder = new Reminder();
        reminder.remindAt("提醒时间快到了", 10, 12);
    }

    private Timer timer;

    public Reminder() {
        timer = new Timer();
    }

    public void remindAt(String reminderText, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Date reminderTime = calendar.getTime();

        timer.schedule(new RemindTask(reminderText), reminderTime);
    }
    class RemindTask extends TimerTask {

        private String reminderText;

        public RemindTask(String reminderText) {
            this.reminderText = reminderText;
        }
        public void run() {
            JOptionPane.showMessageDialog(null, reminderText);
            System.exit(0); // 如果你想在弹出窗口后退出程序，可以添加此行代码
        }

    }
}