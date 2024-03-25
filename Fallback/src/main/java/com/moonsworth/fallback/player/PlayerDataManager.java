// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.player;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import com.moonsworth.fallback.Fallback;

public class PlayerDataManager
{
    private Fallback plugin;
    private Map<UUID, PlayerData> playerDataMap;
    
    public PlayerDataManager() {
        this.playerDataMap = new HashMap<UUID, PlayerData>();
    }
    
    public Fallback getPlugin() {
        return this.plugin;
    }
    
    public void addPlayerData(final Player player) {
        this.playerDataMap.put(player.getUniqueId(), new PlayerData(player.getUniqueId()));
    }
    
    public void removePlayerData(final Player player) {
        this.playerDataMap.remove(player.getUniqueId());
    }
    
    public boolean hasPlayerData(final Player player) {
        return this.playerDataMap.containsKey(player.getUniqueId());
    }
    
    public PlayerData getPlayerData(final Player player) {
        return this.playerDataMap.get(player.getUniqueId());
    }
    
    public PlayerData getPlayerData(final UUID playerUUID) {
        return this.playerDataMap.get(playerUUID);
    }
}
