package com.keven1z;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.jar.JarFile;

/**
 * @author keven1z
 * @date 2022/04/21
 */
public class JarFileHelper {

    public static String getRootPath() {
        String path = JarFileHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
            path = path.substring(1);
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }

    /**
     * 添加jar文件到jdk的跟路径下，优先加载
     *
     * @param inst {@link Instrumentation}
     */
    public static void addJarToBootstrap(Instrumentation inst) throws IOException {
        String localJarPath = getLocalJarPath();
        inst.appendToBootstrapClassLoaderSearch(new JarFile(localJarPath));
//        inst.appendToBootstrapClassLoaderSearch(new JarFile(getEnginePath()));

    }

    /**
     * 获取当前所在jar包的路径
     *
     * @return jar包路径
     */
    public static String getLocalJarPath() {
        URL localUrl = Module.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        try {
            path = URLDecoder.decode(
                    localUrl.getFile().replace("+", "%2B"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("[openIAST] Failed to get jarFile path.");
        }
        return path;
    }

    public static String getAgentPath() {
        String path = getLocalJarPath();
        if (path == null){
            return null;
        }
        return path.substring(0, path.lastIndexOf("/")) + "/" + "iast-agent.jar";
    }
}
