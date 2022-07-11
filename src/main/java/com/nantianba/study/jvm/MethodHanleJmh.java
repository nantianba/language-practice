package com.nantianba.study.jvm;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 4)
@Threads(1)
@Fork(1)
@State(value = Scope.Benchmark)
public class MethodHanleJmh {
    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
                .include("MethodHanleJmh")
                .build())
                .run();
    }

    B son = new B();

    @Benchmark
    public void methodHandle(Blackhole blackhole) throws Throwable {
        MethodHandle hello = MethodHandles.lookup().findVirtual(B.class, "hello", MethodType.methodType(void.class)).bindTo(son);

        hello.invokeExact();

        blackhole.consume(blackhole);
    }

    @Benchmark
    public void reflection(Blackhole blackhole) throws Throwable {
        Method hello = B.class.getMethod("hello");

        hello.invoke(son);

        blackhole.consume(blackhole);
    }

    @Benchmark
    public void quick(Blackhole blackhole) throws Throwable {
        son.hello();

        blackhole.consume(blackhole);
    }
}

class A {
    public void hello() {
    }
}

class B extends A {
    @Override
    public void hello() {
    }
}