package dev.lugami.qmodsuite.command;

import dev.lugami.qlib.command.Command;
import dev.lugami.qmodsuite.utils.ModUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class StaffVisCommands {
   @Command(
      names = {"hidestaff"},
      permission = "qmodsuite.use"
   )
   public static void hideStaff(Player player) {
      ModUtils.hideStaff(player);
      player.sendMessage(ChatColor.GREEN + "Staff members are now hidden.");
   }

   @Command(
      names = {"showstaff"},
      permission = "qmodsuite.use"
   )
   public static void showStaff(Player player) {
      ModUtils.showStaff(player);
      player.sendMessage(ChatColor.GREEN + "You can now see staff members.");
   }
}
