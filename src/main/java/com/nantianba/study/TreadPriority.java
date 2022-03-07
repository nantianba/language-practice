package com.nantianba.study;

import io.reactivex.internal.operators.single.SingleDelayWithCompletable;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;

public class TreadPriority {
    public static void main(String[] args) throws InterruptedException {
        final int num = 10;
        AtomicLong[] ans = new AtomicLong[num];

        CyclicBarrier barrier=new CyclicBarrier(num);

        for (int i = 0; i < num; i++) {
            ans[i] = new AtomicLong();
            final AtomicLong an = ans[i];

            Thread thread = new Thread(() -> {
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                while (true) {
                    an.incrementAndGet();
                }
            });
            thread.setPriority(getNewPriority(i));
            thread.setDaemon(true);
            thread.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);

            System.out.println("ans = " + Arrays.toString(ans));
        }
    }

    private static int getNewPriority(int i) {
        if(i< 3){
            return 1;
        }
        if(i>7){
            return 10;
        }

        return 5;
    }
}
