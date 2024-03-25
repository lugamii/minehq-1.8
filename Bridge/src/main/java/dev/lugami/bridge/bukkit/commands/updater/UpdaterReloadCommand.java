package dev.lugami.bridge.bukkit.commands.updater;

import dev.lugami.bridge.bukkit.util.PluginUtil;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class UpdaterReloadCommand {

    @Command(names = "updater reload", permission = "bridge.updater", hidden = true, description = "Reloads a specific plugin")
    public static void reload(CommandSender sender, @Param(name = "plugin") Plugin plugin) {
        PluginUtil.reload(plugin);
        sender.sendMessage(ChatColor.GREEN + "Reloaded " + plugin.getName() + ".");
    }

    @Command(names = "updater reload all", permission = "bridge.updater", hidden = true, description = "Reloads all plugins")
    public static void reloadall(CommandSender sender) {
        PluginUtil.reloadAll();
        sender.sendMessage(ChatColor.GREEN + "Reloaded all plugins.");
    }
}
