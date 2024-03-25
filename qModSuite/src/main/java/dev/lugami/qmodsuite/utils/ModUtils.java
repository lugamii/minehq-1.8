package dev.lugami.qmodsuite.utils;

import java.util.*;

import dev.lugami.qmodsuite.event.ModModeEnterEvent;
import dev.lugami.qmodsuite.event.ModModeExitEvent;
import dev.lugami.qlib.nametag.FrozenNametagHandler;
import dev.lugami.qlib.visibility.FrozenVisibilityHandler;
import dev.lugami.qmodsuite.qModSuite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class ModUtils {
   private static final Map<String, ItemStack[]> playerInventories = new HashMap();
   private static final Map<String, ItemStack[]> playerArmor = new HashMap();
   private static final Map<String, GameMode> playerGameModes = new HashMap();
   public static final Set<UUID> hideStaff = new HashSet();

   public static boolean isModMode(Player player) {
      return player.hasMetadata("modmode");
   }

   public static boolean isInvis(Player player) {
      return player.hasMetadata("invisible");
   }

   public static void enableModMode(Player player) {
      enableModMode(player, false);
   }

   public static void setModMode(boolean modMode, Player player) {
      if (modMode) {
         enableModMode(player);
      } else {
         disableModMode(player);
      }

   }

   public static void enableModMode(Player player, boolean silent) {
         if (!silent) {
            player.sendMessage(ChatColor.GOLD + "Mod Mode: " + ChatColor.GREEN + "Enabled");
         }

         player.setMetadata("modmode", new FixedMetadataValue(qModSuite.getInstance(), true));
         playerInventories.put(player.getName(), player.getInventory().getContents());
         playerArmor.put(player.getName(), player.getInventory().getArmorContents());
         playerGameModes.put(player.getName(), player.getGameMode());
         enableInvis(player);
         player.getInventory().clear();
         player.getInventory().setArmorContents(null);
         if (player.hasPermission("basic.gamemode")) {
            player.setGameMode(GameMode.CREATIVE);
         } else {
            qModSuite.getInstance().getLogger().info("Setting " + player.getName() + " to fly mode!");
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(true);
            player.setFlying(true);
         }

         player.getInventory().setItem(0, StaffItems.COMPASS);
         player.getInventory().setItem(1, StaffItems.INSPECT_BOOK);
         if (player.hasPermission("worldedit.wand")) {
            player.getInventory().setItem(2, StaffItems.WAND);
            player.getInventory().setItem(3, StaffItems.CARPET);
         } else {
            player.getInventory().setItem(2, StaffItems.CARPET);
         }

         ItemStack onlineStaff = StaffItems.ONLINE_STAFF.clone();
         player.getInventory().setItem(6, StaffItems.LAST_PVP);
         player.getInventory().setItem(7, onlineStaff);
         player.getInventory().setItem(8, StaffItems.GO_VIS);
         player.updateInventory();
         Bukkit.getPluginManager().callEvent(new ModModeEnterEvent(player));
   }

   public static void disableModMode(Player player) {
         player.sendMessage(ChatColor.GOLD + "Mod Mode: " + ChatColor.RED + "Disabled");
         player.removeMetadata("modmode", qModSuite.getInstance());
         disableInvis(player);
         player.getInventory().setContents((ItemStack[])playerInventories.remove(player.getName()));
         player.getInventory().setArmorContents((ItemStack[])playerArmor.remove(player.getName()));
         player.setGameMode((GameMode)playerGameModes.remove(player.getName()));
         if (player.getGameMode() != GameMode.CREATIVE) {
            player.setAllowFlight(false);
         }

         player.updateInventory();
         Bukkit.getPluginManager().callEvent(new ModModeExitEvent(player));
   }

   public static void enableInvis(Player player) {
         player.setMetadata("invisible", new FixedMetadataValue(qModSuite.getInstance(), true));
         Iterator var1 = qModSuite.getInstance().getServer().getOnlinePlayers().iterator();

         while(true) {
            Player otherPlayer;
            do {
               if (!var1.hasNext()) {
                  FrozenNametagHandler.reloadPlayer(player);
                  player.spigot().setCollidesWithEntities(false);
                  player.getInventory().setItem(8, StaffItems.GO_VIS);
                  player.updateInventory();
                  player.spigot().setCollidesWithEntities(false);
                  return;
               }

               otherPlayer = (Player)var1.next();
            } while(qModSuite.getInstance().isShowOtherStaff() && otherPlayer.hasPermission("qmodsuite.Use") && !hideStaff.contains(otherPlayer.getUniqueId()));

            otherPlayer.hidePlayer(player);
         }

   }

   public static void disableInvis(Player player) {
          player.removeMetadata("invisible", qModSuite.getInstance());
         FrozenVisibilityHandler.update(player);
         FrozenNametagHandler.reloadPlayer(player);
         player.spigot().setCollidesWithEntities(!isModMode(player));
         player.getInventory().setItem(8, StaffItems.GO_INVIS);
         player.updateInventory();
         player.spigot().setCollidesWithEntities(true);

   }

   public static void showStaff(Player player) {
        hideStaff.remove(player.getUniqueId());
         FrozenVisibilityHandler.updateAllTo(player);
   }

   public static void hideStaff(Player player) {
          hideStaff.add(player.getUniqueId());
         FrozenVisibilityHandler.updateAllTo(player);

   }
}
