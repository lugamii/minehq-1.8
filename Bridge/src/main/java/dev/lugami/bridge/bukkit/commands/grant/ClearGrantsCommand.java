package dev.lugami.bridge.bukkit.commands.grant;

import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lugami.bridge.bukkit.commands.punishment.menu.MainPunishmentMenu;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Flag;
import dev.lugami.qlib.command.Param;

public class ClearGrantsCommand {

    @Command(names = {"cleargrants"}, permission = "bridge.cleargrants", description = "Clear player's grants from the entire network", async = true)
    public static void clearGrants(CommandSender s, @Param(name = "target") Profile target) {

        if (target.getGrants().isEmpty()) {
            s.sendMessage(ChatColor.RED + target.getUsername() + " does not have any grants.");
            return;
        }
        s.sendMessage(ChatColor.GREEN + "Successfully cleared " + target.getCurrentGrant().getRank().getColor() + target.getUsername() + ChatColor.GREEN + "'s grants.");
        target.getGrants().clear();
        target.saveProfile();
    }
}

