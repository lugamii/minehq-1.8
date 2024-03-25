// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.command;

import com.moonsworth.fallback.banwave.BanWave;
import com.moonsworth.fallback.banwave.BanWaveManager;
import com.moonsworth.fallback.player.PlayerData;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.qlib.menu.Button;
import dev.lugami.qlib.menu.Menu;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.Inventory;
import org.bukkit.OfflinePlayer;

import java.util.*;

import dev.lugami.qlib.util.TimeUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

import com.moonsworth.fallback.Fallback;
import dev.lugami.qlib.command.Param;
import dev.lugami.qlib.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class BanWaveCommand implements Listener
{
    @Command(names = { "banwave start" }, permission = "fallback.banwave.start")
    public static void execute(Player sender) {
        sender.sendMessage(ChatColor.GREEN + "Starting Ban wave...");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f████&c█&f████"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f███&c█&6█&c█&f███ &7&m------- &c&lBAN WAVE STARTED &7&m-------"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f██&c█&6█&0█&6█&c█&f██"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f██&c█&6█&0█&6█&c█&f██ &cThere are a total of &41"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f█&c█&6██&0█&6██&c█&f█ &cplayers in the ban wave!"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f█&c█&6█████&c█&f█"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c█&6███&0█&6███&c█ &7&m------- &c&lBAN WAVE STARTED &7&m-------"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c█████████"));
        BanWave.runBanWave();
    }

    private static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    @Command(names = {"banwave add"}, permission = "fallback.banwave")
    public static void onAdd(Player sender, @Param(name = "player")  Profile target) {
         BanWaveManager waveManager = Fallback.instance.getBanWaveManager();
        if (waveManager.getPlayersToBan().contains(target.getUuid())) {
            return;
        }
        waveManager.addToBan(target.getUuid());
        sender.sendMessage(color(ChatColor.RED + "[" + ChatColor.YELLOW + ChatColor.BOLD + "\u26a0" + ChatColor.RED + "] " + ChatColor.GRAY + "[" + Bukkit.getServerName() + "] " + ChatColor.YELLOW + target.getUsername() + " &chas been added to the ban queue and will be banned!"));
    }
    
    @Command(names = {"banwave remove"}, permission = "op")
    public static void onRemove(Player sender, @Param(name = "player")  Profile target) {
         BanWaveManager waveManager = Fallback.instance.getBanWaveManager();
        waveManager.removeFromBan(target.getUuid());
        sender.sendMessage(color(ChatColor.RED + "[" + ChatColor.YELLOW + ChatColor.BOLD + "\u26a0" + ChatColor.RED + "] " + ChatColor.GRAY + "[" + Bukkit.getServerName() + "] " + ChatColor.YELLOW + target.getUsername() + " &chas been removed to the ban queue and will NOT be banned!"));
    }


    @Command(names = {"banwave list", "bq", "banqueue"}, permission = "fallback.banwave")
    public static void banwaveList(Player sender) {
        BanWaveManager waveManager = Fallback.instance.getBanWaveManager();
        StringBuilder stringBuilder = new StringBuilder();
        Iterator var3 = waveManager.getPlayersToBan().iterator();
        if(waveManager.getPlayersToBan().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "The banwave queue is empty");
            return;
        }
        while(var3.hasNext()) {
            UUID uuid = (UUID)var3.next();
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                stringBuilder.append(offlinePlayer + ", ");
            } else {
                stringBuilder.append(BukkitAPI.getColor(player) + ", ");
            }
        }

        Menu logsmenu = new Menu() {
            @Override
            public Map<Integer, Button> getButtons(Player player) {
                Map<Integer, Button> logButtons = new HashMap<>();
                int i = 0;
                for(UUID uuid : waveManager.getPlayersToBan()) {
                    logButtons.put(i, new Button() {
                        @Override
                        public String getName(Player player) {
                            if(Bukkit.getOfflinePlayer(uuid).getName() == null) {
                                if(Bukkit.getPlayer(uuid).getName() == null) {
                                    return ChatColor.RESET + uuid.toString();
                                } else {
                                    return ChatColor.RESET + Bukkit.getPlayer(uuid).getName();
                                }
                            }else {
                                return ChatColor.RESET + Bukkit.getOfflinePlayer(uuid).getName();
                            }
                        }

                        @Override
                        public List<String> getDescription(Player player) {
                            List<String> strings = new ArrayList();
                            strings.add("                                                    ");


                            PlayerData data = Fallback.instance.getPlayerDataManager().getPlayerData(uuid);

                            Profile profile = BukkitAPI.getProfile(uuid);
                            if(profile != null) {
                                Rank rank = profile.getCurrentGrant().getRank();
                                strings.add(ChatColor.GRAY + " Rank: " + rank.getColor() + rank.getDisplayName());
                                strings.add(ChatColor.GRAY + " Online: " + ChatColor.RED + profile.isOnline());
                            }
                            if(data != null) {
                                strings.add(ChatColor.GRAY + " Violation Level: " + ChatColor.RED + data.violations);
                                strings.add(ChatColor.GRAY + " Ping: " + ChatColor.RED + data.getPing() + "ms");
                                strings.add(ChatColor.GRAY + " Mouse Sensitivity: " + ChatColor.RED + Math.round(data.getSensitivity() * 200.0D) + "%");
                            }
                            strings.add("                                                    ");
                            return strings;
                        }

                        @Override
                        public Material getMaterial(Player player) {
                            return Material.PAPER;
                        }
                    });

                    i++;
                }
                return logButtons;
            }

            @Override
            public String getTitle(Player player) {
                return "Fallback | Ban Queue";
            }
        };
        logsmenu.setAutoUpdate(false);
        logsmenu.openMenu(sender);
    }
}
