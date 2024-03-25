// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.command;

import com.moonsworth.fallback.player.PlayerData;
import dev.lugami.qlib.command.Command;
import org.bukkit.ChatColor;
import com.moonsworth.fallback.Fallback;
import dev.lugami.qlib.command.Param;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

public class UnexemptCommand
{
    @Command(names = { "fallback unexempt"}, permission = "fallback.unexempt", hidden = true)
    public static void execute(final CommandSender player, @Param(name = "target") final Player target) {
        final PlayerData playerData = Fallback.instance.getPlayerDataManager().getPlayerData(target);
        playerData.setSniffing(true);
        playerData.setBanning(true);
        player.sendMessage(ChatColor.RED + "You have unexempted " + ChatColor.WHITE + target.getName() + ChatColor.RED + " from anticheat bans");
    }
}
