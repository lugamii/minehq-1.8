package dev.lugami.ubungee.commands;

import com.google.common.collect.ImmutableSet;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.ranks.Rank;
import dev.lugami.ubungee.listener.CBListener;
import dev.lugami.ubungee.uBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("all")
public class CBICommand extends Command  implements TabExecutor {

    public CBICommand() {
        super("cbi");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            Rank r = GlobalAPI.getPlayerRank(p.getUniqueId(), true);
            if (r.getPriority() < 75) {
                p.sendMessage("§cNo permission.");
                return;
            }
        }
        if(args.length < 1){
            commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            commandSender.sendMessage("§c/cbi <player>");
            commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            return;
        }

        ProxiedPlayer on = uBungee.getInstance().getProxy().getPlayer(args[0]);

        if (on != null) {
            if(CBListener.getLcPlayers().contains(on) || CBListener.getCbPlayers().contains(on)) {
               if(CBListener.getLcPlayers().contains(on)) {
                   commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                   commandSender.sendMessage("§a" + on.getName() + " is on Lunar Client");
                   commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                   return;
               } else if (CBListener.getCbPlayers().contains(on)) {
                   commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                   commandSender.sendMessage("§a" + on.getName() + " is on CheatBreaker");
                   commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                   return;
               }
            } else {
                commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                commandSender.sendMessage("§c" + on.getName() + " is §c§lNOT §con CheatBreaker");
                commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
                return;
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length > 2 || args.length == 0) {
            return ImmutableSet.of();
        }
        Set<String> matches = new HashSet<>();
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            matches.add(p.getName());
        }
        return matches;
    }

}