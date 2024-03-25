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

public class BlacklistCommand {

    @Command(names = {"blacklist", "bl"}, permission = "bridge.blacklist", description = "Blacklist an user from the network. This type of punishment cannot be appealed", async = true)
    public static void blacklistCmd(CommandSender s, @Flag(value = {"a", "announce"}, description = "Announce this blacklist to the server") boolean silent, @Flag(value = {"c", "clear"}, description = "Clear the player's inventory") boolean clear, @Param(name = "target") Profile target, @Param(name = "reason", wildcard = true) String reason) {
        Profile pf = BukkitAPI.getProfile(s);
        if (target.getCurrentIPAddress() == null) {
            s.sendMessage(ChatColor.RED + "We can't blacklist a player that never joined before.");
            return;
        }

        if (target.getActivePunishments(PunishmentType.BLACKLIST).size() > 1) {
            s.sendMessage(ChatColor.RED + target.getUsername() + " is already blacklisted.");
            return;
        }

        if (!BukkitAPI.canOverride(pf, target)) {
            s.sendMessage(ChatColor.RED + "You cannot punish this player.");
            return;
        }

        Punishment punishment = new Punishment(target, pf, BridgeGlobal.getSystemName(), reason, PunishmentType.BLACKLIST, new HashSet<>(), true, !silent, clear, Long.MAX_VALUE);
        target.getPunishments().add(punishment);
        pf.getStaffPunishments().add(punishment);
        target.saveProfile();
        pf.saveProfile();
        PacketHandler.sendToAll(new PunishmentPacket(punishment));
    }
}
