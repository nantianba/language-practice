package com.nantianba.study.jvm;

public class InternTest {
    public static void main(String[] args) {
        String str1= new StringBuilder().append("test_").append("normal").toString();
        System.out.println("str1.intern()==str1 = " + (str1.intern() == str1));

        String str2= new StringBuilder().append("ja").append("va").toString();
        System.out.println("str2==str2.intern() = " + (str2 == str2.intern()));

        String str3= "teststr3";
        System.out.println("str3==str3.intern() = " + (str3 == str3.intern()));
    }
}
