package com.keven1z.core.policy;

import com.keven1z.core.model.Config;
import com.keven1z.core.model.ApplicationModel;
import com.keven1z.core.utils.JsonUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * 本地策略缓存
 */
public class LocalStrategyCache {
    private static final String CACHE_FILE_NAME = "iast_policy.cache";
    private final Logger LOGGER = Logger.getLogger(getClass());

    private final File cacheFile;

    public LocalStrategyCache() {
        this.cacheFile = new File(Config.getConfig().getPolicyCacheDirectory()+File.separator+ApplicationModel.getAgentId(), CACHE_FILE_NAME);

        // 确保缓存目录存在
        if (!cacheFile.getParentFile().exists()) {
            boolean created = cacheFile.getParentFile().mkdirs();
            if (!created) {
                LOGGER.warn("Failed to create cache directory");
            }
        }
    }

    /**
     * 保存策略到本地
     */
    public void save(ServerPolicy policy) {
        if (policy == null) {
            return;
        }
        File tempFile = new File(cacheFile.getAbsolutePath() + ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            byte[] data = JsonUtils.toJsonString(policy).getBytes();
            fos.write(data);
            fos.flush();

            Files.move(tempFile.toPath(), cacheFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            LOGGER.error("Failed to save strategy cache", e);
            tempFile.delete();
        }
    }

    /**
     * 从本地加载策略
     */
    public ServerPolicy load() {
        if (!cacheFile.exists()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(cacheFile);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            // 读取文件内容到StringBuilder
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            // 解析JSON
            if (content.length() > 0) {
                String contentString = content.toString().replace("\n", "").replace("\r", "");
                return JsonUtils.toObject(contentString, ServerPolicy.class);
            } else {
                LOGGER.warn("Empty cache file: " + cacheFile.getAbsolutePath());
                return null;
            }

        } catch (IOException e) {
            LOGGER.error("Failed to read cache file: " + cacheFile.getAbsolutePath(), e);
            throw new IllegalStateException("Failed to load strategy cache", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while loading strategy cache", e);
            throw new IllegalStateException("Unexpected error", e);
        }
    }
}
