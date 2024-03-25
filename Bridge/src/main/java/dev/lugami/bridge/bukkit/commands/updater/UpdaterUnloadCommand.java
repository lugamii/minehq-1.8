package dev.lugami.bridge.bukkit.commands.updater;

import dev.lugami.bridge.bukkit.util.PluginUtil;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class UpdaterUnloadCommand {

    @Command(names = "updater unload", permission = "bridge.updater", hidden = true, description = "Unloads a plugin")
    public static void load(CommandSender sender, @Param(name = "plugin", wildcard = true) Plugin plugin) {
        sender.sendMessage(PluginUtil.unload(plugin));
    }
}
