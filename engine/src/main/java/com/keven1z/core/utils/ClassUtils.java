package com.keven1z.core.utils;

import com.keven1z.core.graph.clazz.ClassData;
import com.keven1z.core.graph.clazz.ClassGraph;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author keven1z
 * @date 2023/02/06
 */
public class ClassUtils {
    private static final String[] IGNORE_OBJECT_CLASS = new String[]{"java.lang.Object", "java.lang.Cloneable", "java.io.Serializable", "java.lang.Iterable"};

    /**
     * 获取当前类的所有祖先类
     *
     * @param ancestors  类的祖先
     * @param classGraph
     * @return 祖先类名的list集合
     */
    public static List<String> buildAllAncestors(List<String> ancestors, ClassLoader classLoader, ClassGraph classGraph) throws ClassNotFoundException {
        Set<String> set = new HashSet<>();
        for (String ancestor : ancestors) {
            Class<?> loadClass;
            try {
                //如果加载出错，使用系统classloader进行加载
                loadClass = classLoader.loadClass(ancestor);
            } catch (Throwable ignore) {
                classLoader = ClassLoader.getSystemClassLoader();
                loadClass = classLoader.loadClass(ancestor);
            }
            //提取接口
            Class<?>[] interfaces = loadClass.getInterfaces();
            for (Class<?> clazz : interfaces) {
                String name = clazz.getName();
                if (!ObjectUtils.isInList(name, Arrays.asList(IGNORE_OBJECT_CLASS))) {
                    classGraph.addNode(name);
                    classGraph.addEdge(new ClassData(ancestor), new ClassData(name));
                    set.add(name);
                }
            }
            //提取父类
            String name = loadClass.getSuperclass().getName();
            if (!ObjectUtils.isInList(name, Arrays.asList(IGNORE_OBJECT_CLASS))) {
                classGraph.addNode(name);
                classGraph.addEdge(new ClassData(ancestor), new ClassData(name));
                set.add(name);
            }


        }
        ArrayList<String> list = new ArrayList<>(set);
        if (!set.isEmpty()) {
            set.addAll(buildAllAncestors(list, classLoader, classGraph));
        }
        return list;
    }

    public static Set<String> buildAncestors(String[] interfaces, String superClass) {
        Set<String> ancestors = new HashSet<>();
        if (superClass != null) {
            superClass = superClass.replace("/", ".");

            if (!ObjectUtils.isInList(superClass, Arrays.asList(IGNORE_OBJECT_CLASS))) {
                ancestors.add(superClass);
            }
        }
        for (String inter : interfaces) {
            inter = inter.replace("/", ".");
            if (!ObjectUtils.isInList(inter, Arrays.asList(IGNORE_OBJECT_CLASS))) {
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

    public static Set<String> getAllInterfaces(Set<String> ancestors, ClassLoader classLoader) throws IOException {
        Set<String> set = new HashSet<>();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        for (String ancestor : ancestors) {
            InputStream stream = null;
            ancestor = ancestor.replace(".", "/");
            try {
                stream = classLoader.getResourceAsStream(ancestor + ".class");
                if (stream == null) {
                    continue;
                }
                ClassReader classReader = new ClassReader(stream);
                String[] interfaces = classReader.getInterfaces();
                for (String interfaceName : interfaces) {
                    if (!ObjectUtils.isInList(interfaceName, Arrays.asList(IGNORE_OBJECT_CLASS))) {
                        interfaceName = interfaceName.replace(".", "/");
                        set.add(interfaceName);
                    }
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

        }
        if (!set.isEmpty()) {
            set.addAll(getAllInterfaces(set, classLoader));
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

}
