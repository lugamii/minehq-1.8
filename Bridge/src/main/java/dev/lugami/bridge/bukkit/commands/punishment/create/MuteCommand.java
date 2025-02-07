package dev.lugami.bridge.bukkit.commands.punishment.create;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.PunishmentPacket;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.punishment.Punishment;
import dev.lugami.bridge.global.punishment.PunishmentType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Flag;
import dev.lugami.qlib.command.Param;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class MuteCommand {

    @Command(names = {"mute"}, permission = "bridge.mute", description = "Temporarily mute a player, stopping them from talking in public chat", async = true)
    public static void muteCmd(CommandSender s, @Flag(value = {"a", "announce"}, description = "Announce this mute to the server") boolean silent, @Param(name = "target") Profile target, @Param(name = "time") Long length, @Param(name = "reason", wildcard = true) String reason) {
        Profile pf = BukkitAPI.getProfile(s);
        if (target.getActivePunishments(PunishmentType.MUTE).size() > 1) {
            s.sendMessage(ChatColor.RED + target.getUsername() + " is already muted.");
            return;
        }

        if (!BukkitAPI.canOverride(pf, target)) {
            s.sendMessage(ChatColor.RED + "You cannot punish this player.");
            return;
        }

        if (!s.hasPermission("bridge.mute.permanent") && TimeUnit.DAYS.toMillis(31L) < length) {
            s.sendMessage(ChatColor.RED + "You don't have permission to create a mute this long. Maximum time allowed: 30 days.");
            return;
        }

        Punishment punishment = new Punishment(target, pf, BridgeGlobal.getSystemName(),reason, PunishmentType.MUTE, new HashSet<>(), false, !silent, false, length);
        target.getPunishments().add(punishment);
        target.saveProfile();
        pf.saveProfile();
        PacketHandler.sendToAll(new PunishmentPacket(punishment));
    }
}