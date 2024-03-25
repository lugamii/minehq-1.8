package dev.lugami.potpvp.kittype.command;

import dev.lugami.potpvp.PotPvPSI;
import dev.lugami.potpvp.kittype.KitType;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitDeleteCommand {

	@Command(names = { "kittype delete" }, permission = "op", description = "Deletes an existing kit-type")
	public static void execute(Player player, @Param(name = "kittype") KitType kitType) {
		kitType.deleteAsync();
		KitType.getAllTypes().remove(kitType);
		PotPvPSI.getInstance().getQueueHandler().removeQueues(kitType);

		player.sendMessage(ChatColor.GREEN + "You've deleted the kit-type by the ID \"" + kitType.id + "\".");
	}

}
