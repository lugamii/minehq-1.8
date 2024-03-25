// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.banwave;

import dev.lugami.bridge.bukkit.BukkitAPI;
import org.bukkit.entity.Player;

import java.util.Objects;

import com.moonsworth.fallback.banwave.checking.ResultTypes;
import com.moonsworth.fallback.banwave.checking.PlayerChecker;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import com.moonsworth.fallback.player.PlayerData;
import org.bukkit.Bukkit;
import com.moonsworth.fallback.Fallback;
import java.util.UUID;
import java.util.ArrayList;

public class BanWaveManager
{
    private ArrayList<UUID> playersToBan;
    public boolean runningBanwave;
    public int counter;
    
    public BanWaveManager() {
        this.runningBanwave = false;
        this.counter = 0;
        this.playersToBan = new ArrayList<UUID>();
    }
    
    public void addToBan(final UUID uuid) {
        if (!this.playersToBan.contains(uuid)) {
            this.playersToBan.add(uuid);
            final PlayerData data = Fallback.instance.getPlayerDataManager().getPlayerData(Bukkit.getPlayer(uuid));
            data.setAddedToBanwave(System.currentTimeMillis());
        }
    }


    public void addToBanWithChecking(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        PlayerData playerData = Fallback.instance.getPlayerDataManager().getPlayerData(player);
        String tpTooltip;
        String tpCommand;
        if (Bukkit.getServer().getPluginManager().getPlugin("Practice") != null) {
            tpTooltip = ChatColor.YELLOW + "Click to silently follow " + player.getDisplayName() + ChatColor.YELLOW + ".";
            tpCommand = "/silentfollow " + player.getName();
        } else {
            tpTooltip = ChatColor.YELLOW + "Click to teleport to " + player.getDisplayName() + ChatColor.YELLOW + ".";
            tpCommand = "/tp " + player.getName();
        }

        FancyMessage banwaveMessage = (new FancyMessage("")).then(BukkitAPI.getColor(player)).tooltip(tpTooltip).command(tpCommand).color(ChatColor.GRAY).then(" [" + playerData.getPing() + "ms] ").color(ChatColor.GRAY).then("[" + playerData.getClient().getName() + "]").color(ChatColor.RED).then(" has been added to the banwave.");
        if (PlayerChecker.checkPlayer(player) == ResultTypes.FAILED && !Fallback.instance.getBanWaveManager().getPlayersToBan().contains(uuid)) {
            Fallback.instance.getBanWaveManager().addToBan(player.getUniqueId());
            Fallback.instance.getAlertsManager().getAlertsToggled().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach((p) -> {
                if (p.hasPermission("fallback.alerts")) {
                    banwaveMessage.send(p);
                }

            });
        }

    }


    public void removeFromBan(final UUID uuid) {
        if (this.playersToBan.contains(uuid)) {
            this.playersToBan.remove(uuid);
        }
    }
    
    public ArrayList<UUID> getPlayersToBan() {
        return this.playersToBan;
    }
}
