package dev.lugami.ubungee.commands;

import dev.lugami.ubungee.uBungee;
import dev.lugami.bridge.global.status.BridgeServer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import dev.lugami.bridge.BridgeGlobal;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            ServerInfo si = getAvailableServer(p, true);

            boolean hasPermission = GlobalAPI.getProfile(p.getUniqueId()) != null && uBungee.getInstance().hasPermission(GlobalAPI.getProfile(p.getUniqueId()), "ubungee.restricted");

            if(!si.getName().endsWith("-Lobby") && hasPermission) {
                if(args.length == 0) {
                    p.sendMessage("§6Sending you to Restricted-Hub...");
                    si = getAvailableServer(p, true);
                    if(si == null) return;
                    p.connect(si);
                    return;
                }
                try {
                    int hub = Integer.parseInt(args[0]);
                    String hubName = "Hub-" + hub;
                    BridgeServer bridgeServer = BridgeGlobal.getServerHandler().getServer(hubName);
                    if(bridgeServer == null || !bridgeServer.isOnline()) {
                        p.sendMessage(ChatColor.RED + "That hub is currently unavailable.");
                        return;
                    }
                    p.connect(uBungee.getInstance().getProxy().getServerInfo(hubName));
                    p.sendMessage("§6Sending you to Hub-" + hub + "...");
                } catch(Exception e) {
                    p.sendMessage("§cThere are no hubs currently available.");
                }
                return;
            }

            si = getAvailableServer(p, false);
            if(si == null) return;
            p.sendMessage("§6Sending you to " + si.getName() + "...");
            p.connect(si);
        }
    }

    public static ServerInfo getAvailableServer(ProxiedPlayer player, boolean restricted) {
        Profile profile = GlobalAPI.getProfile(player.getUniqueId()); // Get the executors Bridge Profile.
        ServerInfo serverInfo; // Variable that we can edit during checks

        /*
                Connect to a Game Lobby Logic
                First we check if their server isn't null and that their server contains a '-' and doesn't contain a 'player'
                This method isn't very safe for checking if in a game server - but it will do for now.
                If the check passed we then check if the server is online using Bridge's Server Handler
         */
        if(player.getServer() != null) {
            String from = player.getServer().getInfo().getName();
            if (from != null && from.contains("-") && !from.endsWith("-Lobby")) {
                String gameMode = from.split("-")[0];
                BridgeServer bridgeServer = BridgeGlobal.getServerHandler().getServer(gameMode + "-Lobby");
                if (bridgeServer != null && bridgeServer.isOnline()) { // Check if Game Lobby exists and is online
                    return uBungee.getInstance().getProxy().getServerInfo(gameMode + "-Lobby");
                }
            }
        }

        /*
                Connect to Restricted Hub Logic
                First we check if there is a server registered in BungeeCord called 'Restricted-Hub'
                Then we check if their Bridge Profile isn't null and that they have the permission: 'ubungee.restricted'
                If the check passed we then check if the server is online using Bridge's Server Handler
         */
        if(restricted) {
            ServerInfo restrictedHub = BungeeCord.getInstance().getServerInfo("Restricted-Hub");
            if(restrictedHub != null) {
                if(profile != null && uBungee.getInstance().hasPermission(profile, "ubungee.restricted")) {
                    BridgeServer bridgeServer = BridgeGlobal.getServerHandler().getServer("Restricted-Hub");
                    if(bridgeServer != null && bridgeServer.isOnline()) return restrictedHub;
                }
            }
        }


        /*
                Connect to a Random Hub Logic
                First we go through a list of Registered Servers in BungeeCord matching the naming scheme
                of 'Hub-*', then we check if the server is available for connecting via Bridge's Server Handler
                if all previous requirements are met, we set the variable and return the server.
                Then we get a random hub by using the Random class to get a random integer between a range.
         */
        List<ServerInfo> servers = BungeeCord.getInstance().getServers().values().stream().filter(serverInfo1 -> {
            if(serverInfo1.getName().startsWith("Hub-")) {
                BridgeServer bridgeServer = BridgeGlobal.getServerHandler().getServer(serverInfo1.getName());
                return bridgeServer != null && bridgeServer.isOnline();
            }
            return false;
        }).collect(Collectors.toList());

        Random rand = new Random();
        try {
           // int generatedInteger = 1 + (int) (new Random().nextFloat() * (servers.size() - 1));
            serverInfo = servers.get(rand.nextInt(servers.size()));
        }catch (Exception e) {
            player.sendMessage(ChatColor.RED + "We are unable to find you a suitable hub.");
            return null;
        }

        return serverInfo;
    }
}