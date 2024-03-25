package dev.lugami.bridge.bukkit.commands.server.group;

import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.qlib.command.Command;

public class GroupListCommand {

    @Command(names = {"group list"}, permission = "bridge.group.list", description = "List all available server groups", hidden = true)
    public static void groupList(CommandSender s) {
        if (BridgeGlobal.getServerHandler().getGroups() == null) {
            s.sendMessage(ChatColor.RED + "There are no server groups...");
            return;
        }
        FancyMessage m = new FancyMessage(ChatColor.GREEN + "Available Groups: ");
        BridgeGlobal.getServerHandler().getGroups().forEach(serv -> m.then(serv + " ").tooltip(ChatColor.GRAY + "Click to view servers in the group.").command("/group info " + serv));
        m.send(s);
    }
}