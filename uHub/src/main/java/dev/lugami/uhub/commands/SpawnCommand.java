package dev.lugami.uhub.commands;

import dev.lugami.qlib.command.Command;
import dev.lugami.uhub.uHub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpawnCommand {

    @Command(names = { "spawn" }, permission = "", description = "Teleports you to spawn!")
    public static void spawn(Player player) {

        World world = Bukkit.getServer().getWorld("world");
        player.teleport(uHub.getInstance().getSpawnLocation());
        player.sendMessage(ChatColor.GREEN + "Teleported!");
    }
}