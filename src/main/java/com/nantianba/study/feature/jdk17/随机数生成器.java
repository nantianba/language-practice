package com.nantianba.study.feature.jdk17;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class 随机数生成器 {
    public static void main(String[] args) {
        RandomGeneratorFactory<RandomGenerator> l128X256MixRandom = RandomGeneratorFactory.of("L128X256MixRandom");
        RandomGenerator randomGenerator = l128X256MixRandom.create(System.currentTimeMillis());
// 生成随机数
        System.out.println("randomGenerator.nextInt(10) = " + randomGenerator.nextInt(10));
    }
}
