package dev.lugami.bridge.bukkit.parameters;

import dev.lugami.bridge.bukkit.BukkitAPI;
import dev.lugami.qlib.uuid.UUIDCache;

import java.util.UUID;

public class BridgeUUIDCache implements UUIDCache {

    @Override
    public UUID uuid(String var1) {
        return BukkitAPI.getProfile(var1).getUuid();
    }

    @Override
    public String name(UUID var1) {
        return BukkitAPI.getName(BukkitAPI.getProfile(var1), false);
    }

    @Override
    public void ensure(UUID var1) {

    }

    @Override
    public void update(UUID var1, String var2) {

    }
}
