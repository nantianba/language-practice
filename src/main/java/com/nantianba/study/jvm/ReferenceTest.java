package com.nantianba.study.jvm;

import com.nantianba.study.util.ClassRunnerUtils;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceTest {
    public static void main(String[] args) {
        ClassRunnerUtils.run(ReferenceTest.class);
    }

    private static void soft_reference() throws InterruptedException {
        Object object = new Object();
        ReferenceQueue<Object> notifyQueue = new ReferenceQueue<>();
        SoftReference<Object> softRef = new SoftReference<>(object, notifyQueue);
        System.out.println("before gc");
        System.out.println("softRef.get() =" + softRef.get());
        System.out.println("notifyQueue.poll() = " + notifyQueue.poll());

        // 清除强引用,触发GC
        object = null;
        System.gc();

        System.out.println("after gc");
        System.out.println("softRef.get() = " + softRef.get());

        //即使发生gc，该对象也不一定会被回收加入queue,只有内存空间不足才会回收软引用对象，因此queue中获取不到对象
        //发生gc到将对象加入到queue中需要一段时间，这里sleep等待，方便下面poll方法（非阻塞）获取值
        Thread.sleep(2000);
        System.out.println("notifyQueue.poll() = " + notifyQueue.poll());
    }

    private static void weak_reference() throws InterruptedException {
        Object object = new Object();
        ReferenceQueue<Object> notifyQueue = new ReferenceQueue<>();
        WeakReference<Object> weakRef = new WeakReference<>(object, notifyQueue);
        System.out.println("before gc");
        System.out.println("weakRef.get() =" + weakRef.get());
        System.out.println("notifyQueue.poll() = " + notifyQueue.poll());

        // 清除强引用,触发GC
        object = null;
        System.gc();
        Thread.sleep(2000);

        System.out.println("after gc");
        System.out.println("weakRef.get() = " + weakRef.get());
        System.out.println("notifyQueue.poll() = " + notifyQueue.poll());

    }

    private static void phantom_reference() throws InterruptedException {
        Object object = new Object();
        ReferenceQueue<Object> notifyQueue = new ReferenceQueue<>();
        PhantomReference<Object> phantomRef = new PhantomReference<>(object, notifyQueue);
        System.out.println("before gc");
        System.out.println("phantomRef.get() =" + phantomRef.get());
        System.out.println("notifyQueue.poll() = " + notifyQueue.poll());

        // 清除强引用,触发GC
        object = null;
        System.gc();
        Thread.sleep(2000);

        System.out.println("after gc");
        System.out.println("phantomRef.get() = " + phantomRef.get());
        System.out.println("notifyQueue.poll() = " + notifyQueue.poll());
    }
}
