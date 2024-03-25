package dev.lugami.bridge.bukkit.commands.disguise;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.bukkit.commands.disguise.menu.DisguiseRankMenu;
import dev.lugami.bridge.global.util.mojang.GameProfileUtil;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Flag;
import dev.lugami.qlib.command.Param;

import java.util.Objects;
import java.util.regex.Pattern;

public class DisguiseCommand {

    @Command(names = {"disguise", "dis", "d", "nick"}, permission = "bridge.disguise", description = "Disguise as another player", hidden = true)
    public static void disguise(Player player, @Flag(value = {"cc"}, description = "Disable case-correction of the name") boolean caseCorrection, @Param(name = "name") String name) {
        if (!Pattern.compile("^\\w{1,16}$").matcher(name).matches()) {
            player.sendMessage(ChatColor.RED + "Invalid username: " + name);
            return;
        }

        String finalName = name;
        if (Bukkit.getPlayerExact(name) != null || Objects.requireNonNull(BridgeGlobal.getUsedDisguisedNames()).stream().anyMatch(n -> n.equalsIgnoreCase(finalName))) {
            player.sendMessage(ChatColor.RED + "The name \"" + name + "\" is unavailable.");
            return;
        } // try this

        if (BukkitAPI.getProfile(name) != null && BukkitAPI.getPlayerRank(BukkitAPI.getProfile(name)).isStaff()) {
            player.sendMessage(ChatColor.RED + name + " exists as a Bridge player. You cannot impersonate others, please choose another name.");
            return;
        }

        if (BukkitAPI.getProfile(name) != null) {
            player.sendMessage(ChatColor.RED + name + " exists as a Bridge player. You cannot impersonate others, please choose another name.");
            return;
        }

        String realName = GameProfileUtil.getRealName(name);


        if (!caseCorrection && realName != null && !name.equals(realName)) {

            name = realName;
        }

        new DisguiseRankMenu(name, realName != null).openMenu(player);
    }
}
