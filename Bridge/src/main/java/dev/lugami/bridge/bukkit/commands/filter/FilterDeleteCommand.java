package dev.lugami.bridge.bukkit.commands.filter;

import dev.lugami.bridge.bukkit.parameters.packets.filter.FilterDeletePacket;
import dev.lugami.bridge.global.filter.Filter;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import dev.lugami.qlib.xpacket.FrozenXPacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class FilterDeleteCommand {

    @Command(names = "filter delete", permission = "bridge.filter", description = "Delete a filter", hidden = true)
    public static void delete(CommandSender sender, @Param(name = "filter", wildcard = true) Filter filter) {
        filter.delete();
        FrozenXPacketHandler.sendToAll(new FilterDeletePacket(filter, Bukkit.getServerName()));
        sender.sendMessage(ChatColor.GREEN + "Successfully deleted the filter.");
    }
}
