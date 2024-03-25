package dev.lugami.qmodsuite.command;

import dev.lugami.qlib.command.Command;
import dev.lugami.qmodsuite.utils.ModUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class VisCommands {
   @Command(
      names = {"vis"},
      permission = "qmodsuite.use"
   )
   public static void vis(Player sender) {
      if (!ModUtils.isModMode(sender)) {
         sender.sendMessage(ChatColor.RED + "You aren't in mod mode!");
      } else if (!ModUtils.isInvis(sender)) {
         sender.sendMessage(ChatColor.RED + "You aren't invisible!");
      } else {
         ModUtils.disableInvis(sender);
      }
   }

   @Command(
      names = {"invis"},
      permission = "qmodsuite.use"
   )
   public static void invis(Player sender) {
      if (ModUtils.isModMode(sender)) {
         if (ModUtils.isInvis(sender)) {
            sender.sendMessage(ChatColor.RED + "You are already invis!");
         } else {
            ModUtils.enableInvis(sender);
         }
      } else {
         ModUtils.enableModMode(sender);
      }

   }

   @Command(
      names = {"amivis", "?vis", "vis?", "?v", "v?", "invis?", "?invis"},
      permission = "qmodsuite.use"
   )
   public static void amIVis(Player sender) {
      boolean modMode = ModUtils.isModMode(sender);
      boolean invis = ModUtils.isInvis(sender);
      sender.sendMessage(ChatColor.GOLD + "You are " + (modMode ? ChatColor.GREEN + "in" : ChatColor.RED + "not in") + ChatColor.GOLD + " Mod Mode, and are " + (invis ? ChatColor.GREEN + "INVISIBLE" : ChatColor.RED + "VISIBLE") + ChatColor.GOLD + ".");
   }
}
