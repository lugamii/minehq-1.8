package dev.lugami.bridge.bukkit.commands.updater;

import dev.lugami.bridge.bukkit.util.PluginUtil;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class UpdaterEnableCommand {

    @Command(names = "updater enable", permission = "bridge.updater", hidden = true, description = "Enables a specific plugin")
    public static void enable(CommandSender sender, @Param(name = "plugin") Plugin plugin) {
        PluginUtil.enable(plugin);
        sender.sendMessage(ChatColor.GREEN + "Enabled " + plugin.getName() + ".");
    }

    @Command(names = "updater enable all", permission = "bridge.updater", hidden = true, description = "Enables all plugins")
    public static void enableall(CommandSender sender) {
        PluginUtil.enableAll();
        sender.sendMessage(ChatColor.GREEN + "Enabled all plugins.");
    }
}
