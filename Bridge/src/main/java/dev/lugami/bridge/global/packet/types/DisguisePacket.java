package dev.lugami.bridge.global.packet.types;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.bridge.global.packet.Packet;

public class DisguisePacket implements Packet {

    public String message;

    public DisguisePacket(String message) {
        this.message = message;
    }

    @Override
    public void onReceive() {
        Bukkit.getOnlinePlayers().stream().filter(player -> BukkitAPI.getProfile(player).getCurrentGrant().getRank().isStaff()).forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }
}