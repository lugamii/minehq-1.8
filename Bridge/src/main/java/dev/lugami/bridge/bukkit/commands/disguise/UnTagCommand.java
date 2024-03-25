package dev.lugami.bridge.bukkit.commands.disguise;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.disguise.DisguisePlayer;
import dev.lugami.qlib.command.Command;

public class UnTagCommand {

    @Command(names = {"untag"}, permission = "bridge.disguise", hidden = true)
    public static void untag(Player player) {
        DisguisePlayer disguisePlayer = BridgeGlobal.getDisguiseManager().getDisguisePlayers().get(player.getUniqueId());

        if (disguisePlayer == null) {
            player.sendMessage(ChatColor.RED + "You're not tagged!");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "Removing the tag...");
        BridgeGlobal.getDisguiseManager().undisguise(player, true, false);
    }
}
