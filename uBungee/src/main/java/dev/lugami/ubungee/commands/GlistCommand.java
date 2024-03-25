package dev.lugami.ubungee.commands;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;
import dev.lugami.bridge.global.ranks.Rank;

public class GlistCommand extends Command {

    public GlistCommand() {
        super("glist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p = (ProxiedPlayer) sender;
        Rank r = GlobalAPI.getPlayerRank(p.getUniqueId(), true);
        if (!r.isStaff()) {
            p.sendMessage(ChatColor.RED + "No permission.");
            return;
        }
        sender.sendMessage(new TextComponent(ChatColor.GREEN + "Sending cross-bungee list request..."));
        final List<ServerInfo> offlineServers = new ArrayList<>();
        final List<ServerInfo> onlineServers = new ArrayList<>();
        List<String> offlineServerNames = new ArrayList<>();
        for ( final ServerInfo server : ProxyServer.getInstance().getServers().values() )
        {

            try {
                Socket s = new Socket();
                s.connect(new InetSocketAddress(server.getAddress().getAddress(), server.getAddress().getPort()), 10);
                s.close();
                onlineServers.add(server);
            } catch (UnknownHostException e) {
                offlineServers.add(server);
            } catch(SocketTimeoutException e){
                offlineServers.add(server);
            } catch (IOException e) {
                offlineServers.add(server);
            }
            if(onlineServers.contains(server)) {

                List<String> players = new ArrayList<>();
                for (ProxiedPlayer player : server.getPlayers()) {
                    players.add(player.getDisplayName());
                }
                Collections.sort(players, String.CASE_INSENSITIVE_ORDER);

                sender.sendMessage(ChatColor.GOLD + "[" + server.getName() + "] " + ChatColor.WHITE + "(" + server.getPlayers().size() + ")" + (server.getPlayers().isEmpty() ? "" : " [" + StringUtils.join(orderedProfiles(server).stream().map(profile -> GlobalAPI.getColor(profile) + profile.getUsername()).collect(Collectors.toList()), ChatColor.WHITE + ", ") + ChatColor.WHITE + "]"));
            }
            for(ServerInfo serverInfo : offlineServers){
                if(!offlineServerNames.contains(serverInfo.getName())) {
                    offlineServerNames.add(serverInfo.getName());
                }
            }
        }
        sender.sendMessage(new TextComponent(ChatColor.RED + "Offline Servers: " + ChatColor.WHITE + offlineServerNames));


        sender.sendMessage(ChatColor.GOLD + "Total Online: " + ChatColor.WHITE + BungeeCord.getInstance().getPlayers().size());
    }

    private List<Profile> orderedProfiles(ServerInfo serverInfo) {
        List<Profile> profiles = new ArrayList<>();
        serverInfo.getPlayers().forEach(onlinePlayer -> profiles.add(GlobalAPI.getProfile(onlinePlayer.getUniqueId())));
        profiles.sort(Comparator.comparingInt(profile -> GlobalAPI.getPlayerRank((Profile)profile).getPriority()).reversed());
        return profiles;
    }
}