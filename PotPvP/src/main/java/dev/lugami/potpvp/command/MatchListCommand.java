package dev.lugami.potpvp.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.match.Match;
import dev.lugami.potpvp.match.MatchHandler;
import dev.lugami.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class MatchListCommand {

    @Command(names = { "match list" }, permission = "op")
    public static void matchList(Player sender) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        for (Match match : matchHandler.getHostedMatches()) {
            sender.sendMessage(ChatColor.RED + match.getSimpleDescription(true));
        }
    }

}