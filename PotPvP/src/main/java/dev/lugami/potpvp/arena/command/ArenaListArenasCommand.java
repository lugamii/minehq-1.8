package dev.lugami.potpvp.arena.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.arena.Arena;
import dev.lugami.potpvp.arena.ArenaHandler;
import dev.lugami.potpvp.arena.ArenaSchematic;
import dev.lugami.potpvp.util.LocationUtils;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ArenaListArenasCommand {

    @Command(names = { "arena listArenas" }, permission = "op")
    public static void arenaListArenas(Player sender, @Param(name="schematic") String schematicName) {
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);

        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }

        sender.sendMessage(ChatColor.RED + "------ " + ChatColor.WHITE + schematic.getName() + " Arenas" + ChatColor.RED + " ------");

        for (Arena arena : arenaHandler.getArenas(schematic)) {
            String locationStr = LocationUtils.locToStr(arena.getSpectatorSpawn());
            String occupiedStr = arena.isInUse() ? ChatColor.RED + "In Use" : ChatColor.GREEN + "Open";

            sender.sendMessage(arena.getCopy() + ": " + ChatColor.GREEN + locationStr + ChatColor.GRAY + " - " + occupiedStr);
        }
    }

}