package com.keven1z.core.error;

public class DetectorNotFoundException extends TaintException {
    public DetectorNotFoundException(String message) {
        super(String.format("Not found detector:%s", message));
    }
}
