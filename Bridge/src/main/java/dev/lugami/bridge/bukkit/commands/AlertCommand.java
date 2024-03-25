package dev.lugami.bridge.bukkit.commands;

import org.bukkit.command.CommandSender;
import dev.lugami.bridge.bukkit.parameters.AlertPacket;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import dev.lugami.qlib.xpacket.FrozenXPacketHandler;

public class AlertCommand {

    @Command(names = "alert", permission = "bridge.alert", description = "Alert a message to the network", async = true)
    public static void alert(CommandSender sender, @Param(name = "message", wildcard = true) String msg) {
            FrozenXPacketHandler.sendToAll(new AlertPacket(msg, false));
        }

    @Command(names = "rawalert", permission = "bridge.alert", description = "Alert a raw message to the network", async = true)
    public static void rawalert(CommandSender sender, @Param(name = "message", wildcard = true) String msg) {
            FrozenXPacketHandler.sendToAll(new AlertPacket(msg, true));
        }
    }