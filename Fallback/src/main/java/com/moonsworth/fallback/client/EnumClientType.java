// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.client;

import java.beans.ConstructorProperties;

public enum EnumClientType implements ClientType
{
    COSMIC_CLIENT(false, "CosmicClient"), 
    CHEAT_BREAKER(false, "CheatBreaker"), 
    Lunar_Client(false, "Lunar-Client"), 
    AERO_CLIENT(false, "Aero-Client"), 
    VANILLA(false, "Vanilla"), 
    FORGE(false, "Forge-Client"), 
    OCMC(false, "OCMC-Client", "OCMC");
    
    private boolean hacked;
    private String name;
    private String payloadTag;
    
    private EnumClientType(final boolean hacked, final String name) {
        this.hacked = hacked;
        this.name = name;
    }
    
    @ConstructorProperties({ "hacked", "name", "payloadTag" })
    private EnumClientType(final boolean hacked, final String name, final String payloadTag) {
        this.hacked = hacked;
        this.name = name;
        this.payloadTag = payloadTag;
    }
    
    @Override
    public boolean isHacked() {
        return this.hacked;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public String getPayloadTag() {
        return this.payloadTag;
    }
}
