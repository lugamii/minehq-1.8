// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.command;

import com.moonsworth.fallback.player.PlayerData;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.PunishmentPacket;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.punishment.Evidence;
import dev.lugami.bridge.global.punishment.Punishment;
import dev.lugami.bridge.global.punishment.PunishmentType;
import dev.lugami.qlib.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import com.moonsworth.fallback.Fallback;
import dev.lugami.qlib.command.Param;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.UUID;

public class FallbackBanCommand
{
    @Command(names = { "fallback ban" }, permission = "fallback.ban", hidden = true)
    public static void execute(final CommandSender player, @Param(name = "target") final Player target) {
        final PlayerData playerData = Fallback.instance.getPlayerDataManager().getPlayerData(target);
        playerData.setBanning(true);
        Fallback.instance.getBanWaveManager().getPlayersToBan().remove(target.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "Player " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + " has been manually banned!");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatColor.AQUA + "Fallback" + ChatColor.WHITE + "  has removed " + ChatColor.AQUA + target.getName() + ChatColor.WHITE + " from the network!");
        Bukkit.broadcastMessage(ChatColor.WHITE + "Reason: " + ChatColor.RED + "Unfair Advantage");
        Bukkit.broadcastMessage(" ");
        Punishment punishment = new Punishment(BridgeGlobal.getProfileHandler().getProfileByUUID(target.getUniqueId()), Profile.getConsoleProfile(), BridgeGlobal.getSystemName(), "[Fallback] Unfair Advantage", PunishmentType.BAN, new HashSet(), false, true, false, 9223372036854775807L);
        BridgeGlobal.getProfileHandler().getProfileByUUID(target.getUniqueId()).getPunishments().add(punishment);
        BridgeGlobal.getProfileHandler().getProfileByUUID(target.getUniqueId()).getStaffPunishments().add(punishment);
        BridgeGlobal.getProfileHandler().getProfileByUUID(target.getUniqueId()).saveProfile();
        BridgeGlobal.getProfileHandler().getProfileByUUID(target.getUniqueId()).saveProfile();
        PacketHandler.sendToAll(new PunishmentPacket(punishment)); }
}
