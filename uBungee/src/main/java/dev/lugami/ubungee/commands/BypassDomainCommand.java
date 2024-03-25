package dev.lugami.ubungee.commands;

import dev.lugami.ubungee.uBungee;
import org.apache.commons.lang3.StringUtils;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BypassDomainCommand extends Command {

    public BypassDomainCommand() {
        super("bypassdomain");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (s instanceof ProxiedPlayer){
            Profile profile = GlobalAPI.getProfile(((ProxiedPlayer) s).getUniqueId());
            if(!uBungee.getInstance().hasPermission(profile, "ubungee.bypassdomain")) {
                s.sendMessage(ChatColor.RED + "No permission.");
                return;
            }
        }
        if (args.length < 1) {
            s.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            s.sendMessage(ChatColor.RED + "/bypassdomain <domain>");
            s.sendMessage(ChatColor.RED + "/bypassdomain clear");
            s.sendMessage(ChatColor.RED + "/bypassdomain view");
            s.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            return;
        }

        if(args[0].equalsIgnoreCase("clear")) {
            if(uBungee.getInstance().getBypassDomain().isEmpty()) {
                s.sendMessage(ChatColor.RED + "There was no bypass domain to clear...");
                return;
            }
            uBungee.getInstance().setBypassDomain("");
            s.sendMessage(ChatColor.GREEN + "Successfully cleared the bypass domain.");
            return;
        }
        if(args[0].equalsIgnoreCase("view")) {
            if(uBungee.getInstance().getBypassDomain().isEmpty()) {
                s.sendMessage(ChatColor.RED + "There is no bypass domain setup.");
                return;
            }
            s.sendMessage(ChatColor.GOLD + "Current Bypass Domain: " + ChatColor.LIGHT_PURPLE + uBungee.getInstance().getBypassDomain());
            return;
        }

        uBungee.getInstance().setBypassDomain(args[0]);
        s.sendMessage(ChatColor.GREEN + "Successfully set the bypass domain to: " + ChatColor.LIGHT_PURPLE + args[0]);
        return;

    }

}