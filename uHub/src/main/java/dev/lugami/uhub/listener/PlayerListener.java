package dev.lugami.uhub.listener;

import dev.lugami.uhub.player.PlayerHotbar;
import dev.lugami.uhub.selector.hub.HubSelectorMenu;
import dev.lugami.uhub.selector.server.ServerSelectorMenu;
import dev.lugami.uhub.uHub;
import dev.lugami.uhub.util.Color;
import dev.lugami.uhub.util.Items;
import dev.lugami.uhub.util.Style;
import mkremins.fanciful.FancyMessage;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import dev.lugami.qlib.util.PlayerUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        uHub.getInstance().getHidingPlayers().remove(event.getPlayer().getUniqueId());
        uHub.getInstance().getHideCooldown().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (event.getItem().isSimilar(Items.selector)) {
                new ServerSelectorMenu().openMenu(player);
                PlayerHotbar.give(player);
            }
            else if(event.getItem().isSimilar(Items.hubSelector)) {
                new HubSelectorMenu().openMenu(player);
            } else if (event.getItem().getType().equals(Material.ENDER_PEARL)) {
                event.setCancelled(true);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        e.setCancelled(!canBuild(e.getPlayer()));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        e.setCancelled(!canBuild(e.getPlayer()));
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        e.setCancelled(!canBuild(e.getPlayer()));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(!canBuild(e.getPlayer()));
    }

    private boolean canBuild(Player p) {
        return (p.hasMetadata("Build") && p.getMetadata("Build").get(0).asBoolean()) && p.getGameMode() == GameMode.CREATIVE;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(null);

        player.setWalkSpeed(0.4F);
        player.setFlySpeed(0.1F);

        if (player.getOpenInventory() != null) {
            player.getOpenInventory().setCursor(null);
        }


        World world = Bukkit.getServer().getWorld("world");
        Location location = uHub.getInstance().getSpawnLocation();
        player.teleport(location);
        player.setHealth(20.0D);
        player.setSaturation(100.0F);
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setMaximumNoDamageTicks(20);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setFlying(false);
        player.setAllowFlight(true);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        player.getInventory().setHeldItemSlot(0);

        player.updateInventory();

        PlayerHotbar.give(player);


        List<String> messages = uHub.getInstance().getConfig().getStringList("joinmessage");
        messages.forEach(message -> player.sendMessage(Color.msg(message)));


        Bukkit.getOnlinePlayers().forEach(other -> {
            if (uHub.getInstance().isHidingPlayers(other)) {
                other.hidePlayer(player);
            }
        });
    }

    @EventHandler
    public void onPlayerPop(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player damagegive = (Player) e.getDamager();
            Player damagetaken = (Player) e.getEntity();

            if (damagetaken.getGameMode() == GameMode.CREATIVE) {
                damagegive.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot Pop this player!"));
                return;

            }
            e.setCancelled(true);
            damagegive.setLevel(damagetaken.getLevel() + 1);
            damagegive.playSound(damagetaken.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            damagegive.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&oPop!"));
            damagegive.hidePlayer(damagetaken);
            damagegive.playEffect(damagetaken.getLocation(), Effect.LARGE_SMOKE, 20);
            PlayerUtils.animateDeath(((CraftPlayer) e.getEntity()).getPlayer(), ((Player) e.getDamager()).getPlayer());
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMoveItem(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if(canInteractWithBlocks(player)) return;
            event.setCancelled(true);
            if (event.getInventory().getTitle().equals(Style.DARK_GRAY + "Select a server to join") && event.getInventory().getTitle().equals(Style.DARK_GRAY + "Select a hub to join")) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.ARROW) {
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!this.canInteractWithBlocks(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!this.canInteractWithBlocks(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    private boolean canInteractWithBlocks(Player player) {
        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        boolean isOp = player.isOp();
        boolean BuildMeta = player.hasMetadata("build");
        return isCreative && isOp && BuildMeta;
    }

    @EventHandler
    public void onPlayerInteract2(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        event.getInventory().setResult(null);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }
}
