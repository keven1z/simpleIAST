package com.keven1z.core.utils;

import com.keven1z.core.EngineBoot;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    public static String readJsonFile(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        try {

            if (inputStream == null) return null;
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(reader);

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line.replace("\r", "").replace("\n", ""));
            }
            return sb.toString();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ignore) {
            }
        }
    }
    /**
     * 获取当前jar包所在的文件夹路径
     *
     * @return jar包所在文件夹路径
     */
    public static String getBaseDir() {
        String baseDir;
        String jarPath = EngineBoot.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            baseDir = URLDecoder.decode(new File(jarPath).getParent(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            baseDir = new File(jarPath).getParent();
        }
        return baseDir;
    }
}
