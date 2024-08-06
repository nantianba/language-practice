package com.nantianba.study.jvm;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@Threads(2)
public class TryCatchJmh {
    public static void main(String[] args) throws Exception {
        Options opts = new OptionsBuilder()
                .include("TryCatchJmh")
                .build();
        new Runner(opts).run();
    }

    @Benchmark
    public long shift(Blackhole blackhole) {
        long t = 455565655225562L;
        long a = 0;
        for (int i = 0; i < 1000; i++) {
            a = t >> 30;
        }

        blackhole.consume(a);

        return a;
    }

    @Benchmark
    public long shiftTryCatch(Blackhole blackhole) {
        try {
            long t = 455565655225562L;
            long a = 0;
            for (int i = 0; i < 1000; i++) {
                a = t >> 30;
            }

            blackhole.consume(a);
            return a;
        } catch (Throwable e) {
            blackhole.consume(e);
            return 0;
        }
    }

    @Benchmark
    public long shift2() {
        long t = 455565655225562L;
        long a = 0;
        for (int i = 0; i < 1000; i++) {
            a = t >> 30;
        }
        return a;
    }

    @Benchmark
    public long shiftTryCatch2() {
        try {
            long t = 455565655225562L;
            long a = 0;
            for (int i = 0; i < 1000; i++) {
                a = t >> 30;
            }

            return a;
        } catch (Throwable e) {
            return 0;
        }
    }

    @Benchmark
    public long div(Blackhole blackhole) {
        long t = 455565655225562L;
        long a = 0;
        for (int i = 0; i < 1000; i++) {
            a = t / 1024 / 1024 / 1024;
        }

        blackhole.consume(a);
        return a;
    }

    @Benchmark
    public long divTryCatch(Blackhole blackhole) {
        try {
            long t = 455565655225562L;
            long a = 0;
            for (int i = 0; i < 1000; i++) {
                a = t / 1024 / 1024 / 1024;
            }

            blackhole.consume(a);

            return a;
        } catch (Throwable e) {
            blackhole.consume(e);
            return 0;
        }
    }


}
