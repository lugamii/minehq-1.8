// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.command;

import com.comphenix.protocol.ProtocolLibrary;
import com.moonsworth.fallback.Fallback;
import com.moonsworth.fallback.player.PlayerData;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.qlib.command.Command;
import org.apache.commons.lang.StringUtils;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import dev.lugami.qlib.command.Param;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

public class InfoCommand
{
    @Command(names = { "fallback info" }, permission = "fallback.info", hidden = true)
    public static void execute(final CommandSender sender, @Param(name = "target") final Player target) {
        final PlayerData targetData = Fallback.instance.getPlayerDataManager().getPlayerData(target);
        final double[] tps = Bukkit.spigot().getTPS();
        final String[] tpsAvg = new String[tps.length];
        for (int i = 0; i < tps.length; ++i) {
            tpsAvg[i] = formatAdvancedTps(tps[i]);
        }
        final Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(target.getUniqueId());
        sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "------------------------------");
        sender.sendMessage(ChatColor.YELLOW + "Fallback lookup from all time:");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Player Information:");
        sender.sendMessage(ChatColor.GRAY + " Client Version: " + ChatColor.RED + getVersion((CraftPlayer)target));
        sender.sendMessage(ChatColor.GRAY + " Client Brand: " + ChatColor.RED + targetData.getClient().getName());
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Statistics:");
        sender.sendMessage(ChatColor.GRAY + " Total Logs: " + ChatColor.RED + targetData.violations);
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "Average CPS: " + ChatColor.RED + targetData.getLastCps());
        sender.sendMessage(ChatColor.GRAY + "Average Ping: " + ChatColor.RED + targetData.getPing() + " ms");
        sender.sendMessage(ChatColor.GRAY + "Average TPS: " + ChatColor.GREEN + StringUtils.join(tpsAvg, ", "));
        sender.sendMessage(ChatColor.GRAY + "Mouse Sensitivity: " + ChatColor.RED + Math.round(targetData.getSensitivity() * 200.0) + "%");
        sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "------------------------------");
    }
    
    private static String formatAdvancedTps(final double tps) {
        return ((tps > 18.0) ? ChatColor.GREEN : ((tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED)).toString() + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
    }
    
    private static String getVersion(final CraftPlayer player) {
        final int i = ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
        return (i == 5) ? "1.7.10" : ((i == 47) ? "1.8" : "N/A");
    }
}
