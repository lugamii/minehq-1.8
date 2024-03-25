package dev.lugami.potpvp.follow.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.follow.FollowHandler;
import dev.lugami.potpvp.match.MatchHandler;
import dev.lugami.potpvp.setting.Setting;
import dev.lugami.potpvp.setting.SettingHandler;
import dev.lugami.potpvp.validation.PotPvPValidation;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class FollowCommand {

    @Command(names = {"follow"}, permission = "")
    public static void follow(Player sender, @Param(name = "target") Player target) {
        if (!PotPvPValidation.canFollowSomeone(sender)) {
            return;
        }

        FollowHandler followHandler = PotPvPSI.getInstance().getFollowHandler();
        SettingHandler settingHandler = PotPvPSI.getInstance().getSettingHandler();
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        if (sender == target) {
            sender.sendMessage(ChatColor.RED + "No, you can't follow yourself.");
            return;
        } else if (!settingHandler.getSetting(target, Setting.ALLOW_SPECTATORS)) {
            if (sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Bypassing " + target.getName() + "'s no spectators preference...");
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + " doesn't allow spectators at the moment.");
                return;
            }
        }

        followHandler.getFollowing(sender).ifPresent(fo -> UnfollowCommand.unfollow(sender));

        if (matchHandler.isSpectatingMatch(sender)) {
            matchHandler.getMatchSpectating(sender).removeSpectator(sender);
        }

        followHandler.startFollowing(sender, target);
    }

}