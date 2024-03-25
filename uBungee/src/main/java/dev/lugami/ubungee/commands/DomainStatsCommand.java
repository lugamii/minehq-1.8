package dev.lugami.ubungee.commands;

import dev.lugami.ubungee.uBungee;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;

public class DomainStatsCommand extends Command {

    public DomainStatsCommand() {
        super("domainstats");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (s instanceof ProxiedPlayer){
            Profile profile = GlobalAPI.getProfile(((ProxiedPlayer) s).getUniqueId());
            if(!uBungee.getInstance().hasPermission(profile, "ubungee.domainstats")) {
                s.sendMessage(ChatColor.RED + "No permission.");
                return;
            }
        }
        s.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 37));
        s.sendMessage(ChatColor.BLUE + "Domain Stats " + ChatColor.GRAY + "[Since Last Reboot]");
        uBungee.getInstance().getDomainConnection().forEach(((s1, uuids) -> {
            s.sendMessage(ChatColor.YELLOW + s1 + ": " + ChatColor.GREEN + uuids.size());
        }));

        s.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 37));
        return;

    }

}