package dev.lugami.uhub.commands;

import dev.lugami.uhub.uHub;
import dev.lugami.qlib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ConfigReloadCommand {

    @Command(names = {"uhreload"}, permission = "op", hidden = true)
    public static void config(CommandSender sender) {

            uHub.getInstance().reloadConfig();
            uHub.getInstance().saveConfig();
            sender.sendMessage(ChatColor.GREEN + "uHub Configuration Reloaded.");
        }
    }