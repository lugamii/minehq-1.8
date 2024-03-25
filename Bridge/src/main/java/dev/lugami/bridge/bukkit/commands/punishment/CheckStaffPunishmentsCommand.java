package dev.lugami.bridge.bukkit.commands.punishment;

import dev.lugami.bridge.bukkit.commands.punishment.menu.staffhistory.MainStaffPunishmentListMenu;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.entity.Player;

public class CheckStaffPunishmentsCommand {

    @Command(names = {"staffpunishments", "checkstaffpunishments", "staffhistory", "staffhist"}, permission = "bridge.staffhistory", description = "Check a player's active punishments", async = true)
    public static void staffPunishments(Player sender, @Param(name = "target") Profile target){
        new MainStaffPunishmentListMenu(target.getUuid().toString(), target.getUsername()).openMenu(sender);
    }
}