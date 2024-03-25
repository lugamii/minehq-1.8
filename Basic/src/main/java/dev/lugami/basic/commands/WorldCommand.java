package dev.lugami.basic.commands;

import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldCommand {

    @Command(names={"world"}, permission="basic.world", description = "Teleport to a world's spawn-point")
    public static void world(Player sender, @Param(name="world") World world) {
        sender.teleport(world.getSpawnLocation().clone().add(0.5, 0.0, 0.5));
    }
}

