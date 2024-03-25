package dev.lugami.bridge.bukkit.commands.punishment.remove;

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

public class UnBanCommand {

    @Command(names = {"unban"}, permission = "bridge.unban", description = "Remove a player's ban", async = true)
    public static void unbanCmd(CommandSender s, @Flag(value = {"a", "announce"}, description = "Announce this unban to the server") boolean silent, @Param(name = "target") Profile target, @Param(name = "reason", wildcard = true) String reason) {
        Profile pf = BukkitAPI.getProfile(s);
        if (target.getActivePunishments(PunishmentType.BAN).isEmpty()) {
            s.sendMessage(ChatColor.RED + target.getUsername() + " is not currently banned.");
            return;
        }

        Punishment punishment = (Punishment) target.getActivePunishments(PunishmentType.BAN).toArray()[0];

        if (punishment.isIP() && !s.hasPermission("bridge.unbanip")) {
            s.sendMessage(ChatColor.RED + "You cannot unban " + target.getUsername() + " because they are ip-banned.");
            return;
        }

        if (!BukkitAPI.canOverride(pf, punishment.getExecutor())) {
            s.sendMessage(ChatColor.RED + "You cannot undo this punishment.");
            return;
        }

        punishment.pardon(pf, BridgeGlobal.getSystemName(), reason, !silent);
        target.getPunishments().add(punishment);
        target.saveProfile();
        PacketHandler.sendToAll(new PunishmentPacket(punishment));
    }
}
