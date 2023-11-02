package com.keven1z.core.hook.http.body;

import com.keven1z.core.log.LogTool;
import com.strobel.annotations.NotNull;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HttpBodyOutputStream extends ByteArrayOutputStream {
    private static final Logger logger = Logger.getLogger(HttpBodyOutputStream.class);

    /**
     * 最大的body限制
     */
    private final int MAX_BODY_SIZE;

    private volatile boolean isExceeded;

    public HttpBodyOutputStream(int maxBodySize) {
        this.MAX_BODY_SIZE = maxBodySize;
        this.isExceeded = false;
    }

    public synchronized void write(byte[] b, int off, int len) {
        if (size() < this.MAX_BODY_SIZE) {
            super.write(b, off, len);
        } else if (!this.isExceeded) {
            if (LogTool.isDebugEnabled()){
                logger.warn("Monitored request/response size has exceeded the configured value.");
            }
            this.isExceeded = true;
        }
    }

    public void write(@NotNull byte[] b) throws IOException {
        if (size() < this.MAX_BODY_SIZE) {
            super.write(b);
        } else if (!this.isExceeded) {
            if (LogTool.isDebugEnabled()){
                logger.warn("Monitored request/response size has exceeded the configured value.");
            }
            this.isExceeded = true;
        }
    }

    public synchronized void write(int b) {
        if (size() < this.MAX_BODY_SIZE) {
            super.write(b);
        } else if (!this.isExceeded) {
            if (LogTool.isDebugEnabled()){
                logger.warn("Monitored request/response size has exceeded the configured value.");
            }
            this.isExceeded = true;
        }
    }
}
