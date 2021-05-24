package com.nantianba.study.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClassRunnerUtils {

    public static void run(Class<?> aClass) {
        final Method[] declaredMethods = aClass.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (method.getName().contains("lambda$")) {
                continue;
            }

            System.out.println("**************** " + method.getName() + " ****************");
            if (method.getParameterCount() == 0 ) {
                method.setAccessible(true);

                try {
                    method.invoke(aClass);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.err.println("NotFound");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String argInfo = Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(","));
                System.out.println("Skipped:" + method.getName() + "(" + argInfo + ")");
            }

            System.out.println();
        }
    }
}
