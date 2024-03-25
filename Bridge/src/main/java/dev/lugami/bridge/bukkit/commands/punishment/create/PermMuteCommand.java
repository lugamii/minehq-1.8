package dev.lugami.bridge.bukkit.commands.punishment.create;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.PunishmentPacket;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.punishment.Punishment;
import dev.lugami.bridge.global.punishment.PunishmentType;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Flag;
import dev.lugami.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;

public class PermMuteCommand {

    @Command(names = {"pmute", "permmute"}, permission = "bridge.mute.permanent", description = "Permanently mute an user, stopping them from talking in public chat", async = true)
    public static void pMuteCmd(CommandSender s, @Flag(value = {"a", "announce"}, description = "Announce this mute to the server") boolean silent, @Param(name = "target") Profile target, @Param(name = "reason", wildcard = true) String reason) {
        Profile pf = BukkitAPI.getProfile(s);

        if (pf.getActivePunishments(PunishmentType.MUTE).size() > 0) {
            s.sendMessage(ChatColor.RED + target.getUsername() + " is already muted.");
            return;
        }

        if (!BukkitAPI.canOverride(pf, target)) {
            s.sendMessage(ChatColor.RED + "You cannot punish this player.");
            return;
        }

        Punishment punishment = new Punishment(target, pf, BridgeGlobal.getSystemName(), reason, PunishmentType.MUTE, new HashSet<>(),false, !silent, false, Long.MAX_VALUE);
        target.getPunishments().add(punishment);
        pf.getStaffPunishments().add(punishment);
        target.saveProfile();
        pf.saveProfile();
        PacketHandler.sendToAll(new PunishmentPacket(punishment));
    }
}