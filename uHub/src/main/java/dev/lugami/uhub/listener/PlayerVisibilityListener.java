package dev.lugami.uhub.listener;

import dev.lugami.uhub.util.ItemBuilders;
import dev.lugami.uhub.util.TimeUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerVisibilityListener implements Listener {

    @Getter private List<Player> hidingPlayers;
    @Getter private Map<Player, Long> lastDone;

    public PlayerVisibilityListener() {
        hidingPlayers = new ArrayList<>();
        lastDone = new HashMap<>();
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() != null && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null) {
                String name = e.getItem().getItemMeta().getDisplayName();
                if (name.toLowerCase().contains("hide players")) {
                    if (!canUseHide(e.getPlayer())) return;
                    e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "You toggled player visibility off!");
                    e.getPlayer().getInventory().setItem(4, new ItemBuilders(Material.INK_SACK, 1).data((short) 10).displayName(ChatColor.translateAlternateColorCodes('&', "&9&l» &a&lShow Players &9&l«")).build());

                    if (!getHidingPlayers().contains(e.getPlayer()))
                        getHidingPlayers().add(e.getPlayer());
                    lastDone.put(e.getPlayer(), System.currentTimeMillis());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        e.getPlayer().hidePlayer(p);
                    }
                } else if (name.toLowerCase().contains("show players")) {
                    if (!canUseHide(e.getPlayer())) return;
                    e.getPlayer().sendMessage(ChatColor.AQUA + "You toggled player visibility on!");
                    e.getPlayer().getInventory().setItem(4, new ItemBuilders(Material.INK_SACK, 1).data((short) 8).displayName(ChatColor.translateAlternateColorCodes('&', "&9&l» &7&lHide Players &9&l«")).build());


                    if (getHidingPlayers().contains(e.getPlayer()))
                        getHidingPlayers().remove(e.getPlayer());
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        e.getPlayer().showPlayer(p);
                    }

                    lastDone.put(e.getPlayer(), System.currentTimeMillis());
                }
            }
        }
    }

    private boolean canUseHide(Player p) {
        if (getLastDone().get(p) == null) {
            return true;
        }

        if (getLastDone().get(p) + 5000 <= System.currentTimeMillis())
            return true;

        p.sendMessage(ChatColor.RED + "Please wait " + (TimeUtils.millisToSeconds(getLastDone().get(p) + 5000 - System.currentTimeMillis())) + "s to toggle player visibility!");
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (Player p : hidingPlayers) {
            p.hidePlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        lastDone.remove(e.getPlayer());
        hidingPlayers.remove(e.getPlayer());
    }

}