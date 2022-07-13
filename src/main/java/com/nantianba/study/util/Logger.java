package com.nantianba.study.util;


import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class Logger {
    private static Stopwatch stopwatch = Stopwatch.createStarted();

    public static void println(String content) {
        System.out.println(Thread.currentThread().getName() + "\t" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "\t" + content);
    }

    public static void flush() {
        System.out.flush();
    }
}
