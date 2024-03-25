// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.badpackets;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class BadPacketsN extends PacketCheck
{
    private long lastFlying;
    private int vl;
    
    public BadPacketsN(final PlayerData playerData) {
        super(playerData, "Invalid Packets (N)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet type) {
        if (type instanceof PacketPlayInTransaction) {
            if (Math.abs(System.currentTimeMillis() - this.lastFlying) > 250L) {
                this.alert(player, AlertType.RELEASE, new AlertData[] { new AlertData("LF ", this.lastFlying) }, true);
            }
        }
        else if (type instanceof PacketPlayInFlying) {
            this.lastFlying = System.currentTimeMillis();
        }
    }
}
