package com.nantianba.study.feature.jdk17;

public class 密封类 {
    public static void main(String[] args) {
        System.out.println(new TruckDriver());
        System.out.println(new Teacher());
        System.out.println(new People());
    }

    // 密封类
    public static sealed class People permits Teacher, Driver {
    }

    // 密封类 People 必须至少有一个子类。
// 非密封的 People 子类
    non-sealed static class Teacher extends People {
    }

    // 密封的 People 子类。
    sealed static class Driver extends People permits TruckDriver{
    }

    non-sealed static class TruckDriver extends Driver {
    }
    final static class CarDriver extends TruckDriver {
    }
//    无法编译
//    static non-sealed class CarDriver extends People {
//    }
}
