package dev.lugami.bridge.bukkit.commands.punishment.create;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.BukkitAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.PunishmentPacket;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.punishment.Punishment;
import dev.lugami.bridge.global.punishment.PunishmentType;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Flag;
import dev.lugami.qlib.command.Param;

import java.util.HashSet;

public class BanCommand {

    @Command(names = {"ban", "b", "banish"}, permission = "bridge.ban", description = "Ban a player from the network", async = true)
    public static void banCmd(CommandSender s, @Flag(value = {"a", "announce"}, description = "Announce this ban to the server") boolean silent, @Flag(value = {"c", "clear"}, description = "Clear the player's inventory") boolean clear, @Param(name = "target") Profile target, @Param(name = "reason", wildcard = true) String reason) {
        Profile pf = BukkitAPI.getProfile(s);

        if (target.getActivePunishments(PunishmentType.BAN).size() > 1) {
            s.sendMessage(ChatColor.RED + target.getUsername() + " is already banned.");
            return;
        }

        if (!BukkitAPI.canOverride(pf, target)) {
            s.sendMessage(ChatColor.RED + "You cannot punish this player.");
            return;
        }
        //empty list instead of int instantly as null
        Punishment punishment = new Punishment(target, pf, BridgeGlobal.getSystemName(), reason, PunishmentType.BAN, new HashSet<>(), false, !silent, clear, Long.MAX_VALUE);
        target.getPunishments().add(punishment);
        pf.getStaffPunishments().add(punishment);
        target.saveProfile();
        pf.saveProfile();
        PacketHandler.sendToAll(new PunishmentPacket(punishment));
    }
}
