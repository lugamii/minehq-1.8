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
import org.apache.commons.lang3.StringUtils;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.ranks.Rank;

import java.util.HashSet;
import java.util.Set;


public class SendCommand extends Command implements TabExecutor {

    public SendCommand() {
        super("send");
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
        if(args.length < 2){
            commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            commandSender.sendMessage("§c/send <player> <server>");
            commandSender.sendMessage("§c/send <server> <server>");
            commandSender.sendMessage("§c/send all <server>");
            commandSender.sendMessage(ChatColor.BLUE.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 35));
            return;
        }
        ServerInfo server = uBungee.getInstance().getProxy().getServerInfo(args[1]);

        if(server == null){
            commandSender.sendMessage("§cNo server by the name \"" + args[1] + "\" §cfound.");
            return;
        }

        // Send all players to a server

        if(args[0].equalsIgnoreCase("all")){
            if(args.length < 2) {
                commandSender.sendMessage(ChatColor.RED + "Usage: /send all <server>");
                return;
            }
            for(ProxiedPlayer p : uBungee.getInstance().getProxy().getPlayers()){
                p.connect(server);
            }

            commandSender.sendMessage(ChatColor.GREEN + "Sending cross-bungee send request...");
            commandSender.sendMessage("§6Successfully sent §feveryone §6to §f" + server.getName() + "§6.");
            return;
        }

        // Send specific player to a server

        ProxiedPlayer on = uBungee.getInstance().getProxy().getPlayer(args[0]);

        if (on != null) {
            on.connect(server);
            Rank r = GlobalAPI.getPlayerRank(on.getUniqueId(), true);
            commandSender.sendMessage(ChatColor.GREEN + "Sending cross-bungee send request...");
            commandSender.sendMessage("§6Successfully sent §f" + r.getColor() + on.getName() + " §6to §f" + server.getName() + "§6.");
            return;
        }

        // Send players from one server to another

        ServerInfo theServer = uBungee.getInstance().getProxy().getServerInfo(args[0]);

        if (theServer == null){
            commandSender.sendMessage("§cNo server by the name \"" + args[0] + "\" §cfound.");
            return;
        }

        int count = 0;
        for(ProxiedPlayer p : theServer.getPlayers()){
            count++;
            p.connect(server);
        }

        commandSender.sendMessage(ChatColor.GREEN + "Sending cross-bungee send request...");
        commandSender.sendMessage("§6Successfully sent §f" + count + " players §6to §f" + server.getName() + "§6.");
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
            if ("all".startsWith(search)) {
                matches.add("all");
            }
            for (String server : ProxyServer.getInstance().getServers().keySet()) {
                if (server.toLowerCase().startsWith(search)) {
                    matches.add(server);
                }
            }
        } else {
            String search = args[1].toLowerCase();
            for (String server : ProxyServer.getInstance().getServers().keySet()) {
                if (server.toLowerCase().startsWith(search)) {
                    matches.add(server);
                }
            }
        }
        return matches;
    }
}
