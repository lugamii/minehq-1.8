package dev.lugami.bridge.bukkit.parameters;

import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import dev.lugami.qlib.xpacket.XPacket;


public class AnnouncePackets implements XPacket {

    private String message = "";
    private String server;



    public AnnouncePackets(String message, String server) {
        this.message = message;
        this.server = server;
    }


    @Override
    public void onReceive() {
        FancyMessage fancyMessage = new FancyMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Alert" + ChatColor.GRAY + "] " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', message));
        if (this.server.contains("Bunkers")) {
        fancyMessage.tooltip(ChatColor.LIGHT_PURPLE + "Click to go to the Bunkers Lobby");
        } else if (this.server.contains("MineSG")) {
            fancyMessage.tooltip(ChatColor.LIGHT_PURPLE + "Click to go to the MineSG Lobby");
        } else if (this.server.contains("UHC-Meetup")) {
            fancyMessage.tooltip(ChatColor.LIGHT_PURPLE + "Click to go to the UHC Meetup Lobby");
        } else if (this.server.contains("Vault-Battles")) {
            fancyMessage.tooltip(ChatColor.LIGHT_PURPLE + "Click to go to the Vault Battles Lobby");
        }
        fancyMessage.command("/joinfastserver " + server);
        Bukkit.getOnlinePlayers().forEach(fancyMessage::send);

    }
}
