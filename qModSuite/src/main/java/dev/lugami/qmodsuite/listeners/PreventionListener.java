package dev.lugami.qmodsuite.listeners;

import dev.lugami.qmodsuite.utils.ModUtils;
import dev.lugami.qmodsuite.utils.StaffItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PreventionListener implements Listener {
   @EventHandler
   public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
      Entity potentialPlayer = event.getDamager();
      if (potentialPlayer instanceof Player && ModUtils.isInvis((Player)potentialPlayer)) {
         ((Player)potentialPlayer).sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You cannot do this while in mod mode!");
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      if (event.getAction() == Action.PHYSICAL && ModUtils.isInvis(event.getPlayer())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onPlayerPickupItem(PlayerPickupItemEvent event) {
      if (ModUtils.isInvis(event.getPlayer())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onEntityDamage(EntityDamageEvent event) {
      if (event.getEntity() instanceof Player && ModUtils.isModMode((Player)event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onFoodLevelChange(FoodLevelChangeEvent event) {
      if (event.getEntity() instanceof Player && ModUtils.isModMode((Player)event.getEntity())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onEntityTarget(EntityTargetEvent event) {
      if (event.getTarget() instanceof Player && ModUtils.isModMode((Player)event.getTarget())) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onPlayerDropItem(PlayerDropItemEvent event) {
      if (ModUtils.isModMode(event.getPlayer())) {
         ItemStack item = event.getItemDrop().getItemStack();
         if (item.isSimilar(StaffItems.COMPASS) || item.isSimilar(StaffItems.CARPET) || item.isSimilar(StaffItems.INSPECT_BOOK) || item.isSimilar(StaffItems.WAND) || item.isSimilar(StaffItems.ONLINE_STAFF) || item.isSimilar(StaffItems.GO_VIS) || item.isSimilar(StaffItems.GO_INVIS)) {
            event.setCancelled(true);
            return;
         }

         if (ModUtils.isInvis(event.getPlayer())) {
            event.getItemDrop().remove();
         }
      }

   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event) {
      if (ModUtils.isModMode(event.getEntity())) {
         event.getDrops().clear();
         event.setDeathMessage((String)null);
      }
   }

   @EventHandler
   public void breakBlock(BlockBreakEvent e){
      if(ModUtils.isInvis(e.getPlayer())){
         e.setCancelled(true);
         e.getPlayer().sendMessage(ChatColor.RED + "You cannot do this whilst in your current state.");
      }
   }

   @EventHandler
   public void placeBlock(BlockBreakEvent e){
      if(ModUtils.isInvis(e.getPlayer())){
         e.setCancelled(true);
         e.getPlayer().sendMessage(ChatColor.RED + "You cannot do this whilst in your current state.");
      }
   }

}
