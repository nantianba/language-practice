package com.nantianba.study.language;

import java.lang.reflect.Field;

public class 父子类静态字段 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        System.out.println(Child.name);
        System.out.println(Child.name2);
        System.out.println(Parent.name);


        System.out.println("*******************");

        {
            Field name = Parent.class.getField("name");
            System.out.println(name.get(Child.class));

        }
        {
            Field name = Child.class.getField("name");
            System.out.println(name.get(Child.class));
        }
        System.out.println("*******************");
        System.out.println(Parent.getNameP());
        System.out.println(Child.getNameP());
    }

    static class Parent {
        public final static String name = "parent";
        public final static String name2 = "parent";

        public static String getNameP() {
            return name;
        }
        public void name(){
            System.out.println("parent");
        }
    }

    static class Child extends Parent {
        public final static String name = "child";

        public static String getNameP() {
            return name;
        }

        public void name(){
            System.out.println("child");
        }
    }
}
