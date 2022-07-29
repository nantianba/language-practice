package com.nantianba.study.jvm;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Benchmark                     Mode  Cnt          Score          Error  Units
 * MethodHanleJmh.lambda        thrpt    3  255052284.436 ±  5939016.607  ops/s
 * MethodHanleJmh.methodHandle  thrpt    3    1423480.358 ±    97809.205  ops/s
 * MethodHanleJmh.quick         thrpt    3  256389216.117 ± 17274101.160  ops/s
 * MethodHanleJmh.reflection    thrpt    3   22018184.487 ± 53689257.394  ops/s
 */
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
        int o = getO();

        blackhole.consume(o);
    }

    private int getO() throws Throwable {
        MethodHandle hello = MethodHandles.lookup().findVirtual(B.class, "hello", MethodType.methodType(int.class)).bindTo(son);

        int o = (int) hello.invokeExact();
        return o;
    }

    @Benchmark
    public void reflection(Blackhole blackhole) throws Throwable {
        Object invoke = getObject();

        blackhole.consume(invoke);
    }

    private Object getObject() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method hello = B.class.getMethod("hello");

        Object invoke = hello.invoke(son);
        return invoke;
    }

    @Benchmark
    public void quick(Blackhole blackhole) throws Throwable {
        int hello = getHello();

        blackhole.consume(hello);
    }

    private int getHello() {
        return son.hello();
    }

    @Benchmark
    public void lambda(Blackhole blackhole) throws Throwable {
        Object consume = consume(son::hello);

        blackhole.consume(consume);
    }

    private Object consume(Callable<Object> run) throws Throwable{
        return run.call();
    }

}

class A {
    public int hello() {
        return 2;
    }
}

class B extends A {
    @Override
    public int hello() {
        return 1;
    }
}