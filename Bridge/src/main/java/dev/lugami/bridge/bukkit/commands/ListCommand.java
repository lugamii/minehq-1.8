package dev.lugami.bridge.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.visibility.FrozenVisibilityHandler;

import java.util.*;
import java.util.stream.Collectors;

public class ListCommand {

    @Command(names = { "list", "who", "players" }, permission = "", description = "See a list of online players", async = true)
    public static void list(CommandSender sender) {
        Map<Rank, List<String>> sorted = new TreeMap<>(Comparator.comparingInt(Rank::getPriority).reversed());
        int online = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {

            if(!sender.hasPermission("basic.staff") && !canSee(sender, player)) continue;
            ++online;
            Profile profile = BukkitAPI.getProfile(player.getUniqueId());
            Rank rank = BukkitAPI.getPlayerRank(profile);

            String displayName = player.getDisplayName() + ChatColor.WHITE;
            if (player.hasMetadata("invisible")) {
                displayName = ChatColor.GRAY + "*" + displayName + ChatColor.WHITE;
            }
            sorted.putIfAbsent(rank, new LinkedList<>());
            sorted.get(rank).add(displayName);
        }
        List<String> merged = new LinkedList<>();
        for (List<String> part : sorted.values()) {
            part.sort(String.CASE_INSENSITIVE_ORDER);
            merged.addAll(part);
        }
        sender.sendMessage(getHeader(sender));
        sender.sendMessage("(" + online + "/" + Bukkit.getMaxPlayers() + ") " + merged);
    }

    private static String getHeader(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        List<Rank> ranks = BridgeGlobal.getRankHandler().getRanks().parallelStream().sorted(Comparator.comparingInt(Rank::getPriority).reversed()).collect(Collectors.toList());
        for (Rank rank : ranks) {
            boolean displayed = rank.getPriority() >= 0;
            if (displayed) {
                if(rank.isHidden() && sender.hasPermission("bridge.hiddenranks")) {
                    builder.append(ChatColor.GRAY + "*").append(rank.getColor()).append(rank.getDisplayName()).append(ChatColor.RESET).append(", ");
                }else if(!rank.isHidden()){
                    builder.append(rank.getColor()).append(rank.getDisplayName()).append(ChatColor.RESET).append(", ");
                }
            }
        }
        if (builder.length() > 2) {
            builder.setLength(builder.length() - 2);
        }
        return builder.toString();
    }

    private static boolean canSee(CommandSender sender, CommandSender target) {
        if(!(sender instanceof Player)) return true;
        return FrozenVisibilityHandler.treatAsOnline((Player)target, (Player)sender);
    }

}
