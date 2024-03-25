package dev.lugami.bridge.bukkit.commands.disguise;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.disguise.DisguisePlayer;
import dev.lugami.qlib.command.Command;

public class UnDisguiseCommand {

    @Command(names = {"undisguise", "undis", "ud", "unnick", "und"}, permission = "bridge.disguise", description = "Reveal yourself once again!", hidden = true)
    public static void undisguise(Player player) {
        DisguisePlayer disguisePlayer = BridgeGlobal.getDisguiseManager().getDisguisePlayers().get(player.getUniqueId());

        if (disguisePlayer == null) {
            player.sendMessage(ChatColor.RED + "You're not disguised!");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "Removing the disguise...");
        BridgeGlobal.getDisguiseManager().undisguise(player, true, false);
    }
}
