package dev.lugami.ubungee.listener;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;

public class CBListener implements Listener {

    @Getter private static List<ProxiedPlayer> cbPlayers;
    @Getter private static List<ProxiedPlayer> lcPlayers;

    public CBListener(){
        cbPlayers = new ArrayList<>();
        lcPlayers = new ArrayList<>();
    }


    @EventHandler
    public void onRegister(PluginMessageEvent e) {
            if (e.getSender() instanceof ProxiedPlayer && e.getTag() != null) {
                String channel = e.getTag();
                ProxiedPlayer player = (ProxiedPlayer) e.getSender();
                boolean cheatbreaker = false;
                boolean lcclient = false;

                if (channel.contains("Lunar-Client")) {
                    lcclient = true;
                    lcPlayers.add(player);
                } else if (channel.contains("CB|IN") || channel.contains("CB-C") || channel.contains("CHEATBREAKER|IN") && !lcPlayers.contains(player)) {
                    cheatbreaker = true;
                    cbPlayers.add(player);
                }
            }
    }
}