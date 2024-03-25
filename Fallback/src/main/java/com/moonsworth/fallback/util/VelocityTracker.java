// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import java.util.LinkedList;
import java.util.Deque;

public class VelocityTracker
{
    private final Deque<Velocity> velocities;
    
    public VelocityTracker() {
        this.velocities = new LinkedList<Velocity>();
    }
    
    public void handleFlying(final PacketPlayInFlying packet) {
        this.velocities.removeIf(velocity -> {
            velocity.onMove();
            return velocity.hasExpired();
        });
    }
    
    public void handleVelocity(final PacketPlayOutEntityVelocity packet) {
    }
    
    public Deque<Velocity> getVelocities() {
        return this.velocities;
    }
    
    public class Velocity
    {
        private final double x;
        private final double y;
        private final double z;
        private final double horizontal;
        private int ticksExisted;
        
        public Velocity(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.horizontal = Math.hypot(x, z);
            this.ticksExisted = (int)(((x + z) / 2.0 + 2.0) * 15.0);
        }
        
        public void onMove() {
            --this.ticksExisted;
        }
        
        public boolean hasExpired() {
            return this.ticksExisted < 0;
        }
        
        public double getX() {
            return this.x;
        }
        
        public double getY() {
            return this.y;
        }
        
        public double getZ() {
            return this.z;
        }
        
        public double getHorizontal() {
            return this.horizontal;
        }
        
        public int getTicksExisted() {
            return this.ticksExisted;
        }
    }
}
