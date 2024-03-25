package dev.lugami.ubungee.commands;

import dev.lugami.ubungee.uBungee;
import dev.lugami.bridge.global.GlobalAPI;
import dev.lugami.bridge.global.profile.Profile;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnRegsiterServerCommand extends Command {

    public UnRegsiterServerCommand() {
        super("unregisterserver");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (s instanceof ProxiedPlayer){
            Profile profile = GlobalAPI.getProfile(((ProxiedPlayer) s).getUniqueId());
            if(!uBungee.getInstance().hasPermission(profile, "ubungee.unregisterserver")) {
                s.sendMessage(ChatColor.RED + "No permission.");
                return;
            }
        }

        if (args.length < 2) {
            s.sendMessage(ChatColor.RED + "Usage: /unregisterserver <name>");
            return;
        }

        String name = args[0];

        ServerInfo info = ProxyServer.getInstance().getServerInfo(name);

        if (info == null) {
            s.sendMessage("§cNo server by the name \"" + name + "\" §cfound.");
            return;
        }

        if (!info.getPlayers().isEmpty() && info.getPlayers().stream().anyMatch(this::isNotStaff)) {
            s.sendMessage("§cThe server has to be empty to remove it!");
            return;
        }

        ProxyServer.getInstance().getServers().remove(info.getName());

        s.sendMessage(ChatColor.GREEN + "Successfully removed the server \"" + name + "\".");
        return;

    }

    private boolean isNotStaff(ProxiedPlayer player) {
        return GlobalAPI.getProfile(player.getUniqueId()) == null || !GlobalAPI.getProfile(player.getUniqueId()).hasPermission("ubungee.restricted");
    }

}