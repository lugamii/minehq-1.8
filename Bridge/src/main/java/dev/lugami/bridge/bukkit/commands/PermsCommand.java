package dev.lugami.bridge.bukkit.commands;

import com.google.common.collect.Lists;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.util.PaginatedResult;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class PermsCommand {

    @Command(names = { "perms" }, permission = "bridge.viewperms", description = "View an player's permissions", async = true)
    public static void perms(CommandSender sender, @Param(name = "player", defaultValue = "self") Player player, @Param(name = "page", defaultValue = "1") int page) {
        Optional<Profile> optionalProfile = Optional.ofNullable(BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId()));
        if (!optionalProfile.isPresent()) {
            sender.sendMessage(ChatColor.RED + player.getName() + "'s profile isn't loaded.");
            return;
        }
        Rank rank = optionalProfile.get().getCurrentGrant().getRank();
        List<String> toSend = Lists.newArrayList();
        for (String permission : rank.getPermissions().keySet()) {
            toSend.add((player.hasPermission(permission) ? (ChatColor.GREEN + " + ") : (ChatColor.RED + " - ")) + ChatColor.WHITE + permission + " (" + rank.getPermissions().get(permission) + ")");
        }
        new PaginatedResult<String>() {
            @Override
            public String getHeader(int page, int maxPages) {
                return ChatColor.translateAlternateColorCodes('&', "&c" + StringUtils.repeat('-', 3) + " &r" + player.getDisplayName() + "&7(&r" + rank.getColor() + rank.getDisplayName() + "&7)'s Permissions (&e" + page + "&7/&e" + maxPages + "&7) &c" + StringUtils.repeat('-', 3));
            }

            @Override
            public String format(String entry, int index) {
                return entry;
            }
        }.display(sender, toSend, page);
    }
}

