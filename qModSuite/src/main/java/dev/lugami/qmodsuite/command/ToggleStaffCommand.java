package dev.lugami.qmodsuite.command;

import dev.lugami.qlib.command.Command;
import dev.lugami.qmodsuite.qModSuite;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ToggleStaffCommand {
   @Command(
      names = {"togglestaff"},
      permission = "qmodsuite.togglestaff"
   )
   public static void toggleStaff(Player player) {
      if (!qModSuite.getInstance().getSilencedStaffMembers().contains(player.getUniqueId())) {
         qModSuite.getInstance().getSilencedStaffMembers().add(player.getUniqueId());
         player.sendMessage(ChatColor.RED + "You can no longer see reports and requests.");
      } else {
         qModSuite.getInstance().getSilencedStaffMembers().remove(player.getUniqueId());
         player.sendMessage(ChatColor.GREEN + "You can now see reports and requests.");
      }

   }
}
