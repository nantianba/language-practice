package com.nantianba.study.opensource.javassist;

import com.nantianba.study.opensource.javassist.reosurce.SuperClass;
import com.nantianba.study.opensource.javassist.reosurce.TestClass;
import javassist.*;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class 动态生成代码 {
    public static void main(String[] args) throws Exception {
        Class.forName(SuperClass.class.getName());
        Class<?> newClass = buildListnerClass(SuperClass.class);
        Object o = newClass.getDeclaredConstructor().newInstance();
        newClass.getMethod("onMessage", String.class).invoke(o, "haha");
    }


    private static Class<?> buildListnerClass(Class<?> targetClazz) throws CannotCompileException, NotFoundException, IOException {

        ClassPool pool = new ClassPool(true);
        ClassLoader parentClassLoader = targetClazz.getClassLoader();
        myClassLoader.putSuperClassLoader(parentClassLoader);

        pool.appendClassPath(new LoaderClassPath(parentClassLoader));

        String className = "EnhancerFor" + targetClazz.getSimpleName();

        CtClass ctClass = pool.makeClass(className);
        ctClass.setSuperclass(pool.get(SuperClass.class.getName()));

        //添加onMessage方法，执行super的方法
        CtMethod method = CtNewMethod.make(STR."""
    public void onMessage(\{String.class.getName()} message){
        \{TestClass.class.getName()}.before(message);
        super.test(message);
    }
""", ctClass);

        ctClass.addMethod(method);

        //添加一个构造函数
        CtConstructor constructor = CtNewConstructor.make("public " + className + "(){}", ctClass);
        //添加autowired注解
        ctClass.addConstructor(constructor);

        byte[] bytecode = ctClass.toBytecode();

        Class<?> aClass = ctClass.toClass(myClassLoader, null);

        myClassLoader.putIfAbsent(className, aClass, bytecode);

        return aClass;
    }


    private static final MyClassLoader myClassLoader = new MyClassLoader();

    private static class MyClassLoader extends ClassLoader {
        private final Map<ClassLoader, Object> clref = new ConcurrentHashMap<>();

        private final Map<String, Class> container = new ConcurrentHashMap<>();
        private final Map<String, byte[]> container2 = new ConcurrentHashMap<>();

        private void putIfAbsent(String name, Class<?> clazz, byte[] bytecode) {
            container.putIfAbsent(name, clazz);
            container2.putIfAbsent(getCname(name), bytecode);
        }

        private void putSuperClassLoader(ClassLoader classLoader) {
            clref.put(classLoader, new Object());
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Class ans = container.get(name);
            if (ans != null) {
                return ans;
            }

            for (ClassLoader parent : clref.keySet()) {
                try {
                    ans = parent.loadClass(name);

                    if (ans != null) {
                        return ans;
                    }
                } catch (ClassNotFoundException e) {
                    //ignore
                }
            }

            return super.findClass(name);
        }

        @Nullable
        @Override
        public URL getResource(String name) {
            if (container2.containsKey(name)) {
                try {
                    return new URL("file:/");
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

            return super.getResource(name);
        }

        @Nullable
        @Override
        public InputStream getResourceAsStream(String name) {
            if (container2.containsKey(name)) {
                return new ByteArrayInputStream(container2.get(name));
            }
            return super.getResourceAsStream(name);
        }

    }

    private static String getCname(String name) {
        return name.replace('.', '/') + ".class";
    }
}
