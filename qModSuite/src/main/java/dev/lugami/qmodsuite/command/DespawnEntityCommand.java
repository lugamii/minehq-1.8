package dev.lugami.qmodsuite.command;

import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.util.EntityUtils;
import dev.lugami.qmodsuite.listeners.GeneralListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class DespawnEntityCommand {
   @Command(
      names = {"despawnentity"},
      permission = "basic.staff"
   )
   public static void despawnentity(Player sender) {
      if (!GeneralListener.getDespawn().containsKey(sender.getUniqueId())) {
         sender.sendMessage(ChatColor.RED + "No entity to despawn.");
      } else {
         Entity entity = (Entity)GeneralListener.getDespawn().get(sender.getUniqueId());
         GeneralListener.getDespawn().remove(sender.getUniqueId());
         entity.remove();
         sender.sendMessage(ChatColor.GOLD + "Successfully despawned the " + ChatColor.WHITE + EntityUtils.getName(entity.getType()) + ChatColor.GOLD + ".");
      }
   }
}
