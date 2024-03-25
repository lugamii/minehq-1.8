package dev.lugami.uhub.util;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static int getPing(Player player) {
        return ((CraftPlayer)player).getHandle().ping;
    }
}
