package dev.lugami.qmodsuite.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import dev.lugami.qmodsuite.menu.OnlineStaffMenu;
import dev.lugami.qmodsuite.qModSuite;
import dev.lugami.qmodsuite.utils.ModUtils;
import dev.lugami.qmodsuite.utils.StaffItems;
import dev.lugami.qmodsuite.utils.chest.ChestUtils;
import mkremins.fanciful.FancyMessage;
import dev.lugami.qlib.util.EntityUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GeneralListener implements Listener {
   private static Map<UUID, Entity> despawn = new HashMap();
   private Set<UUID> forceInvis = new HashSet();

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPlayerLogin(PlayerLoginEvent event) {
      Player player = event.getPlayer();
      if (player.hasPermission("qmodsuite.use") && player.hasMetadata("ForceModMode")) {
         this.forceInvis.add(player.getUniqueId());
      }

   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      if (this.forceInvis.contains(event.getPlayer().getUniqueId()) || qModSuite.getInstance().isEnableOnJoin() && event.getPlayer().hasPermission("qmodsuite.use")) {
         ModUtils.enableModMode(event.getPlayer(), true);
      }

      Iterator var2 = qModSuite.getInstance().getServer().getOnlinePlayers().iterator();

      while(true) {
         Player otherPlayer;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               otherPlayer = (Player)var2.next();
            } while(!ModUtils.isInvis(otherPlayer));
         } while(event.getPlayer().hasPermission("qmodsuite.use") && qModSuite.getInstance().isShowOtherStaff());

         event.getPlayer().hidePlayer(otherPlayer);
      }
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      if (ModUtils.isModMode(event.getPlayer())) {
         ModUtils.disableModMode(event.getPlayer());
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerInteract(PlayerInteractEvent event) {
      if (ModUtils.isModMode(event.getPlayer()) && event.getAction().name().contains("RIGHT")) {
         if (event.getItem() != null) {
            if (event.getItem().isSimilar(StaffItems.ONLINE_STAFF)) {
               event.setCancelled(true);
               (new OnlineStaffMenu()).openMenu(event.getPlayer());
               return;
            }

            if (event.getItem().isSimilar(StaffItems.GO_VIS)) {
               event.setCancelled(true);
               ModUtils.disableInvis(event.getPlayer());
               return;
            }

            if (event.getItem().isSimilar(StaffItems.GO_INVIS)) {
               event.setCancelled(true);
               ModUtils.enableInvis(event.getPlayer());
               return;
            }
         }

         if (event.getClickedBlock() != null && event.getClickedBlock().getState() instanceof Chest) {
            event.setCancelled(true);
            ChestUtils.openSilently(event.getPlayer(), (Chest)event.getClickedBlock().getState());
         }
      }

   }

   @EventHandler
   public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
      Player player = event.getPlayer();
      if (ModUtils.isModMode(player) && StaffItems.INSPECT_BOOK.isSimilar(player.getItemInHand())) {
         if (!(event.getRightClicked() instanceof Player)) {
            FancyMessage message = (new FancyMessage("Click here to despawn this ")).color(ChatColor.BLUE).tooltip(ChatColor.YELLOW + "Click to despawn").command("/despawnentity");
            message.then(EntityUtils.getName(event.getRightClicked().getType())).color(ChatColor.YELLOW).tooltip(ChatColor.YELLOW + "Click to despawn").command("/despawnentity");
            message.then(".").color(ChatColor.BLUE).tooltip(ChatColor.YELLOW + "Click to despawn").command("/despawnentity");
            despawn.put(event.getPlayer().getUniqueId(), event.getRightClicked());
            message.send(event.getPlayer());
            (new BukkitRunnable() {
               public void run() {
                  if (GeneralListener.despawn.containsKey(event.getPlayer().getUniqueId()) && ((Entity)GeneralListener.despawn.get(event.getPlayer().getUniqueId())).equals(event.getRightClicked())) {
                     GeneralListener.despawn.remove(event.getPlayer().getUniqueId());
                  }

               }
            }).runTaskLater(qModSuite.getInstance(), 200L);
            event.setCancelled(true);
         } else {
            Player clicked = (Player)event.getRightClicked();
            player.sendMessage(ChatColor.YELLOW + "Opening inventory of: " + ChatColor.AQUA + clicked.getName());
            player.chat("/invsee " + clicked.getName());
         }
      }
   }

   public static Map<UUID, Entity> getDespawn() {
      return despawn;
   }

   public Set<UUID> getForceInvis() {
      return this.forceInvis;
   }
}
