package dev.lugami.bridge.bukkit.parameters;

import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.qlib.nametag.NametagInfo;
import dev.lugami.qlib.nametag.NametagProvider;
import org.bukkit.entity.Player;

public class BridgeNameTagProvider extends NametagProvider {

    public BridgeNameTagProvider() {
        super("Bridge Provider", 1);
    }

    @Override
    public NametagInfo fetchNametag(Player player, Player viewer) {
        return createNametag(BukkitAPI.getColor(player), "");
    }
}
