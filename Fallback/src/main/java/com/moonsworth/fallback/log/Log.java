// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.log;

import java.beans.ConstructorProperties;
import org.bukkit.Bukkit;
import org.bson.Document;
import java.util.UUID;

public class Log
{
    private long timestamp;
    private UUID player;
    private String log;
    private double tps;
    
    public Document toDocument() {
        final Document document = new Document();
        document.put("player", (Object)this.player.toString());
        document.put("server", (Object)Bukkit.getServer().getServerName());
        document.put("log", (Object)this.log);
        document.put("time", (Object)this.timestamp);
        return document;
    }
    
    public Log(final UUID player, final String log, final double tps) {
        this.timestamp = System.currentTimeMillis();
        this.player = player;
        this.log = log;
        this.tps = tps;
    }
    
    @ConstructorProperties({ "timestamp", "player", "log", "tps" })
    public Log(final long timestamp, final UUID player, final String log, final double tps) {
        this.timestamp = timestamp;
        this.player = player;
        this.log = log;
        this.tps = tps;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public UUID getPlayer() {
        return this.player;
    }
    
    public String getLog() {
        return this.log;
    }
    
    public double getTps() {
        return this.tps;
    }
}
