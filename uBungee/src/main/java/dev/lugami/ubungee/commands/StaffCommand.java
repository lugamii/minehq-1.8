package dev.lugami.ubungee.commands;

import dev.lugami.ubungee.util.PaginatedResult;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StaffCommand extends Command {

    public StaffCommand() {
        super("staff");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            Rank r = GlobalAPI.getPlayerRank(p.getUniqueId(), true);
            if(!r.isStaff()) {
                p.sendMessage(ChatColor.RED + "No permission.");
                return;
            }

            List<Profile> profileList = BridgeGlobal.getMongoHandler().getProfiles();
            profileList.removeIf(profile -> !profile.getCurrentGrant().getRank().isStaff());
            profileList.sort(Comparator.comparingInt(profile -> ((Profile) profile).getCurrentGrant().getRank().getPriority()).reversed());

            int page;

            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) {
                page = 1;
            }

            new PaginatedResult<String>() {
                @Override
                public String getHeader(int page, int maxPages) {
                    return ChatColor.translateAlternateColorCodes('&', "Staff Members (Page " + page + "/" + maxPages + ") ");
                }

                @Override
                public String format(String entry, int index) {
                    return entry;
                }
            }.display(commandSender, profileList.stream()
                    .map(profile -> profile.getCurrentGrant().getRank().getColor() + profile.getUsername() + ChatColor.GRAY + " (" + profile.getConnectedServer() + ChatColor.GRAY + ")")
                    .collect(Collectors.toList()), page);
        }
    }
}

