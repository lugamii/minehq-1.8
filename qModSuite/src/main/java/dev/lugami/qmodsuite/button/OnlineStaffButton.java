package dev.lugami.qmodsuite.button;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.UUID;
import dev.lugami.qlib.menu.Button;
import dev.lugami.qmodsuite.qModSuite;
import dev.lugami.qmodsuite.utils.ModUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class OnlineStaffButton extends Button {
   private UUID wrapped;

   public OnlineStaffButton(UUID wrapped) {
      this.wrapped = wrapped;
   }

   public String getName(Player player) {
      Player resolved = qModSuite.getInstance().getServer().getPlayer(this.wrapped);
      return resolved == null ? ChatColor.RED + "Error!" : resolved.getDisplayName();
   }

   public List<String> getDescription(Player player) {
      Player resolved = qModSuite.getInstance().getServer().getPlayer(this.wrapped);
      return resolved == null ? ImmutableList.of() : ImmutableList.of("", ChatColor.GOLD + "Mod Mode: " + (ModUtils.isModMode(resolved) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"), ChatColor.GOLD + "Invisible: " + (ModUtils.isInvis(resolved) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"), "", ChatColor.YELLOW + "Click to teleport.");
   }

   public Material getMaterial(Player player) {
      return Material.SKULL_ITEM;
   }

   public byte getDamageValue(Player player) {
      return 3;
   }

   public void clicked(Player player, int slot, ClickType clickType) {
      Player resolved = qModSuite.getInstance().getServer().getPlayer(this.wrapped);
      if (resolved != null) {
         if (!ModUtils.isInvis(player)) {
            ModUtils.enableInvis(player);
         }

         player.teleport(resolved);
         player.closeInventory();
      }

   }
}
