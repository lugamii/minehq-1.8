package dev.lugami.bridge.bukkit.commands.punishment;

import dev.lugami.bridge.bukkit.commands.punishment.menu.MainPunishmentMenu;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.qlib.command.Flag;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class CheckPunishmentsCommand {

    @Command(names = {"checkpunishments", "cp", "c", "history"}, permission = "bridge.checkpunishments", description = "Check a player's active punishments", async = true)
    public static void checkPunishments(Player sender, @Flag(value = {"gui", "menu"}, description = "Check a player's active punishments ingame") boolean gui, @Param(name = "target", extraData = "get") Profile target) {
        if (!gui) {
            FancyMessage message = new FancyMessage(ChatColor.GREEN + "[Click Here]" + ChatColor.YELLOW + " to view all of " + target.getUsername() + "'s punishments");
            message.tooltip(ChatColor.GRAY + "Click here: https://www.bridge.rip/u/" + target.getUsername() + "/punishments").link("https://www.bridge.rip/u/" + target.getUsername() + "/punishments");
            message.send(sender);
        } else {
            new MainPunishmentMenu(target.getUuid().toString(), target.getUsername()).openMenu(sender);
        }

    }
}
