// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.command;

import com.comphenix.protocol.ProtocolLibrary;
import com.moonsworth.fallback.player.PlayerData;
import dev.lugami.qlib.command.Command;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.ChatColor;
import com.moonsworth.fallback.Fallback;
import dev.lugami.qlib.command.Param;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

public class ClientCommand
{
    @Command(names = { "fallback client"}, permission = "fallback.staff", hidden = true)
    public static void execute(final CommandSender sender, @Param(name = "target") final Player target) {
        final PlayerData targetData = Fallback.instance.getPlayerDataManager().getPlayerData(target);
        sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "------------------------------");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Player Information:");
        sender.sendMessage(ChatColor.GRAY + " Client Brand: " + ChatColor.RED + targetData.getClient().getName());
        sender.sendMessage(ChatColor.GRAY + " Client Version: " + ChatColor.RED + getVersion((CraftPlayer)target));
        sender.sendMessage("");
        sender.sendMessage(ChatColor.YELLOW + "Statistics:");
        sender.sendMessage(ChatColor.GRAY + " Average Ping: " + targetData.getPing());
        sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "------------------------------");
    }
    
    private static String getVersion(final CraftPlayer player) {
        final int i = ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
        return (i == 5) ? "1.7.10" : ((i == 47) ? "1.8" : "N/A");
    }
}
