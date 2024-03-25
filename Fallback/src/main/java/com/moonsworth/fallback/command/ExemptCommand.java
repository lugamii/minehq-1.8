// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.command;

import com.moonsworth.fallback.player.PlayerData;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.qlib.command.Command;
import org.bukkit.ChatColor;
import com.moonsworth.fallback.Fallback;
import dev.lugami.qlib.command.Param;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

public class ExemptCommand
{
    @Command(names = { "fallback exempt" }, permission = "fallback.exempt", hidden = true)
    public static void execute(final CommandSender player, @Param(name = "target") final Player target) {
        final PlayerData playerData = Fallback.instance.getPlayerDataManager().getPlayerData(target);
        playerData.setSniffing(false);
        playerData.setBanning(false);
        player.sendMessage(ChatColor.GREEN + "You have exempted " + BukkitAPI.getColor(target) + ChatColor.GREEN + " from anticheat bans");
    }
}
