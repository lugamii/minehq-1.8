package dev.lugami.ubungee.commands;

import com.google.common.collect.ImmutableSet;
import dev.lugami.ubungee.uBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.ranks.Rank;

import java.util.HashSet;
import java.util.Set;

public class PullCommand extends Command implements TabExecutor {

    public PullCommand() {
        super("pull");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            Rank r = GlobalAPI.getPlayerRank(p.getUniqueId(), true);
            if(r.isStaff()){
                if(args.length == 0){
                    p.sendMessage("§cUsage: /pull <player>");
                } else if (args.length == 1) {
                    ServerInfo si = uBungee.getInstance().getProxy().getServerInfo(p.getServer().getInfo().getName());
                    ProxiedPlayer on = uBungee.getInstance().getProxy().getPlayer(args[0]);
                    Rank rank = GlobalAPI.getPlayerRank(on.getUniqueId(), true);
                    if(si != null){
                        p.sendMessage(ChatColor.GREEN + "Sending cross-bungee pull request...");
                        p.sendMessage("§ePulling " + rank.getColor() + on.getName() + "§e...");
                        on.connect(si);
                    } else {
                        p.sendMessage("§cNo server by the name \"" + args[0] + "\"§c found.");
                    }
                }
            } else {
                p.sendMessage("§cNo permission.");
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length > 2 || args.length == 0) {
            return ImmutableSet.of();
        }
        Set<String> matches = new HashSet<>();
        if (args.length == 1) {
            String search = args[0].toLowerCase();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().toLowerCase().startsWith(search)) {
                    matches.add( player.getName());
                }
            }
        }
        return matches;
    }
}
