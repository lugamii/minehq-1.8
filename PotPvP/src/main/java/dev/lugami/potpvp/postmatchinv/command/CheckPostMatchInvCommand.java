package dev.lugami.potpvp.postmatchinv.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.postmatchinv.PostMatchInvHandler;
import dev.lugami.potpvp.postmatchinv.PostMatchPlayer;
import dev.lugami.potpvp.postmatchinv.menu.PostMatchMenu;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import dev.lugami.qlib.util.UUIDUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public final class CheckPostMatchInvCommand {

    @Command(names = { "checkPostMatchInv", "_" }, permission = "")
    public static void checkPostMatchInv(Player sender, @Param(name = "target") UUID target) {
        PostMatchInvHandler postMatchInvHandler = PotPvPSI.getInstance().getPostMatchInvHandler();
        Map<UUID, PostMatchPlayer> players = postMatchInvHandler.getPostMatchData(sender.getUniqueId());

        if (players.containsKey(target)) {
            new PostMatchMenu(players.get(target)).openMenu(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Data for " + UUIDUtils.name(target) + " not found.");
        }
    }

}