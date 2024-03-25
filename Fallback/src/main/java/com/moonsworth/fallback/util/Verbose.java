// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

public class Verbose
{
    public static String licensedTo;
    private int verbose;
    private long lastFlagTime;
    
    public boolean flag(final int amount) {
        this.lastFlagTime = System.currentTimeMillis();
        return this.verbose++ > amount;
    }
    
    public boolean flag(final int amount, final long reset) {
        if (System.currentTimeMillis() - this.lastFlagTime <= reset) {
            this.lastFlagTime = System.currentTimeMillis();
            return this.verbose++ > amount;
        }
        this.verbose = 0;
        this.lastFlagTime = System.currentTimeMillis();
        return false;
    }
    
    public boolean flag(final int amount, final int cap, final long reset) {
        if (System.currentTimeMillis() - this.lastFlagTime <= reset) {
            this.lastFlagTime = System.currentTimeMillis();
            if (this.verbose <= cap) {
                ++this.verbose;
            }
            return this.verbose > amount;
        }
        this.verbose = 0;
        this.lastFlagTime = System.currentTimeMillis();
        return false;
    }
    
    public int getVerbose() {
        return this.verbose;
    }
    
    public void setVerbose(final int verbose) {
        this.verbose = verbose;
    }
    
    public void takeaway() {
        this.verbose = ((this.verbose > 0) ? (this.verbose - 1) : 0);
    }
    
    public void takeaway(final int amount) {
        this.verbose = ((this.verbose > 0) ? (this.verbose - amount) : 0);
    }
    
    public boolean flag(final int amount, final long reset, final int toAdd) {
        if (System.currentTimeMillis() - this.lastFlagTime <= reset) {
            this.lastFlagTime = System.currentTimeMillis();
            final int verbose = this.verbose + toAdd;
            this.verbose = verbose;
            return verbose > amount;
        }
        this.verbose = 0;
        this.lastFlagTime = System.currentTimeMillis();
        return false;
    }
}
