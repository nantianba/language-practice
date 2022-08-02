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
 Benchmark                               Mode  Cnt          Score           Error  Units
 MethodHanleJmh.cachedMethodHandle      thrpt    3   20421446.371 ±  23650081.529  ops/s
 MethodHanleJmh.cachedMethodMoreHandle  thrpt    3  172731114.494 ±  20176348.353  ops/s
 MethodHanleJmh.cachedReflection        thrpt    3  174845608.763 ±  67199073.378  ops/s
 MethodHanleJmh.lambda                  thrpt    3  251940569.498 ± 125335672.103  ops/s
 MethodHanleJmh.methodHandle            thrpt    3    1131644.005 ±   3181816.692  ops/s
 MethodHanleJmh.quick                   thrpt    3  239122722.703 ± 218380758.021  ops/s
 MethodHanleJmh.reflection              thrpt    3   21958718.575 ±   5285624.573  ops/s
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

    static B son = new B();

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
    public void cachedMethodHandle(Blackhole blackhole) throws Throwable {
        int o = getO2();

        blackhole.consume(o);
    }
    static MethodHandle helloMethodHandles;

    static {
        try {
            helloMethodHandles = MethodHandles.lookup().findVirtual(B.class, "hello", MethodType.methodType(int.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private int getO2() throws Throwable {
        int o = (int) helloMethodHandles.bindTo(son).invokeExact();
        return o;
    }
    @Benchmark
    public void cachedMethodMoreHandle(Blackhole blackhole) throws Throwable {
        int o = getO3();

        blackhole.consume(o);
    }
    static MethodHandle helloMethodHandles2;

    static {
        try {
            helloMethodHandles2 = MethodHandles.lookup().findVirtual(B.class, "hello", MethodType.methodType(int.class)).bindTo(son);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private int getO3() throws Throwable {
        int o = (int) helloMethodHandles2.invokeExact();
        return o;
    }

    @Benchmark
    public void reflection(Blackhole blackhole) throws Throwable {
        Object invoke = getObject();

        blackhole.consume(invoke);
    }
    @Benchmark
    public void cachedReflection(Blackhole blackhole) throws Throwable {
        Object invoke = getObject2();

        blackhole.consume(invoke);
    }

    static Method helloMethod;

    static {
        try {
            helloMethod = B.class.getMethod("hello");
        } catch (NoSuchMethodException e) {

        }
    }

    private Object getObject2() throws InvocationTargetException, IllegalAccessException {


        Object invoke = helloMethod.invoke(son);
        return invoke;
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