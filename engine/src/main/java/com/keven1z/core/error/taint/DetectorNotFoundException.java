package com.keven1z.core.error.taint;

public class DetectorNotFoundException extends TaintException {
    public DetectorNotFoundException(String message) {
        super(String.format("Not found detector:%s", message));
    }
}
