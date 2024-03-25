package dev.lugami.ubungee.commands;

import dev.lugami.ubungee.uBungee;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;

public class MOTDCommand extends Command {

    public MOTDCommand() {
        super("motd");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            Profile profile = GlobalAPI.getProfile(((ProxiedPlayer)commandSender).getUniqueId());
            if(!uBungee.getInstance().hasPermission(profile, "ubungee.motd")) {
                p.sendMessage(ChatColor.RED + "No permission.");
                return;
            }

            if(args.length < 1) {
                p.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                p.sendMessage(ChatColor.RED + "/motd edit <line> <input...>");
                p.sendMessage(ChatColor.RED + "/motd preview");
                p.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                return;
            }
            switch(args[0].toLowerCase()) {
                case "edit": {
                    if(args.length < 2) {
                        p.sendMessage(ChatColor.RED + "Usage: /motd edit <line> <input...>");
                        return;
                    }
                    String joined = ChatColor.translateAlternateColorCodes('&', StringUtils.join(args, ' ', 2, args.length));
                    switch(args[1]) {
                        case "1": {
                            uBungee.getInstance().getConfig().set("motd.1", joined);
                            uBungee.getInstance().saveConfig();
                            p.sendMessage(ChatColor.GREEN + "Successfully updated the MOTD!");
                            preview(commandSender);
                            break;
                        }
                        case "2": {
                            uBungee.getInstance().getConfig().set("motd.2", joined);
                            uBungee.getInstance().saveConfig();
                            p.sendMessage(ChatColor.GREEN + "Successfully updated the MOTD!");
                            preview(commandSender);
                            break;
                        }
                        default: {
                            p.sendMessage(ChatColor.RED + "Invalid line number, 1 or 2.");
                            break;
                        }
                    }
                }

                case "preview": {
                    preview(commandSender);
                    break;
                }
            }
        }
    }

    public void preview(CommandSender sender) {
        sender.sendMessage(uBungee.getInstance().getMotdLineOne());
        sender.sendMessage(uBungee.getInstance().getMotdLineTwo());
    }

}