// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check;

import java.beans.ConstructorProperties;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.event.PlayerAlertEvent;
import com.moonsworth.fallback.event.PlayerBanEvent;
import org.bukkit.event.Event;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.Fallback;
import com.moonsworth.fallback.player.PlayerData;

public abstract class AbstractCheck<T> implements ICheck<T>
{
    protected PlayerData playerData;
    private Class<T> clazz;
    private String name;
    
    @Override
    public Class<? extends T> getType() {
        return (Class<? extends T>)this.clazz;
    }
    
    protected Fallback getPlugin() {
        return Fallback.instance;
    }
    
    protected Player getPlayer() {
        return Fallback.instance.getServer().getPlayer(this.playerData.getUuid());
    }
    
    protected double getVl() {
        return this.playerData.getCheckVl(this);
    }
    
    protected void setVl(final double vl) {
        this.playerData.setCheckVl(vl, this);
    }
    
    protected boolean alert(final Player player, final AlertType alertType, final AlertData[] data, final boolean violation) {
        final String check = this.name + ((alertType != AlertType.RELEASE) ? (" (" + Character.toUpperCase(alertType.name().toLowerCase().charAt(0)) + alertType.name().toLowerCase().substring(1) + ")") : "");
        final PlayerAlertEvent event = new PlayerAlertEvent(alertType, player, check, data);
        this.playerData.flaggedChecks.add(this);
        this.playerData.addViolation(this);
        this.getPlugin().getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (violation) {
                this.playerData.flaggedChecks.add(this);
                this.playerData.addViolation(this);
            }
            return true;
        }
        return true;
    }
    
    protected boolean ban(final Player player) {
        this.playerData.setBanning(true);
        final PlayerBanEvent event = new PlayerBanEvent(player, this.name);
        this.getPlugin().getServer().getPluginManager().callEvent((Event)event);
        return !event.isCancelled();
    }
    
    protected void randomBan(final Player player, final double rate) {
        this.playerData.setRandomBanRate(rate);
        this.playerData.setRandomBanReason(this.name);
        this.playerData.setRandomBan(true);
        this.getPlugin().getServer().getPluginManager().callEvent((Event)new PlayerAlertEvent(AlertType.RELEASE, player, this.name));
    }
    
    @ConstructorProperties({ "playerData", "clazz", "name" })
    public AbstractCheck(final PlayerData playerData, final Class<T> clazz, final String name) {
        this.playerData = playerData;
        this.clazz = clazz;
        this.name = name;
    }
    
    public PlayerData getPlayerData() {
        return this.playerData;
    }
    
    public Class<T> getClazz() {
        return this.clazz;
    }
    
    public String getName() {
        return this.name;
    }
}
