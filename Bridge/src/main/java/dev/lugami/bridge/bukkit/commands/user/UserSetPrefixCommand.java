package dev.lugami.bridge.bukkit.commands.user;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.qlib.command.Command;
import dev.lugami.qlib.command.Param;

public class UserSetPrefixCommand {

    @Command(names = {"user setprefix"}, permission = "bridge.user", description = "Set a players prefix", async = true, hidden = true)
    public static void UserSetPrefixCmd(CommandSender s, @Param(name = "player") Profile pf, @Param(name = "prefix", wildcard = true) String prefix) {
        String tag = ChatColor.translateAlternateColorCodes('&', prefix);
        if (prefix.equals("clear")) tag = "";
        pf.setPrefix(tag);
        pf.saveProfile();
        s.sendMessage("§aSuccessfully " + (tag.equals("") ? "cleared" : "set") + " the prefix of " + pf.getUsername() + (!tag.equals("") ? " to " + tag : ""));
    }
}
