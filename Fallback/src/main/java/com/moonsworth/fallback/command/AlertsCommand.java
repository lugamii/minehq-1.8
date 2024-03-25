// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.command;

import dev.lugami.qlib.command.Command;
import org.bukkit.ChatColor;
import com.moonsworth.fallback.Fallback;
import org.bukkit.entity.Player;

public class AlertsCommand
{
    @Command(names = { "alerts", "fallback alerts" }, permission = "fallback.alerts", hidden = true)
    public static void execute(final Player sender) {
        Fallback.instance.getAlertsManager().toggleAlerts(sender);
        Fallback.instance.getPlayerDataManager().getPlayerData(sender).staffalerts = !Fallback.instance.getPlayerDataManager().getPlayerData(sender).staffalerts;
        Fallback.instance.getPlayerDataManager().getPlayerData(sender).devalerts = !Fallback.instance.getPlayerDataManager().getPlayerData(sender).devalerts;
        sender.sendMessage(ChatColor.YELLOW + "You have" + (Fallback.instance.getAlertsManager().hasAlertsToggled(sender) ? (ChatColor.GREEN + " enabled " + ChatColor.YELLOW + "anticheat alerts") : (ChatColor.RED + " disabled " + ChatColor.YELLOW + "anticheat alerts")));
    }
}
