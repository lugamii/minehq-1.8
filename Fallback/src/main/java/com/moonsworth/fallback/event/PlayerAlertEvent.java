// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.event;

import com.moonsworth.fallback.alert.AlertData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class PlayerAlertEvent extends Event implements Cancellable
{
    private static HandlerList HANDLER_LIST;
    private AlertType alertType;
    private Player player;
    private String checkName;
    private AlertData[] data;
    private boolean cancelled;
    
    public PlayerAlertEvent(final AlertType alertType, final Player player, final String checkName) {
        this(alertType, player, checkName, new AlertData[0]);
    }
    
    public PlayerAlertEvent(final AlertType alertType, final Player player, final String checkName, final AlertData[] data) {
        this.alertType = alertType;
        this.player = player;
        this.checkName = checkName;
        this.data = data;
    }
    
    public String concatData() {
        if (this.data.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.data.length; ++i) {
            final AlertData alertData = this.data[i];
            builder.append(alertData.getName());
            builder.append(" ");
            builder.append(alertData.getValue().toString());
            if (i != this.data.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
    
    public HandlerList getHandlers() {
        return PlayerAlertEvent.HANDLER_LIST;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerAlertEvent.HANDLER_LIST;
    }
    
    public AlertType getAlertType() {
        return this.alertType;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public String getCheckName() {
        return this.checkName;
    }
    
    public AlertData[] getData() {
        return this.data;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    static {
        PlayerAlertEvent.HANDLER_LIST = new HandlerList();
    }
}
