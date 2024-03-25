package dev.lugami.bridge.bukkit.commands.updater;

import dev.lugami.bridge.bukkit.util.PluginUtil;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;
import org.bukkit.command.CommandSender;

public class UpdaterLoadCommand {

    @Command(names = "updater load", permission = "bridge.updater", hidden = true, description = "Loads a plugin")
    public static void load(CommandSender sender, @Param(name = "fileName", wildcard = true) String fileName) {
        sender.sendMessage(PluginUtil.load(fileName));
    }
}
