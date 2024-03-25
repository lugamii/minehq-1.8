package dev.lugami.bridge.bukkit.commands.server;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.lugami.bridge.global.packet.PacketHandler;
import dev.lugami.bridge.global.packet.types.NetworkBroadcastPacket;
import dev.lugami.bridge.global.packet.types.ServerShutdownPacket;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class ShutdownCommand {

    @Command(names = {"shutdown"}, permission = "bridge.server.shutdown", description = "Shutdown a server", hidden = true)
    public static void shutdown(Player s, @Param(name = "server") String serverName) {
        PacketHandler.sendToAll(new ServerShutdownPacket(serverName));
        if (serverName.equalsIgnoreCase("all")) {
            PacketHandler.sendToAll(new NetworkBroadcastPacket("basic.staff", "&8[&eServer Monitor&8] &fRemoving all servers..."));
        } else {
            PacketHandler.sendToAll(new NetworkBroadcastPacket("basic.staff", "&8[&eServer Monitor&8] &fRemoving server " + serverName + "..."));
        }
    }
}
