package dev.lugami.potpvp.arena.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ArenaFreeCommand {

    @Command(names = { "arena free" }, permission = "op")
    public static void arenaFree(Player sender) {
        PotPvPSI.getInstance().getArenaHandler().getGrid().free();
        sender.sendMessage(ChatColor.GREEN + "Arena grid has been freed.");
    }

}