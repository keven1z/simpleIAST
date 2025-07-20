package com.keven1z.core.error;

import java.io.IOException;

public class ConfigLoadException extends IOException {
    public ConfigLoadException(String message) {
        super(message);
    }
}
