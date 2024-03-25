package dev.lugami.bridge.bukkit.commands.punishment;

import org.bukkit.command.CommandSender;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.bukkit.commands.punishment.menu.MainPunishmentMenu;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.PunishmentPacket;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.punishment.Punishment;
import dev.lugami.bridge.global.punishment.PunishmentType;
import dev.lugami.qlib.command.Flag;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

import java.util.HashSet;

public class ClearPunishmentsCommand {

    @Command(names = {"clearpunishments", "clearhistory"}, permission = "bridge.clearpunishments", description = "Clear player's punishments from the entire network", async = true)
    public static void clearPunishments(CommandSender s, @Param(name = "target") Profile target) {

        if (target.getPunishments().isEmpty()) {
            s.sendMessage(ChatColor.RED + target.getUsername() + " does not have any punishments.");
            return;
        }
        s.sendMessage(ChatColor.GREEN + "Successfully cleared " + target.getCurrentGrant().getRank().getColor() + target.getUsername() + ChatColor.GREEN + "'s punishments.");
        target.getPunishments().clear();
        target.saveProfile();
    }
}

