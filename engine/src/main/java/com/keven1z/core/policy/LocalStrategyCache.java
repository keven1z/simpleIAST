package com.keven1z.core.policy;

import com.keven1z.core.Config;
import com.keven1z.core.utils.JsonUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
        this.cacheFile = new File(Config.getConfig().getPolicyCacheDirectory(), CACHE_FILE_NAME);

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

            // 原子性替换
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

        try (FileInputStream fis = new FileInputStream(cacheFile)) {
            byte[] data = new byte[(int) cacheFile.length()];
            fis.read(data);
            return JsonUtils.parseObject(data, ServerPolicy.class);
        } catch (IOException e) {
            LOGGER.warn("Failed to load strategy cache", e);
            return null;
        }
    }
}
