// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.banwave;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.PunishmentPacket;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.punishment.Evidence;
import dev.lugami.bridge.global.punishment.Punishment;
import dev.lugami.bridge.global.punishment.PunishmentType;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import com.moonsworth.fallback.Fallback;
import org.bukkit.Bukkit;

public class BanWave
{
    public BanWave() {
        Bukkit.getScheduler().runTaskTimer((Plugin)Fallback.instance, (Runnable)new Runnable() {
            @Override
            public void run() {
                BanWave.runBanWave();
            }
        }, 0L, 360000L);
    }
    
    private static String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    public static void runBanWave() {
        Fallback.instance.getBanWaveManager().runningBanwave = true;
        final int max = Fallback.instance.getBanWaveManager().getPlayersToBan().size();
        if (max > 0) {
            long last = 0L;
            while (Fallback.instance.getBanWaveManager().counter <= max - 1) {
                if (System.currentTimeMillis() - last > 2500L) {
                    final Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(Fallback.instance.getBanWaveManager().getPlayersToBan().get(Fallback.instance.getBanWaveManager().counter));
                    Bukkit.broadcastMessage(" ");
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Fallback" + ChatColor.WHITE + "  has removed " + ChatColor.AQUA + profile.getUsername() + ChatColor.WHITE + " from the network!");
                    Bukkit.broadcastMessage(ChatColor.WHITE + "Reason: " + ChatColor.RED + "Unfair Advantage");
                    Bukkit.broadcastMessage(" ");
                    HashSet<Evidence> proof = new HashSet<>();
                    Punishment punishment = new Punishment(profile, Profile.getConsoleProfile(), BridgeGlobal.getSystemName(), "[Fallback] Unfair Advantage", PunishmentType.BAN, new HashSet(), false, true, false, 9223372036854775807L);
                    profile.getPunishments().add(punishment);
                    profile.getStaffPunishments().add(punishment);
                    profile.saveProfile();
                    profile.saveProfile();
                    PacketHandler.sendToAll(new PunishmentPacket(punishment));
                    final BanWaveManager banWaveManager = Fallback.instance.getBanWaveManager();
                    ++banWaveManager.counter;
                    last = System.currentTimeMillis();
                }
            }
        }
        Fallback.instance.getBanWaveManager().counter = 0;
        Fallback.instance.getBanWaveManager().runningBanwave = false;
    }
}
