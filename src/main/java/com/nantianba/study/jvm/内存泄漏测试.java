package com.nantianba.study.jvm;

public class 内存泄漏测试 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("当前已使用内存：" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "M");

//           3次 每次增加200M内存
        {
            //可以回收，栈帧里的引用被覆盖了
            Object o2;
            {
                o2 = new Outer().create();
            }
        }
        Object o;
        {
            o = new Outer().create();
        }
        {
            //不可以回收，我猜栈帧里的引用还在
            Object o2;
            {
                o2 = new Outer().create();
            }
        }

        //600M
        System.out.println("当前已使用内存：" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "M");

        Thread.sleep(2000);
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.out.println("gc");

        Thread.sleep(20000);
        //看内存泄漏的情况

        //打印当前已使用内存
        // 400M
        System.out.println("当前已使用内存：" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "M");
    }

    public static class Outer {
        //占用100M内存
        byte[] a = new byte[1024 * 1024 * 100];

        Object create() {
            Object o = new Object() {

                byte[] a = new byte[1024 * 1024 * 100];

                @Override
                public String toString() {
                    return STR."\{Outer.this}";
                }
            };

            return o;
        }
    }
}
