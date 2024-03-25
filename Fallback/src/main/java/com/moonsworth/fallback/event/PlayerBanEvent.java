// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class PlayerBanEvent extends Event implements Cancellable
{
    private static HandlerList HANDLER_LIST;
    private Player player;
    private String reason;
    private boolean cancelled;
    
    public PlayerBanEvent(final Player player, final String reason) {
        this.player = player;
        this.reason = reason;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerBanEvent.HANDLER_LIST;
    }
    
    public HandlerList getHandlers() {
        return PlayerBanEvent.HANDLER_LIST;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    static {
        PlayerBanEvent.HANDLER_LIST = new HandlerList();
    }
}
