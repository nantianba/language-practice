package com.nantianba.study.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class RejectPolicyTest {
    public static void main(String[] args) {
        {
            final ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(5), new ThreadFactoryBuilder().setNameFormat("test").build(),
                    new ThreadPoolExecutor.DiscardOldestPolicy()
            );

            final int num = 30;
            CompletableFuture[] futures = new CompletableFuture[num];

            Set<Integer> ans = new HashSet<>();

            for (int i = 0; i < num; i++) {
                int finalI = i;
                final CompletableFuture<Object> future = new CompletableFuture<>();
                futures[i] = future;
                executor.submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ans.add(finalI);

                    future.complete(1);
                });

            }

            try {
                CompletableFuture.allOf(futures).get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            System.out.println("DiscardOld:" + ans.stream().sorted().collect(Collectors.toList()));
        }
        {
            final ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(5), new ThreadFactoryBuilder().setNameFormat("test").build(),
                    new ThreadPoolExecutor.DiscardPolicy()
            );

            final int num = 30;
            CompletableFuture[] futures = new CompletableFuture[num];

            Set<Integer> ans = new HashSet<>();

            for (int i = 0; i < num; i++) {
                int finalI = i;
                final CompletableFuture<Object> future = new CompletableFuture<>();
                futures[i] = future;
                executor.submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ans.add(finalI);

                    future.complete(1);
                });

            }

            try {
                CompletableFuture.allOf(futures).get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            System.out.println("Discard:" + ans.stream().sorted().collect(Collectors.toList()));
        }
        {
            final ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(5), new ThreadFactoryBuilder().setNameFormat("test").build(),
                    new ThreadPoolExecutor.AbortPolicy()
            );

            final int num = 30;
            CompletableFuture[] futures = new CompletableFuture[num];

            Set<Integer> ans = new HashSet<>();

            for (int i = 0; i < num; i++) {
                int finalI = i;
                final CompletableFuture<Object> future = new CompletableFuture<>();
                futures[i] = future;
                try {

                    executor.submit(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ans.add(finalI);

                        future.complete(1);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            try {
                CompletableFuture.allOf(futures).get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            System.out.println("Abort:" + ans.stream().sorted().collect(Collectors.toList()));
        }
        {
            final ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 10, 60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(5), new ThreadFactoryBuilder().setNameFormat("test").build(),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

            final int num = 30;
            CompletableFuture[] futures = new CompletableFuture[num];

            Set<Integer> ans = new HashSet<>();

            for (int i = 0; i < num; i++) {
                int finalI = i;
                final CompletableFuture<Object> future = new CompletableFuture<>();
                futures[i] = future;
                executor.submit(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ans.add(finalI);

                    future.complete(1);
                });

            }

            try {
                CompletableFuture.allOf(futures).get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            System.out.println("CallerRun:" + ans.stream().sorted().collect(Collectors.toList()));
        }

    }
}
