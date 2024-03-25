// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.log;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

public class LogManager
{
    private Queue<Log> logQueue;
    
    public LogManager() {
        this.logQueue = new ConcurrentLinkedQueue<Log>();
    }
    
    public void exportAllLogs() {
        new LogExportRunnable(null).run();
    }
    
    public Queue<Log> getLogQueue() {
        return this.logQueue;
    }
}
