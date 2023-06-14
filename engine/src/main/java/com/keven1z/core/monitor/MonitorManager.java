package com.keven1z.core.monitor;

/**
 *
 */
public class MonitorManager {
    private static final MonitorPoolClient client = MonitorPoolClient.getClient();
    public static void start(Monitor... monitors) {
        for (Monitor monitor : monitors) {
            monitor.setDaemon(true);
            client.createTask(monitor);
        }
    }
    public static void clear(){
        client.shutdownNow();
    }
}
