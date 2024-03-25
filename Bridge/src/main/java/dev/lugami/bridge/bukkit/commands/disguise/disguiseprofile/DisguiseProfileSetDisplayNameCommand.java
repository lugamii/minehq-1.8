package dev.lugami.bridge.bukkit.commands.disguise.disguiseprofile;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.disguise.DisguiseProfile;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class DisguiseProfileSetDisplayNameCommand {

    @Command(names = {"disguiseprofile setdisplayname"}, permission = "bridge.disguise.admin", description = "Set a disguise profile display name", hidden = true)
    public static void disguised(Player player, @Param(name = "name") String name, @Param(name = "displayName", wildcard = true) String displayName) {
        DisguiseProfile profile = BridgeGlobal.getDisguiseManager().getProfile(name);

        profile.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        BridgeGlobal.getDisguiseManager().saveProfiles(true);
        player.sendMessage(ChatColor.GREEN + "You've changed display name of " + ChatColor.RESET + profile.getName() + ChatColor.GREEN + " to " + displayName + ChatColor.GREEN + '.');
    }
}

