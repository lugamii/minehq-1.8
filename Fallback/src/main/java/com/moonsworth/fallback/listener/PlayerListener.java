// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.listener;

import com.moonsworth.fallback.Fallback;
import com.moonsworth.fallback.banwave.checking.PlayerChecker;
import com.moonsworth.fallback.banwave.checking.ResultTypes;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.event.PlayerAlertEvent;
import com.moonsworth.fallback.event.PlayerBanEvent;
import com.moonsworth.fallback.log.Log;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.CC;
import com.moonsworth.fallback.util.ChatComponentBuilder;
import com.moonsworth.fallback.util.ChatHelper;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.PunishmentPacket;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.punishment.Punishment;
import dev.lugami.bridge.global.punishment.PunishmentType;
import io.netty.buffer.Unpooled;
import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PlayerListener implements Listener
{
    private Map<UUID, Long> lastFire;
    
    public PlayerListener() {
        this.lastFire = new HashMap<>();
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Fallback.instance.getPlayerDataManager().addPlayerData(event.getPlayer());
        Fallback.instance.getServer().getScheduler().runTaskLaterAsynchronously(Fallback.instance, () -> {
            PlayerConnection playerConnection = ((CraftPlayer)event.getPlayer()).getHandle().playerConnection;
            playerConnection.sendPacket(new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer("CB-Client".getBytes()))));
            playerConnection.sendPacket(new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer("Lunar-Client".getBytes()))));
            playerConnection.sendPacket(new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer("FML|HS".getBytes()))));
            playerConnection.sendPacket(new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer("CC".getBytes()))));
        }, 10L);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (Fallback.instance.getAlertsManager().hasAlertsToggled(event.getPlayer())) {
            Fallback.instance.getAlertsManager().toggleAlerts(event.getPlayer());
        }
        Fallback.instance.getPlayerDataManager().removePlayerData(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = Fallback.instance.getPlayerDataManager().getPlayerData(player);
        if (playerData != null) {
            playerData.setInventoryOpen(false);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileLaunch(final EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player shooter = (Player)event.getEntity();
        final Long lastFired = this.lastFire.get(shooter.getUniqueId());
        if (lastFired != null && System.currentTimeMillis() - lastFired < 500L) {
            event.setCancelled(true);
            this.lastFire.put(shooter.getUniqueId(), System.currentTimeMillis());
            return;
        }
        this.lastFire.put(shooter.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerAlert(PlayerAlertEvent event) {
        if (!Fallback.instance.isAntiCheatEnabled()) {
            event.setCancelled(true);
        } else {
            Player player = event.getPlayer();
            if (player != null) {
                PlayerData playerData = Fallback.instance.getPlayerDataManager().getPlayerData(player);
                if (playerData != null) {
                    String tpTooltip;
                    String tpCommand;
                    String var6;
                    if (Bukkit.getServer().getPluginManager().getPlugin("Practice") != null || Bukkit.getServer().getPluginManager().getPlugin("PotPvP") != null) {
                        tpTooltip = ChatColor.YELLOW + "Click to silently follow " + player.getDisplayName() + ChatColor.YELLOW + ".";
                        tpCommand = "/silentfollow " + player.getName();
                        var6 = playerData.getClient().getName().replace("Lunar-Client", "LC");
                    } else {
                        tpTooltip = ChatColor.YELLOW + "Click to teleport to " + player.getDisplayName() + ChatColor.YELLOW + ".";
                        tpCommand = "/tp " + player.getName();
                        var6 = playerData.getClient().getName().replace("Lunar-Client", "LC");
                    }

                    ResultTypes types = PlayerChecker.checkPlayer(player);
                    String stage = ChatColor.RED + " (unsure)";
                    if (types == ResultTypes.PASS) {
                        stage = ChatColor.RED + " (unsure)";
                    }

                    if (types == ResultTypes.UNSURE) {
                        stage = ChatColor.YELLOW + " (Possibly)";
                    }

                    if (types == ResultTypes.FAILED) {
                        stage = ChatColor.GREEN + " (Cheating)";
                    }

                    FancyMessage basicAlerts = (new FancyMessage("")).color(ChatColor.RED).then("[" + ChatColor.YELLOW + ChatColor.BOLD + "⚠" + ChatColor.RED + "] ").color(ChatColor.RED).then("[" + Bukkit.getServerName() + "] ").color(ChatColor.GRAY).then(player.getName()).tooltip(tpTooltip).command(tpCommand).color(ChatColor.YELLOW).then(" failed ").color(ChatColor.GRAY).then(event.getCheckName()).color(ChatColor.RED).then(ChatColor.GRAY + " [" + ChatColor.RED + playerData.violations++ + ChatColor.GRAY + "] ");
                    BaseComponent[] b4sicAlerts = new ChatComponentBuilder(CC.translate("&c[&e&l⚠&c] &r&c[" + Bukkit.getServerName() + "] &r&7" + player.getName() + " failed " + event.getCheckName() + " [" + ChatColor.RED + playerData.violations++ + ChatColor.GRAY + "] ")).setCurrentClickEvent(ChatHelper.click(tpCommand)).setCurrentHoverEvent(ChatHelper.hover(tpTooltip)).create();
                    FancyMessage devAlerts = (new FancyMessage("")).color(ChatColor.RED).then("[" + ChatColor.YELLOW + ChatColor.BOLD + "⚠" + ChatColor.RED + "] ").color(ChatColor.RED).then("[" + Bukkit.getServerName() + "] ").color(ChatColor.GRAY).then(player.getName()).tooltip(tpTooltip).command(tpCommand).color(ChatColor.YELLOW).then(" failed ").color(ChatColor.GRAY).then(event.getCheckName()).color(ChatColor.RED).then(ChatColor.GRAY + " [" + ChatColor.RED + playerData.violations++ + ChatColor.GRAY + "] ");
                    FancyMessage banwaveMessage = (new FancyMessage("")).then(player.getDisplayName()).tooltip(tpTooltip).command(tpCommand).color(ChatColor.GRAY).then(" [" + playerData.getPing() + "ms] ").color(ChatColor.GRAY).then("[" + playerData.getClient().getName() + "]").color(ChatColor.RED).then(" has been added to the banwave.");
                    BaseComponent[] banwave = new ChatComponentBuilder(CC.translate(player.getDisplayName()) + " &7[" + playerData.getPing() + "ms] &7[" + playerData.getClient() + "] &chas been added to the banwave.").setCurrentHoverEvent(ChatHelper.hover(tpTooltip)).setCurrentClickEvent(ChatHelper.click(tpCommand)).create();
                    ++playerData.violations;
                    if (PlayerChecker.checkPlayer(event.getPlayer()) == ResultTypes.FAILED && !Fallback.instance.getBanWaveManager().getPlayersToBan().contains(event.getPlayer().getUniqueId())) {
                        Fallback.instance.getBanWaveManager().addToBan(event.getPlayer().getUniqueId());
                        Fallback.instance.getAlertsManager().getAlertsToggled().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach((p) -> {
                            if (p.hasPermission("fallback.alerts")) {
                                p.spigot().sendMessage(banwave);
                            }

                        });
                    }

                    if (System.currentTimeMillis() - playerData.getLastFlag() > 75L) {
                        Fallback.instance.getAlertsManager().getAlertsToggled().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach((p) -> {
                            PlayerData data = Fallback.instance.getPlayerDataManager().getPlayerData(p);
                            if (data.staffalerts && p.hasPermission("srmod.alerts")) {
                                p.spigot().sendMessage(b4sicAlerts);
                            }

                        });
                    }

                    Fallback.instance.getLogManager().getLogQueue().add(new Log(event.getPlayer().getUniqueId(), event.getCheckName() + " " + event.concatData(), Bukkit.spigot().getTPS()[0]));
                    playerData.setLastFlag(System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBan(PlayerBanEvent event) {
        if (!Fallback.instance.isAntiCheatEnabled()) {
            event.setCancelled(true);
        } else {
            Player player = event.getPlayer();
            if (player != null) {
                if (Fallback.instance.getBanWaveManager().getPlayersToBan().contains(event.getPlayer().getUniqueId())) {
                    Fallback.instance.getBanWaveManager().removeFromBan(event.getPlayer().getUniqueId());
                }

                Fallback.instance.getServer().getScheduler().runTask(Fallback.instance, () -> {
                    for(UUID u : Fallback.instance.getBanWaveManager().getPlayersToBan()) {
                        Fallback.instance.getBanWaveManager().getPlayersToBan().remove(player.getUniqueId());
                    }
                    Punishment punishment = new Punishment(BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId()), Profile.getConsoleProfile(), BridgeGlobal.getSystemName(), "[Fallback] Unfair Advantage", PunishmentType.BAN, new HashSet(), false, true, false, 9223372036854775807L);
                    BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId()).getPunishments().add(punishment);
                    BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId()).getStaffPunishments().add(punishment);
                    BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId()).saveProfile();
                    BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId()).saveProfile();
                    PacketHandler.sendToAll(new PunishmentPacket(punishment));
                    Bukkit.broadcastMessage(this.color(" "));
                    Bukkit.broadcastMessage(this.color(ChatColor.AQUA + "Fallback" + ChatColor.WHITE + " has removed " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " from the network!"));
                    Bukkit.broadcastMessage(this.color(ChatColor.WHITE + "Reason: " + ChatColor.RED + "Unfair Advantage"));
                    Bukkit.broadcastMessage(this.color(" "));
                    PlayerAlertEvent alertEvent = new PlayerAlertEvent(AlertType.RELEASE, player, "was banned from the ban queue for " + event.getReason());
                    Fallback.instance.getServer().getPluginManager().callEvent(alertEvent);
                });
            }
        }
    }
    
    public String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
