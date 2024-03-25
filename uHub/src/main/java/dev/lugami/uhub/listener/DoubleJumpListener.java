package dev.lugami.uhub.listener;

import dev.lugami.uhub.uHub;
import lombok.Getter;
import org.bukkit.*;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoubleJumpListener implements Listener {

    @Getter private static Map<UUID,Boolean> allowSneakJump = new HashMap<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) return;

        event.setCancelled(true); // Prevent normal flight

        player.setAllowFlight(false);
        player.setFlying(false);

        // Perform double jump action here
        player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1.0));

        // Play sounds or effects as desired
        player.getLocation().getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 20);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.WITHER_SHOOT, 1, 1);
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLAZE_HIT, 1, 1);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (player.isOnGround() || player.getAllowFlight() || allowSneakJump.get(player.getUniqueId()) == null || !allowSneakJump.get(player.getUniqueId())) {
            return;
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(uHub.getInstance(), () -> player.setVelocity(player.getLocation().getDirection().multiply(4)), 1L);
        player.playSound(player.getLocation(),Sound.WITHER_HURT,10.5F,8.5F);
        player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 5.0F, 2.0F);
        player.playSound(player.getLocation(),Sound.BAT_IDLE,4.5F,3.5F);
        allowSneakJump.put(player.getUniqueId(),false);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        final Player player = (Player)event.getEntity();
        player.setAllowFlight(true);
        allowSneakJump.put(player.getUniqueId(),true);
    }

}