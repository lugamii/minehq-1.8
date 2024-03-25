package dev.lugami.bridge.bukkit.commands.filter;

import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.filter.Filter;
import dev.lugami.bridge.global.filter.FilterAction;
import dev.lugami.bridge.global.util.TimeUtil;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class FilterTestCommand {

    @Command(names = "filter test", permission = "bridge.filter", description = "Test a filter", hidden = true)
    public static void test(CommandSender sender, @Param(name = "message", wildcard = true) String message) {
        Filter filter = BridgeGlobal.getFilterHandler().isViolatingFilter(message);
        if(filter == null) {
            sender.sendMessage(ChatColor.GREEN + "This message is not filtered.");
            return;
        }

        sender.sendMessage(ChatColor.RED + "Your message currently flags for the filter: " + filter.getPattern());
        if(filter.getFilterAction() == FilterAction.MUTE) sender.sendMessage(ChatColor.RED + "This message would of caused you to be muted for " + TimeUtil.millisToTimer(filter.getMuteTime()));
    }
}
