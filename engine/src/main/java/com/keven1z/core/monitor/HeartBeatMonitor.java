package com.keven1z.core.monitor;





 /**
  * 心跳线程
  * @author keven1z
  * @since 2023/11/3
  */

public class HeartBeatMonitor extends Monitor {

    @Override
    public String getThreadName() {
        return "SimpleIAST-HeartBeat-Thread";
    }

    @Override
    public boolean isForServer() {
        return true;
    }

    @Override
    public void doRun() throws Exception {

        Thread.sleep(1500);
    }

}
