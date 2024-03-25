// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util.update;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MovementUpdate
{
    private Player player;
    private Location to;
    private Location from;
    private PacketPlayInFlying packet;
    
    public MovementUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packet) {
        this.player = player;
        this.to = to;
        this.from = from;
        this.packet = packet;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Location getTo() {
        return this.to;
    }
    
    public Location getFrom() {
        return this.from;
    }
    
    public PacketPlayInFlying getPacket() {
        return this.packet;
    }
}
