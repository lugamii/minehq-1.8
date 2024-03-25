package dev.lugami.bridge.bukkit.commands.server.serverinfo;

import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.qlib.command.Command;

public class ServerInfoListCommand {

    @Command(names = {"serverinfo list"}, permission = "bridge.server.list", description = "List all available server groups", hidden = true)
    public static void serverList(CommandSender s) {
        if (BridgeGlobal.getServerHandler().getServers().isEmpty()) {
            s.sendMessage(ChatColor.RED + "There are no servers found...");
            return;
        }
        FancyMessage m = new FancyMessage(ChatColor.GREEN + "Available Servers: ");
        BridgeGlobal.getServerHandler().getServers().values().forEach(serv -> m.then(serv.getName() + " ").tooltip(ChatColor.GRAY + "Click to view information about the server.").command("/serverinfo info " + serv.getName()));
        m.send(s);
    }
}
