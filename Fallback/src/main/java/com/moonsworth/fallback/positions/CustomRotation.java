// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.positions;

public class CustomRotation
{
    private float yaw;
    private float pitch;
    
    public CustomRotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
}
