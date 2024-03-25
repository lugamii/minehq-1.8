package dev.lugami.potpvp.command;

import com.google.common.collect.ImmutableList;

import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.qLib;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class ShowMeTheDoorCommand {

    private static final List<String> reasons = ImmutableList.of(
        "Here's the door.",
        "Go cry about it.",
        "Later, skater!"
    );

    @Command(names = {"showmethedoor"}, permission = "")
    public static void showmethedoor(Player sender) {
        String reason = reasons.get(qLib.RANDOM.nextInt(reasons.size()));
        sender.kickPlayer(ChatColor.YELLOW + reason);
    }

}
