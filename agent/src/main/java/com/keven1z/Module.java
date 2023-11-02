package com.keven1z;



import java.lang.instrument.Instrumentation;

public interface Module {
     String START_MODE_SERVER = "normal";
     String START_MODE_OFFLINE = "offline";

     String START_ACTION_INSTALL = "install";
    String START_ACTION_UNINSTALL = "uninstall";

    void start(String mode, Instrumentation inst) throws Throwable;
}
