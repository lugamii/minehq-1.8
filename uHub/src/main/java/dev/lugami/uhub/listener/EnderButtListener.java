package dev.lugami.uhub.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EnderButtListener implements Listener {

    public EnderButtListener() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem()) {
            if (event.getAction().name().contains("RIGHT")) {
                if (event.getItem().getType() == Material.ENDER_PEARL) {
                    Player player = event.getPlayer();
                    event.setCancelled(true);

                    if (player.getVehicle() != null && player.getVehicle() instanceof EnderPearl) {
                        player.getVehicle().remove();
                    }

                    EnderPearl enderPearl = player.launchProjectile(EnderPearl.class);
                    enderPearl.setPassenger(player);
                    enderPearl.setVelocity(player.getLocation().getDirection().multiply(2));
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof EnderPearl) {
            if (event.getEntity().getShooter() instanceof Player) {
                ((Player) event.getEntity().getShooter()).getLocation().add(0.0D, 1.0D, 0.0D);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnEntityDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            event.getDismounted().remove();
            event.getEntity().getLocation().add(0.0D, 1.0D, 0.0D);
            DoubleJumpListener.getAllowSneakJump().put(event.getEntity().getUniqueId(), false);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().isInsideVehicle()) {
            event.getPlayer().getVehicle().remove();
        }
    }
}