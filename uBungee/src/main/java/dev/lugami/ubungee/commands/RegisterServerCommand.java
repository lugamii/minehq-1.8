package dev.lugami.ubungee.commands;

import dev.lugami.ubungee.uBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;

import java.net.InetSocketAddress;

public class RegisterServerCommand extends Command {

    public RegisterServerCommand() {
        super("registerserver");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (s instanceof ProxiedPlayer) {
            Profile profile = GlobalAPI.getProfile(((ProxiedPlayer) s).getUniqueId());
            if (!uBungee.getInstance().hasPermission(profile, "ubungee.registerserver")) {
                s.sendMessage(ChatColor.RED + "No permission.");
                return;
            }
        }

        if (args.length < 2) {
            s.sendMessage(ChatColor.RED + "Usage: /registerserver <name> <ip>");
            return;
        }

        String name = args[0];
        String ip = args[1];

        String host = ip.contains(":") ? ip.split(":")[0] : ip;
        int port = ip.contains(":") ? Integer.parseInt(ip.split(":")[1]) : 25565;

        if (ProxyServer.getInstance().getServerInfo(name) != null) {
            s.sendMessage(ChatColor.RED + "A server already exists with this name!");
            return;
        }

        ProxyServer.getInstance().getServers().put(name, ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(host, port), "", false));

        s.sendMessage(ChatColor.GREEN + "Successfully added the server \"" + name + "\". (" + ip + ")");
        return;

    }
}