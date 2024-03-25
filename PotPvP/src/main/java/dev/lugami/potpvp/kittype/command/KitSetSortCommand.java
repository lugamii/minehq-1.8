package dev.lugami.potpvp.kittype.command;

import java.util.Comparator;
import dev.lugami.potpvp.kittype.KitType;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitSetSortCommand {

	@Command(names = { "kittype setsort" }, permission = "op", description = "Sets a kit-type's sort")
	public static void execute(Player player, @Param(name = "kittype") KitType kitType, @Param(name = "sort") String sort) {
		kitType.setSort(Integer.parseInt(sort));
		kitType.saveAsync();

		KitType.getAllTypes().sort(Comparator.comparing(KitType::getSort));

		player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's sort.");
	}

}
