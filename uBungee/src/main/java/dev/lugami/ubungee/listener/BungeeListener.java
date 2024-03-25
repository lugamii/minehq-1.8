package dev.lugami.ubungee.listener;

import dev.lugami.ubungee.commands.HubCommand;
import dev.lugami.ubungee.commands.WhitelistCommand;
import dev.lugami.ubungee.uBungee;
import dev.lugami.ubungee.whitelist.WhitelistType;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.disguise.DisguisePlayer;
import dev.lugami.bridge.global.profile.Profile;
import net.md_5.bungee.BungeeCord;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.ranks.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BungeeListener implements Listener {

    List<UUID> login;

    public BungeeListener(){
        login = new ArrayList<>();
    }

    @EventHandler
    public void onJoin(LoginEvent e){
        WhitelistType whitelist = uBungee.getInstance().getWhitelist();
        if(whitelist != null){
            if(!whitelist.isAllowed(e.getConnection().getUniqueId())){
                if(!uBungee.getInstance().getBypassDomain().isEmpty() && e.getConnection().getVirtualHost().getHostName().contains(uBungee.getInstance().getBypassDomain())) {
                    sendMessageToStaff(ChatColor.DARK_GREEN + "✓ " + GlobalAPI.getPlayerRank(e.getConnection().getUniqueId(), true).getColor() + GlobalAPI.getProfile(e.getConnection().getUniqueId()).getUsername() + ChatColor.GREEN + " has connected using the bypass domain.");
                    return;
                }
                e.setCancelReason(whitelist.getKickMessage());
                e.setCancelled(true);
                sendMessageToStaff(ChatColor.RED + e.getConnection().getName() + " tried to join but isn't whitelisted.");
                return;
            }
        }
        uBungee.getInstance().addDomainID(e.getConnection().getUniqueId(), e.getConnection().getVirtualHost().getHostName());
    }

    @EventHandler
    public void onServerSwitch(ServerConnectEvent e){
        if(e.isCancelled()) return;

        if(e.getPlayer().getServer() == null) {
            if(GlobalAPI.getProfile(e.getPlayer().getUniqueId()) != null) {
                if(GlobalAPI.getPlayerRank(e.getPlayer().getUniqueId(), true).isStaff()) notifyJoin(e.getPlayer(), e);
            }
            e.setTarget(HubCommand.getAvailableServer(e.getPlayer(), GlobalAPI.getProfile(e.getPlayer().getUniqueId()) != null && GlobalAPI.getProfile(e.getPlayer().getUniqueId()).hasPermission("ubungee.restricted")));
        }

        if(GlobalAPI.getProfile(e.getPlayer().getUniqueId()) != null) {
            if(GlobalAPI.getPlayerRank(e.getPlayer().getUniqueId(), true).isStaff()) notifySwitch(e.getPlayer(), e);
        }

    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e){
        if(GlobalAPI.getProfile(e.getPlayer().getUniqueId()) == null) return;
        Rank r = GlobalAPI.getPlayerRank(e.getPlayer().getUniqueId(), true);
        if(r.isStaff()) {
            notifyLeave(e.getPlayer(), GlobalAPI.getPlayerRank(e.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onPing(ProxyPingEvent e){
        ServerPing sp = e.getResponse();
        WhitelistType whitelist = uBungee.getInstance().getWhitelist();
        if(whitelist != null) {
            sp.setDescription(ChatColor.translateAlternateColorCodes('&', uBungee.getInstance().getWhitelistMotdLineOne() + "\n" + uBungee.getInstance().getWhitelistMotdLineTwo()));
            sp.setVersion(new ServerPing.Protocol("Whitelisted", 27875731));
        } else {
            if(uBungee.getInstance().getConfig().getBoolean("cheatbreaker")) {
                sp.setDescription(ChatColor.translateAlternateColorCodes('&', uBungee.getInstance().getMotdLineOne() + "\n" + uBungee.getInstance().getMotdLineTwo()));
                sp.setVersion(new ServerPing.Protocol("CheatBreaker", -1332));
            } else {
                sp.setDescription(ChatColor.translateAlternateColorCodes('&', uBungee.getInstance().getMotdLineOne() + "\n" + uBungee.getInstance().getMotdLineTwo()));
            }
        }
    }

    Set<ProxiedPlayer> uknPlayers = new HashSet<>();
    @EventHandler
    public void onRegister(PluginMessageEvent e) {

        if (uBungee.getInstance().getConfig().getBoolean("cheatbreaker")) {
            if (e.getSender() instanceof ProxiedPlayer && e.getTag() != null) {
                String channel = e.getTag();
                ProxiedPlayer player = (ProxiedPlayer) e.getSender();
                boolean vanilla = true;
                if (channel.contains("REGISTER") || channel.contains("MC|Brand")) {
                    if(!uknPlayers.contains((ProxiedPlayer) e.getSender())) {
                        uknPlayers.add((ProxiedPlayer) e.getSender());
                    }
                    vanilla = true;
                    return;
                }
                boolean cheatbreaker = false;
                boolean lcclient = false;

                if (channel.contains("Lunar-Client")) {
                    lcclient = true;
                    vanilla = cheatbreaker = false;
                    uknPlayers.remove((ProxiedPlayer) e.getSender());
                    ((ProxiedPlayer) e.getSender()).sendData("secretlcchan", ("pls use cheatbreaker and not the skid client").getBytes());

                    e.getSender().disconnect("use cheatbreaker thx");
                    return;
                } else if (channel.contains("CB|IN") || channel.contains("CB-C") || channel.contains("CHEATBREAKER|IN")) {

                    uknPlayers.remove((ProxiedPlayer) e.getSender());
                    cheatbreaker = true;
                    vanilla = lcclient = false;
                    ((ProxiedPlayer) e.getSender()).sendData("secretcbchan", ("ty for using cb").getBytes());
                    return;
                }
                if (!vanilla) {
                    e.getSender().disconnect("use cheatbreaker thx");
                }
            }
        }
    }

    @EventHandler
    public void onPostJoin(PostLoginEvent event) {
        if(uknPlayers.contains(event.getPlayer())) {
            event.getPlayer().disconnect("use cheatbreaker thx");
        }
    }

    public void notifyJoin(ProxiedPlayer p, ServerConnectEvent e) {
        ServerInfo server = null;
        if (e.getTarget() != null) server = e.getTarget();
        Rank r = GlobalAPI.getPlayerRank(p.getUniqueId());
        if (server.getName().contains("Dev") || server.getName().contains("Staging")) {
            sendMessageToStaff(ChatColor.BLUE + "[Staff] " + r.getColor() + p.getName() + ChatColor.GREEN + " joined " + ChatColor.AQUA + "the network (" + ChatColor.RED + (server == null || server.getName() == null ? "N/A" : server.getName()) + ChatColor.AQUA + ")");
        } else {
            sendMessageToStaff(ChatColor.BLUE + "[Staff] " + r.getColor() + p.getName() + ChatColor.GREEN + " joined " + ChatColor.AQUA + "the network (" + (server == null || server.getName() == null ? "N/A" : server.getName()) + ")");
        }
    }

    public void notifyLeave(ProxiedPlayer p, Rank r) {
        ServerInfo server = null;
        if (p.getServer() != null) server = p.getServer().getInfo();
        if (server.getName().contains("Dev") || server.getName().contains("Staging")) {
            sendMessageToStaff(ChatColor.BLUE + "[Staff] " + r.getColor() + p.getName() + ChatColor.RED + " left " + ChatColor.AQUA + "the network (from " + ChatColor.RED + (server == null || server.getName() == null ? "N/A" : server.getName()) + ChatColor.AQUA + ")");
        } else {
            sendMessageToStaff(ChatColor.BLUE + "[Staff] " + r.getColor() + p.getName() + ChatColor.RED + " left " + ChatColor.AQUA + "the network (from " + (server == null || server.getName() == null ? "N/A" : server.getName()) + ")");
        }
    }

    public void notifySwitch(ProxiedPlayer p, ServerConnectEvent e) {
        if (e == null || p == null) return;
        ServerInfo to = e.getTarget();
        Rank fr = GlobalAPI.getPlayerRank(p.getUniqueId());
        if (fr == null || to == null || p.getServer() == null || p.getServer().getInfo() == null) return;
        if (p.getServer().getInfo().getName().equals(to.getName())) return;
        for (ProxiedPlayer player : uBungee.getInstance().getProxy().getPlayers()) {
            Rank r = GlobalAPI.getPlayerRank(player.getUniqueId(), true);
            if (r.isStaff()) {
                if (player.getServer() != null && player.getServer().getInfo().getName().equals(to.getName()) && p != player) {
                    ServerInfo server = p.getServer().getInfo();
                    if (server.getName().contains("Dev") || server.getName().contains("Staging")) {
                        player.sendMessage(ChatColor.BLUE + "[Staff] " + fr.getColor() + p.getName() + ChatColor.AQUA + " joined your server (from " + ChatColor.RED + (server == null || server.getName() == null ? "N/A" : server.getName()) + ChatColor.AQUA + ")");
                    } else {
                        player.sendMessage(ChatColor.BLUE + "[Staff] " + fr.getColor() + p.getName() + ChatColor.AQUA + " joined your server (from " + (server == null || server.getName() == null ? "N/A" : server.getName()) + ")");
                    }
                }
                if (player.getServer() != null && player.getServer().getInfo().getName().equals(p.getServer().getInfo().getName()) && p != player) {
                    if (to.getName().contains("Dev") || to.getName().contains("Staging")) {
                        player.sendMessage(ChatColor.BLUE + "[Staff] " + fr.getColor() + p.getName() + ChatColor.AQUA + " left your server (to " + ChatColor.RED + (to.getName() == null || to.getName() == null ? "N/A" : to.getName()) + ChatColor.AQUA + ")");
                    } else {
                        player.sendMessage(ChatColor.BLUE + "[Staff] " + fr.getColor() + p.getName() + ChatColor.AQUA + " left your server (to " + (to == null || to.getName() == null ? "N/A" : to.getName()) + ")");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKick(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        boolean restricted = GlobalAPI.getProfile(player.getUniqueId()) != null && GlobalAPI.getProfile(player.getUniqueId()).hasPermission("ubungee.restricted");
        if(event.getKickReason().contains("from " + BridgeGlobal.getServerDisplayName()) && event.getKickedFrom().getName().contains("hub")) return;
        ServerInfo kickTo = HubCommand.getAvailableServer(player, restricted);
        if(event.getKickedFrom() == kickTo) return;
        event.setCancelServer(kickTo);
        event.setCancelled(true);
        BungeeCord.getInstance().getScheduler().schedule(uBungee.getInstance(), () -> {
            player.sendMessage(ChatColor.RED +
                    "Kicked from "+ ChatColor.GOLD + event.getKickedFrom().getName()+ ChatColor.RED + ": §r" + event.getKickReason());
        }, 1, TimeUnit.SECONDS);


    }


    public void sendMessageToStaff(String message) {
        uBungee.getInstance().getProxy().getConsole().sendMessage(message);

        for(ProxiedPlayer player : uBungee.getInstance().getProxy().getPlayers()) {
            Rank rank = GlobalAPI.getPlayerRank(player.getUniqueId(), true);
            if(rank == null || !rank.isStaff()) continue;
            if(message.contains("whitelisted.")) {
                if(!WhitelistCommand.getUuidList().contains(player.getUniqueId())) player.sendMessage(message);
            }else {
                player.sendMessage(message);
            }
        }
    }

}