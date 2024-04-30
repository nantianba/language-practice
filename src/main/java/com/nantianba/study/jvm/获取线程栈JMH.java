package com.nantianba.study.jvm;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 4)
@Threads(1)
@Fork(1)
@State(value = Scope.Benchmark)
public class 获取线程栈JMH {
    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
                .include("获取线程栈JMH")
                .build())
                .run();
    }

    @Benchmark
    public void methodHandle(Blackhole blackhole) throws Throwable {
        boolean o = checkDeadLock();

        blackhole.consume(o);
    }


    private static boolean checkDeadLock() {
        List<String> topStack = Thread.getAllStackTraces().entrySet()
                .stream()
                .filter(p -> p.getKey().getName().contains("ForkJoinPool-1-worker"))
                .map(p -> {
                    StackTraceElement[] traceElements = p.getValue();
                    if (traceElements == null || traceElements.length == 0) {
                        return "empty";
                    }
                    return traceElements[0].toString();
                })
                .toList();

        return !topStack.isEmpty()
                && topStack.stream().allMatch(s -> s.contains("jdk.internal.vm.Continuation.run(Continuation"));
    }
}
