package dev.lugami.ubungee.whitelist;

import dev.lugami.ubungee.uBungee;
import net.md_5.bungee.api.ChatColor;

import java.util.UUID;

public interface WhitelistType {

    boolean isAllowed(UUID uuid);

    String getKickMessage();

    String name();

    static String getBaseMessage(){
        return ChatColor.RED + "The network is currently whitelisted.\n" +
                "Additional info may be found at " + uBungee.getInstance().getConfig().getString("website");
    }

    static WhitelistType getType(String s){
        return (s.equalsIgnoreCase("rank") ? new WhitelistRank() : (s.equalsIgnoreCase("uuid") ? new WhitelistUUID() : null));
    }

}
