package com.keven1z;


import java.lang.instrument.Instrumentation;

public interface Module {

    String START_ACTION_INSTALL = "install";
    String START_ACTION_UNINSTALL = "uninstall";
    String DEFAULT_APP_NAME = "default";


    void start(Instrumentation inst) throws Throwable;
}
