package dev.lugami.potpvp.command;

import dev.lugami.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * /setspawn command, updates spawn location
 * (spawn location is used when teleporting players to the lobby)
 *
 * {@link org.bukkit.World#setSpawnLocation(int, int, int, float, float)}
 * is a custom method provided by PowerSpigot which stores yaw/pitch along
 * with x/y/z. See net.frozenorb:mspigot-api in pom.xml
 */
public final class SetSpawnCommand {

    @Command(names = {"setspawn"}, permission = "op")
    public static void setSpawn(Player sender) {
        Location loc = sender.getLocation();

        sender.getWorld().setSpawnLocation(
            loc.getBlockX(),
            loc.getBlockY(),
            loc.getBlockZ(),
            loc.getYaw(),
            loc.getPitch()
        );

        sender.sendMessage(ChatColor.YELLOW + "Spawn point updated!");
    }

}