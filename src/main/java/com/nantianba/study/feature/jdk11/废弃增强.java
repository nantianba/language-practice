package com.nantianba.study.feature.jdk11;

@Deprecated(since = "9", forRemoval = true)
public class 废弃增强 {
    public static void main(String[] args) {
        Class<废弃增强> clazz = 废弃增强.class;
        String s = clazz.descriptorString();

        System.out.println(s);


        boolean annotationPresent = clazz.isAnnotationPresent(Deprecated.class);

        System.out.println(annotationPresent);
    }
}
