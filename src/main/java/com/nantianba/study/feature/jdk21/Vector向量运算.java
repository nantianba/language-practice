package com.nantianba.study.feature.jdk21;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(8)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class Vector向量运算 {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Vector向量运算.class.getSimpleName())
                .build();

        new Runner(options).run();
    }

    private int[] nums;

    @Param({
            "1000",
            "10000",
            "100000",
            "1000000",
            "10000000",
            "100000000",
            "1000000000"
    })
    int size;

    @Setup
    public void setup() {
        nums = new int[size];
    }

    @Benchmark
    public void streamCompute() {
        int[] result = new int[size];

        Arrays.parallelSetAll(result, i -> (nums[i] * i + nums[i] * nums[i]) * -1);
    }

//    @Benchmark
//    public void ParallelComputation() throws InterruptedException {
//        int[] result = new int[size];
//        CountDownLatch count = new CountDownLatch(8);
//        for (int i = 0; i < 8; i++) {
//            final int start = i;
//            new Thread(() -> {
//                for (int j = start; j < nums.length; j += 8) {
//                    result[j] = (nums[j] * j + nums[j] * nums[j]) * -1;
//                }
//                count.countDown();
//            }).start();
//        }
//        count.await();
//    }

    @Benchmark
    public int[] Vector128Computation() {
        int[] result = new int[size];
        VectorSpecies<Integer> species = IntVector.SPECIES_128;
        int loop = species.loopBound(nums.length);
        int i = 0;
        for (; i < loop; i += species.length()) {
            IntVector va = IntVector.fromArray(species, nums, i);
            IntVector vb = IntVector.fromArray(species, nums, i);
            IntVector vc = va.mul(va)
                    .add(vb.mul(vb))
                    .neg();
            vc.intoArray(result, i);
        }
        return result;
    }

    @Benchmark
    public int[] Vector256Computation() {
        int[] result = new int[size];
        VectorSpecies<Integer> species = IntVector.SPECIES_256;
        int loop = species.loopBound(nums.length);
        int i = 0;
        for (; i < loop; i += species.length()) {
            IntVector va = IntVector.fromArray(species, nums, i);
            IntVector vb = IntVector.fromArray(species, nums, i);
            IntVector vc = va.mul(va)
                    .add(vb.mul(vb))
                    .neg();
            vc.intoArray(result, i);
        }
        return result;
    }

    @Benchmark
    public int[] Vector512Computation() {
        int[] result = new int[size];
        VectorSpecies<Integer> species = IntVector.SPECIES_512;
        int loop = species.loopBound(nums.length);
        int i = 0;
        for (; i < loop; i += species.length()) {
            IntVector va = IntVector.fromArray(species, nums, i);
            IntVector vb = IntVector.fromArray(species, nums, i);
            IntVector vc = va.mul(va)
                    .add(vb.mul(vb))
                    .neg();
            vc.intoArray(result, i);
        }

        return result;
    }

    @Benchmark
    public int[] defaultComputation() {
        int[] result = new int[size];
        for (int i = 0; i < nums.length; i++) {
            result[i] = (nums[i] * i + nums[i] * nums[i]) * -1;
        }
        return result;
    }

}
