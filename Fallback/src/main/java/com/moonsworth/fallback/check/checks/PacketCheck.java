// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.checks;

import com.moonsworth.fallback.check.AbstractCheck;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;

public abstract class PacketCheck extends AbstractCheck<Packet>
{
    public static int START_SNEAKING;
    public static int STOP_SNEAKING;
    public static int START_SPRINTING;
    public static int STOP_SPRINTING;
    public static int START_DESTROY_BLOCK;
    public static int ABORT_DESTROY_BLOCK;
    public static int STOP_DESTROY_BLOCK;
    public static int DROP_ALL_ITEMS;
    public static int DROP_ITEM;
    public static int RELEASE_USE_ITEM;
    
    public PacketCheck(final PlayerData playerData, final String name) {
        super(playerData, Packet.class, name);
    }
    
    static {
        PacketCheck.START_SNEAKING = 1;
        PacketCheck.STOP_SNEAKING = 2;
        PacketCheck.START_SPRINTING = 4;
        PacketCheck.STOP_SPRINTING = 5;
        PacketCheck.START_DESTROY_BLOCK = 0;
        PacketCheck.ABORT_DESTROY_BLOCK = 1;
        PacketCheck.STOP_DESTROY_BLOCK = 2;
        PacketCheck.DROP_ALL_ITEMS = 3;
        PacketCheck.DROP_ITEM = 4;
        PacketCheck.RELEASE_USE_ITEM = 5;
    }
}
