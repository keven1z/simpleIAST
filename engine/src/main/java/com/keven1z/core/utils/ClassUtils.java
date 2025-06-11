package com.keven1z.core.utils;

import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author keven1z
 * @since 2023/02/06
 */
public class ClassUtils {
    private static final String[] IGNORE_OBJECT_CLASS = new String[]{"java.lang.Object",
    "java.lang.Cloneable",
    "java.io.Closeable",
    "java.io.Serializable",
    "java.lang.Iterable",
    "java.io.Flushable",
    "java.lang.Appendable",
    "java.lang.Enum",
    "java.lang.Comparable",
    "java.rmi.Remote",
    "javax.servlet.SingleThreadModel",
    "java.lang.FunctionalInterface"};
    private static final String INTEGER_CLASS = "java.lang.Integer";
    private static final String IAST_FAMILY_CLASS_RES_PREFIX = "com/keven1z/";
    private static final int RECURSION_LIMIT = 3; // 设置递归限制次数

    public static Set<String> buildAncestors(String[] interfaces, String superClass) {
        Set<String> ancestors = new HashSet<>();
        if (superClass != null) {
            if (!CommonUtils.isInList(CommonUtils.toJavaClassName(superClass), Arrays.asList(IGNORE_OBJECT_CLASS))) {
                ancestors.add(superClass);
            }
        }
        for (String inter : interfaces) {
            if (!CommonUtils.isInList(CommonUtils.toJavaClassName(inter), Arrays.asList(IGNORE_OBJECT_CLASS))) {
                ancestors.add(inter);
            }
        }
        return ancestors;
    }

    public static Set<String> getAllInterfaces(Class<?> clazz) {
        Set<String> allInterfaces = new HashSet<>();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> inter : interfaces) {
            String name = inter.getName().replace(".", "/");
            allInterfaces.add(name);
            allInterfaces.addAll(getAllInterfaces(inter));
        }
        return allInterfaces;
    }
    public static Set<String> getAncestors(String[] interfaces, String superClass, ClassLoader loader) throws IOException {
        Set<String> ancestors = ClassUtils.buildAncestors(interfaces, superClass);
        return ClassUtils.getAncestors(ancestors, loader, 0);
    }

    public static Set<String> getAncestors(Set<String> ancestors, ClassLoader classLoader, int recursionCount) throws IOException {
        Set<String> set = new HashSet<>(5);
        if (recursionCount >= RECURSION_LIMIT) {
            return set;
        }
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        List<String> ignoreList = Arrays.asList(IGNORE_OBJECT_CLASS);
        for (String ancestor : ancestors) {
            InputStream stream = null;
            BufferedInputStream bufferedInput = null;
            ancestor = CommonUtils.toInternalClassName(ancestor);
            try {
                stream = classLoader.getResourceAsStream(ancestor + ".class");
                if (stream == null) {
                    continue;
                }
                bufferedInput = new BufferedInputStream(stream);
                ClassReader classReader = new ClassReader(bufferedInput);
                String superName = classReader.getSuperName();
                if (superName != null) {
                    if (!CommonUtils.isInList(CommonUtils.toJavaClassName(superName), ignoreList)) {
                        set.add(superName);
                    }
                }

                String[] interfaces = classReader.getInterfaces();
                for (String interfaceName : interfaces) {
                    if (!CommonUtils.isInList(CommonUtils.toJavaClassName(interfaceName), ignoreList)) {
                        if (set.size() <= 5) {
                            set.add(interfaceName);
                        }
                    }
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
                if (bufferedInput != null) {
                    bufferedInput.close();
                }
            }

        }
        if (!set.isEmpty()) {
            set.addAll(getAncestors(set, classLoader, recursionCount + 1));
        }
        set.addAll(ancestors);
        return set;
    }

    private static final Map<Class<?>, String> BASIC_TYPE = new HashMap<>();

    static {
        BASIC_TYPE.put(Integer.class, "I");
        BASIC_TYPE.put(Long.class, "J");
        BASIC_TYPE.put(Short.class, "S");
        BASIC_TYPE.put(Byte.class, "B");
        BASIC_TYPE.put(Double.class, "D");
        BASIC_TYPE.put(Float.class, "F");
        BASIC_TYPE.put(Boolean.class, "Z");
        BASIC_TYPE.put(Character.class, "C");

    }

    public static String classToSmali(Class<?> clazz) {

        if (BASIC_TYPE.containsKey(clazz)) {
            return BASIC_TYPE.get(clazz);
        } else {
            String name = clazz.getName();
            name = name.replace(".", "/");
            return "()L" + name + ";";
        }
    }

    public static boolean isPrivate(int access) {
        return BitUtils.isIn(access, ACC_PRIVATE);
    }

    public static boolean isProtected(int access) {
        return BitUtils.isIn(access, ACC_PROTECTED);
    }

    public static boolean isStatic(int access) {
        // 隐性的Java语法约束：如果是接口类型，就一定是静态的
        return isInterface(access)
                || BitUtils.isIn(access, ACC_STATIC);
    }

    public static boolean isInterface(int access) {
        return BitUtils.isIn(access, ACC_INTERFACE);
    }

    /**
     * @param clazz 待判断的class
     * @return 判断class对应的对象是否为数字类型
     */
    public static boolean classIsInteger(Class<?> clazz) {
        return clazz.getName().equals(INTEGER_CLASS);
    }

    public static void closeSimpleIASTClassLoader() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        //关闭classLoader
        Class<?> loadClass = ClassLoader.getSystemClassLoader().loadClass("com.keven1z.ModuleLoader");
        Field field = loadClass.getField("classLoader");
        Object classloader = field.get(loadClass);
        ReflectionUtils.invokeMethod(classloader, "closeIfPossible", new Class[]{});
    }

    public static String normalizeClass(String className) {
        return className.replace(".", "/");
    }


    public static boolean isComeFromIASTFamily(final String internalClassName, final ClassLoader loader) {

        if (null != internalClassName
                && isIASTPrefix(internalClassName)) {
            return true;
        }
        return null != loader
                && isIASTPrefix(normalizeClass(loader.getClass().getName()));
    }
    public static boolean shouldSkipProxyClass(String className) {
        return className.endsWith("$Proxy") || className.endsWith("Proxy$");
    }
    private static boolean isIASTPrefix(String className) {
        return className.startsWith(IAST_FAMILY_CLASS_RES_PREFIX);
    }
}
