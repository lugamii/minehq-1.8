package dev.lugami.potpvp.kittype.command;

import dev.lugami.potpvp.kittype.KitType;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitSetIconCommand {

	@Command(names = { "kittype seticon" }, permission = "op", description = "Sets a kit-type's icon")
	public static void execute(Player player, @Param(name = "kittype") KitType kitType) {
		if (player.getItemInHand() == null) {
			player.sendMessage(ChatColor.RED + "Please hold an item in your hand.");
			return;
		}

		kitType.setIcon(player.getItemInHand().getData());
		kitType.saveAsync();

		player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's icon.");
	}

}
