package dev.lugami.bridge.bukkit.parameters;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import dev.lugami.qlib.xpacket.XPacket;


public class AnnouncePacket implements XPacket {

    private String message;

    public AnnouncePacket(String message) {
        this.message = message;
    }


    @Override
    public void onReceive() {
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Alert" + ChatColor.GRAY + "] " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', message));
    }
}
