package com.nantianba.study.feature.jdk21.safe_vt;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * FIX死锁问题
 * 检查调度延迟，疑似死锁时运载线程自动扩容
 * 需在系统启动时提前于其他场景加载，以优化默认线程池参数，否则无效
 * <p>
 * 添加：--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util.concurrent=ALL-UNNAMED --add-opens java.base/jdk.internal.vm=ALL-UNNAMED
 *
 * @author ying_zhou
 */
@UtilityClass
public class VirtualThreads {
    /**
     * 创建虚拟线程
     */
    public static Thread start(Runnable runnable) {
        return Thread.startVirtualThread(runnable);
    }

    public static Thread unStarted(Runnable runnable) {
        return Thread.ofVirtual().unstarted(runnable);
    }

    public static ExecutorService getExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    private static ForkJoinPool vtPoolRef;
    private static VarHandle vtCtlField;
    private static MethodHandle vtCompensateMethod;

    static {
        try {
            //强制进行检查需要显示设置参数
            System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "3000");

            //确保初始化虚拟线程carrier
            Thread.startVirtualThread(() -> {
            });

            vtPoolRef = getPool();

            //校验最大线程数数是否和设置相同
            VarHandle bounds = MethodHandles.privateLookupIn(ForkJoinPool.class, MethodHandles.lookup())
                    .findVarHandle(ForkJoinPool.class, "bounds", long.class);
            VarHandle parallelism = MethodHandles.privateLookupIn(ForkJoinPool.class, MethodHandles.lookup())
                    .findVarHandle(ForkJoinPool.class, "parallelism", int.class);

            int maxTotal = (short) (((long) bounds.get(vtPoolRef)) >> 16)
                           + ((int) parallelism.get(vtPoolRef));
            if (maxTotal != 3000) {
                throw new IllegalStateException("VirtualThread线程池最大线程数不符合预期:" + maxTotal);
            }

            vtCtlField = MethodHandles.privateLookupIn(ForkJoinPool.class, MethodHandles.lookup())
                    .findVarHandle(ForkJoinPool.class, "ctl", long.class);
            vtCompensateMethod = MethodHandles.privateLookupIn(ForkJoinPool.class, MethodHandles.lookup())
                    .findVirtual(ForkJoinPool.class, "tryCompensate", MethodType.methodType(int.class, long.class, boolean.class));

            startMonitor();
        } catch (Throwable e) {
            throw new Error("获取VirtualThread线程池失败", e);
        }
    }

    private static void startMonitor() {
        //死锁fixer
        new Thread(
                () -> {
                    //等待类加载完成
                    sleepInternal(2000, 0);

                    long stealCount = 0;
                    long lastAlertTime = 0;

                    //除解决死锁核心逻辑外，其他逻辑在其他线程执行，避免逻辑本身访问了死锁对象
                    while (true) {
                        //通过检查pool的任务统计间接检查是否有死锁
                        long newStealCount = vtPoolRef.getStealCount();
                        //解决死锁，当死锁发生>5S后
                        if (newStealCount == stealCount
                            && vtPoolRef.getQueuedSubmissionCount() > 0) {
                            try {
                                CompletableFuture<Void> logFuture = new CompletableFuture<>();
                                logAsync(() -> {
                                    Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();

                                    Map<String, Integer> sameStateCount = new HashMap<>();
                                    List<Pair<String, String>> list = allStackTraces.entrySet()
                                            .stream()
                                            .filter(p -> p.getKey().getName().contains("ForkJoinPool-"))
                                            .map(p -> new Pair<>(p.getKey().getName(),
                                                    p.getKey().getState() + ":\n"
                                                    + Arrays.stream(p.getValue()).map(stackTraceElement -> "\t" + stackTraceElement.toString())
                                                            .collect(Collectors.joining("\n"))
                                            ))
                                            .filter(p -> {
                                                String view = p.getValue();
                                                Integer val = sameStateCount.compute(view, (k, v) -> v == null ? 1 : v + 1);

                                                return val == 1;
                                            })
                                            .toList();

                                    String carrierView = list.stream()
                                            .map(p -> p.getKey() + "（相同 " + sameStateCount.getOrDefault(p.getValue(), 0) + " 个):\n" + p.getValue())
                                            .collect(Collectors.joining("\n"));

                                    String vtView = allStackTraces.keySet().stream()
                                            .filter(stackTraceElements -> stackTraceElements.getName().contains("ForkJoinPool-"))
                                            .map(VirtualThreads::parseVtInstance)
                                            .filter(Objects::nonNull)
                                            .map(t -> t.getName() + "\t" + t.getState() + ":\n"
                                                      + Arrays.stream(t.getStackTrace()).map(stackTraceElement -> "\t" + stackTraceElement)
                                                              .collect(Collectors.joining("\n")))
                                            .collect(Collectors.joining("\n\n"));


                                    System.err.println("DeadLockFixer:异步调度积压，栈采样：\n" + carrierView +
                                                       "\n虚拟线程采样：\n" + vtView);

                                    logFuture.complete(null);
                                });

                                //等待上述逻辑执行
                                try {
                                    logFuture.get(3, TimeUnit.MILLISECONDS);
                                } catch (Exception ignored) {
                                }

                                int prePoolSize = vtPoolRef.getPoolSize();

                                //每次最多补充500个
                                for (int j = 0; j < 500; j++) {
                                    if (!vtPoolRef.hasQueuedSubmissions()) {
                                        break;
                                    }
                                    tryCompensateOneWorker();

                                    sleepInternal(0, 100000);
                                }

                                boolean fixed = !vtPoolRef.hasQueuedSubmissions();

                                if (System.currentTimeMillis() - lastAlertTime > 60_000 || fixed) {

                                    logAsync(() -> {
                                        try {
                                            if (fixed
                                                && vtPoolRef.getPoolSize() <= prePoolSize) {
                                                //可能误报，未扩容
                                                return;
                                            }

                                            String content = "当前pool状态" + vtPoolRef + "<br>" +
                                                             "是否解决:" + (fixed ? "success" : "fail") + "<br>";

                                            System.out.println("DeadLockFixer:解決情況 " + content);
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    });

                                    lastAlertTime = System.currentTimeMillis();
                                }
                            } catch (Throwable e) {
                                logAsync(() -> {
                                    e.printStackTrace();
                                });
                            }
                        }

                        stealCount = newStealCount;

                        //每5s发起一次检测
                        sleepInternal(5000, 0);
                    }
                },
                "DeadLockFixer").

                start();

        //延迟Monitor
        new Thread(() -> {
            long lastStart = 0;
            AtomicLong lastCheckPoint = new AtomicLong(0);
            AtomicBoolean taskLock = new AtomicBoolean();

            while (true) {
                try {
                    long delay;

                    if (taskLock.compareAndSet(false, true)) {
                        //上次已完成，创建新任务，并记录上一次结果
                        delay = lastCheckPoint.get() - lastStart;

                        lastStart = System.currentTimeMillis();

                        Thread.startVirtualThread(() -> {
                            lastCheckPoint.set(System.currentTimeMillis());
                            taskLock.set(false);
                        });
                    } else {
                        //任务仍在执行，此时用当前时间-上次任务开始时间作为延迟
                        delay = System.currentTimeMillis() - lastStart;
                    }

                    if (delay > 5) {
                        //do some metric
//                        logAsync(() -> Cat.logSizeEvent("VtDelayCount", delay));
                    }
                } catch (Throwable e) {
                    logAsync(() -> {
                        e.printStackTrace();
                    });
                } finally {
                    sleepInternal(100, 0);
                }
            }
        }, "VirtualThreadDelayMonitor").start();
    }

    /**
     * 从运载线程提取出虚拟线程示例
     */
    private static Thread parseVtInstance(Thread maybeCarrier) {
        return VtThreadParser.parse(maybeCarrier);
    }

    /**
     * 使用反射从thread的cont字段中提取出vt实例
     */
    private static class VtThreadParser {
        private static boolean canParse;

        private static Class<?> carrierThreadClass;
        private static Field contField;

        private static Class<?> continuationClass;
        private static VarHandle targetField;

        private static final Map<Class, Function<Runnable, Thread>> vtFetchMethodMap = new ConcurrentHashMap<>();

        static {
            try {
                carrierThreadClass = Class.forName("jdk.internal.misc.CarrierThread");
                contField = Thread.class.getDeclaredField("cont");
                contField.setAccessible(true);

                continuationClass = Class.forName("jdk.internal.vm.Continuation");

                //如需打印vt stack 需要模块权限 --add-opens java.base/jdk.internal.vm=ALL-UNNAMED
                targetField = MethodHandles.privateLookupIn(continuationClass, MethodHandles.lookup())
                        .findVarHandle(continuationClass, "target", Runnable.class);

                canParse = true;
            } catch (Throwable e) {
                canParse = false;
                e.printStackTrace();
            }
        }

        public static Thread parse(Thread maybeCarrier) {
            if (!canParse) {
                return null;
            }

            if (carrierThreadClass.isInstance(maybeCarrier)) {
                try {
                    Object o = contField.get(maybeCarrier);
                    if (continuationClass.isInstance(o)) {
                        Runnable target = ((Runnable) targetField.get(o));

                        if (target != null) {
                            Class targetClass = target.getClass();

                            Function<Runnable, Thread> convert = vtFetchMethodMap.get(targetClass);
                            if (convert != null) {
                                return convert.apply(target);
                            }

                            try {
                                Field field = targetClass.getDeclaredField("val$vthread");
                                field.setAccessible(true);
                                Object client = field.get(target);

                                if (client instanceof Thread) {
                                    vtFetchMethodMap.putIfAbsent(targetClass, l -> {
                                        try {
                                            return (Thread) field.get(l);
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                            return null;
                                        }
                                    });
                                    return (Thread) client;
                                } else {
                                    vtFetchMethodMap.putIfAbsent(targetClass, l -> null);
                                    return null;
                                }
                            } catch (Exception e) {
                                vtFetchMethodMap.putIfAbsent(targetClass, l -> null);
                                return null;
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    private static void sleepInternal(int millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException ignored) {
        }
    }

    private static void tryCompensateOneWorker() throws Throwable {
        long ctl = (long) vtCtlField.get(VirtualThreads.vtPoolRef);
        vtCompensateMethod.invoke(VirtualThreads.vtPoolRef, ctl, true);
    }

    private static ForkJoinPool getPool() throws
            ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> virtualThreadClass = Class.forName("java.lang.VirtualThread");
        Field defaultSchedulerField = virtualThreadClass.getDeclaredField("DEFAULT_SCHEDULER");
        defaultSchedulerField.setAccessible(true);

        return (ForkJoinPool) defaultSchedulerField.get(null);
    }

    private static class LazyLoader {
        private static final ExecutorService logPool = new ThreadPoolExecutor(0,
                4,
                30,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadFactoryBuilder().setNameFormat("DeadLockLogger").setDaemon(true).build(),
                new ThreadPoolExecutor.DiscardPolicy()
        );
    }

    private static void logAsync(Runnable logAction) {
        //日誌 控制台等可能是死锁来源,故监控、日志、报警等单独提交,避免阻塞修复逻辑运行
        LazyLoader.logPool.submit(logAction);
    }
}
