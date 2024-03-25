// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.command;

import dev.lugami.qlib.command.Command;

import java.util.*;

import dev.lugami.qlib.menu.Button;
import dev.lugami.qlib.menu.Menu;
import dev.lugami.qlib.util.PaginatedOutput;
import java.io.IOException;
import com.moonsworth.fallback.util.HastebinAPI;
import dev.lugami.qlib.util.UUIDUtils;
import dev.lugami.qlib.util.TimeUtils;
import com.google.common.collect.Lists;
import com.moonsworth.fallback.util.CC;
import dev.lugami.qlib.uuid.FrozenUUIDCache;
import org.bukkit.ChatColor;
import com.moonsworth.fallback.log.Log;
import org.bson.conversions.Bson;
import org.bson.Document;
import com.moonsworth.fallback.Fallback;
import dev.lugami.qlib.command.Param;
import dev.lugami.qlib.command.Flag;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogsCommand
{
    @Command(names = { "logs" }, permission = "anticheat.logs", async = true, hidden = true)
    public static void execute(final CommandSender sender, @Param(name = "target") final UUID target, @Param(name = "page", defaultValue = "1") final int page) {
        final Iterable<Document> mongoDocs = Fallback.instance.getMongoDatabase().getCollection("logs").find((Bson)new Document("player", (Object)target.toString()));
        final Iterable<Log> sessionLogs = Fallback.instance.getLogManager().getLogQueue();
        List<Document> logs = new ArrayList<>();
        final Set<Long> caught = new HashSet<>();
        for (final Document mongoDocument : mongoDocs) {
            final long time = mongoDocument.getLong("time");
            logs.add(mongoDocument);
            caught.add(time);
        }
        for (final Log log : sessionLogs) {
            if (log.getPlayer().equals(target) && caught.add(log.getTimestamp())) {
                logs.add(log.toDocument());
            }
        }
        if (logs.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No records for " + FrozenUUIDCache.name(target) + " found.");
            return;
        }
        final List<Document> finalLogs;
        logs = (finalLogs = (List<Document>)Lists.reverse((List)logs));

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&m-------------------------------------------------"));
            new PaginatedOutput<Document>() {
                public String getHeader(final int page, final int maxPages) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &cTotal logs: &6" + finalLogs.size() + ""));
                    return ChatColor.translateAlternateColorCodes('&', "&c" + UUIDUtils.name(target) + "&c's Logs &e(&7" + page + "&e/&7" + maxPages + "&e)");
                }
                
                public String format(final Document entry, final int index) {
                    final String message = entry.getString("log").replaceAll("failed ", "");
                    return ChatColor.YELLOW + " - [" + TimeUtils.formatIntoMMSS((int)((System.currentTimeMillis() - entry.getLong((Object)"time")) / 1000L)) + " ago on " + entry.getString((Object)"server") + "] " + (sender.hasPermission("anticheat.logs.view") ? (ChatColor.YELLOW + message) : (ChatColor.RED + "You do not have permission to view this log."));
                }
            }.display(sender, page, (List)logs);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&m-------------------------------------------------"));
        }
}
