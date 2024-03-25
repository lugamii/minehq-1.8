package dev.lugami.potpvp.follow.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.follow.FollowHandler;
import dev.lugami.potpvp.match.Match;
import dev.lugami.potpvp.match.MatchHandler;
import dev.lugami.qlib.command.Command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class UnfollowCommand {

    @Command(names={"unfollow"}, permission="")
    public static void unfollow(Player sender) {
        FollowHandler followHandler = PotPvPSI.getInstance().getFollowHandler();
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        if (!followHandler.getFollowing(sender).isPresent()) {
            sender.sendMessage(ChatColor.RED + "You're not following anybody.");
            return;
        }

        Match spectating = matchHandler.getMatchSpectating(sender);

        if (spectating != null) {
            spectating.removeSpectator(sender);
        }

        followHandler.stopFollowing(sender);
    }

}