package com.nantianba.study.jvm;

import com.nantianba.study.util.ClassRunnerUtils;
import jdk.swing.interop.SwingInterOpUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandlerTest {
    public static void main(String[] args) {
        ClassRunnerUtils.run(MethodHandlerTest.class);
    }

    public static void normalUsage() throws Throwable {
        Son son = new Son();

        System.out.println("call by son");
        son.hello();

        MethodHandle hello = MethodHandles.lookup().findVirtual(Father.class, "hello", MethodType.methodType(void.class)).bindTo(son);

        System.out.println("call by method handler");
        hello.invokeExact();
    }
}

class Father {
    public void hello() {
        System.out.println("father hello");
    }
}

class Son extends Father {
    @Override
    public void hello() {
        System.out.println("son hello");
    }
}