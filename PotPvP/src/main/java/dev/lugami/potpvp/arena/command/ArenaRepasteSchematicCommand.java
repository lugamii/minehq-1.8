package dev.lugami.potpvp.arena.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.arena.ArenaGrid;
import dev.lugami.potpvp.arena.ArenaHandler;
import dev.lugami.potpvp.arena.ArenaSchematic;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ArenaRepasteSchematicCommand {

    @Command(names = { "arena repasteSchematic" }, permission = "op")
    public static void arenaRepasteSchematic(Player sender, @Param(name="schematic") String schematicName) {
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);

        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }


        int currentCopies = arenaHandler.countArenas(schematic);

        if (currentCopies == 0) {
            sender.sendMessage(ChatColor.RED + "No copies of " + schematic.getName() + " exist.");
            return;
        }

        ArenaGrid arenaGrid = arenaHandler.getGrid();

        sender.sendMessage(ChatColor.GREEN + "Starting...");

        arenaGrid.scaleCopies(schematic, 0, () -> {
            sender.sendMessage(ChatColor.GREEN + "Removed old maps, creating new copies...");

            arenaGrid.scaleCopies(schematic, currentCopies, () -> {
                sender.sendMessage(ChatColor.GREEN + "Repasted " + currentCopies + " arenas using the newest " + schematic.getName() + " schematic.");
            });
        });
    }

}