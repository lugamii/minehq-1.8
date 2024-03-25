// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.log;

import java.beans.ConstructorProperties;
import java.util.Iterator;

import com.moonsworth.fallback.Fallback;
import org.bukkit.Bukkit;
import java.util.List;
import org.bson.Document;
import java.util.ArrayList;
import org.bukkit.ChatColor;

import java.util.Queue;
import org.bukkit.command.CommandSender;

public class LogExportRunnable implements Runnable
{
    private CommandSender sender;
    private Queue<Log> logs;
    
    public LogExportRunnable(final CommandSender sender) {
        this(sender, Fallback.instance.getLogManager().getLogQueue());
    }
    
    @Override
    public void run() {
        if (this.logs.isEmpty()) {
            if (this.sender != null) {
                this.sender.sendMessage(ChatColor.RED + "There are no logs to be exported.");
            }
            return;
        }
        final long start = System.currentTimeMillis();
        final List<Document> logsDocuments = new ArrayList<Document>();
        final Iterator<Log> logIterator = this.logs.iterator();
        while (logIterator.hasNext()) {
            final Log current = logIterator.next();
            logsDocuments.add(current.toDocument());
            logIterator.remove();
        }
        Fallback.instance.getMongoDatabase().getCollection("logs").insertMany((List)logsDocuments);
        final long timeTaken = System.currentTimeMillis() - start;
        if (this.sender != null) {
            this.sender.sendMessage(ChatColor.GREEN + "Exported " + logsDocuments.size() + " logs in " + timeTaken + "ms.");
        }
        else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Exported " + logsDocuments.size() + " logs in " + timeTaken + "ms.");
        }
    }
    
    @ConstructorProperties({ "sender", "logs" })
    public LogExportRunnable(final CommandSender sender, final Queue<Log> logs) {
        this.sender = sender;
        this.logs = logs;
    }
}
