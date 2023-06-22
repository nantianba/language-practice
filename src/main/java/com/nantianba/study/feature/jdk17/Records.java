package com.nantianba.study.feature.jdk17;

public class Records {
    record Student(String name, Integer age) {
        //紧凑构造函数
        public Student {
            if (age < 0) {
                throw new IllegalArgumentException("年龄不能小于0");
            }
        }
    }

    public static void main(String[] args) {
        var student = new Student("Wang Fang", 13);

        System.out.println("student.getClass() = " + student.getClass());
        System.out.println("student.getClass().descriptorString() = " + student.getClass().descriptorString());
        System.out.println("student = " + student);
        System.out.println("student.age() = " + student.age());
        System.out.println("student.name() = " + student.name());
    }
}
