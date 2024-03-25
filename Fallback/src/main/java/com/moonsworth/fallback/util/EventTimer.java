// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

import com.moonsworth.fallback.player.PlayerData;

public class EventTimer
{
    private int tick;
    private int max;
    private PlayerData user;
    
    public EventTimer(final int max, final PlayerData user) {
        this.tick = 0;
        this.max = max;
        this.user = user;
    }
    
    public boolean hasNotPassed(final int ctick) {
        return this.user.currentTick > ctick && this.user.currentTick - this.tick < ctick;
    }
    
    public boolean passed(final int ctick) {
        return this.user.currentTick > ctick && this.user.currentTick - this.tick > ctick;
    }
    
    public boolean hasNotPassed() {
        return this.user.currentTick > this.max && this.user.currentTick - this.tick < this.max;
    }
    
    public boolean passed() {
        return this.user.currentTick > this.max && this.user.currentTick - this.tick > this.max;
    }
    
    public void reset() {
        this.tick = this.user.currentTick;
    }
    
    public int getTick() {
        return this.tick;
    }
    
    public int getMax() {
        return this.max;
    }
    
    public PlayerData getUser() {
        return this.user;
    }
}
