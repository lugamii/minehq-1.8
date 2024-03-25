// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.badpackets;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class BadPacketsI extends PacketCheck
{
    private float lastYaw;
    private float lastPitch;
    private boolean ignore;
    
    public BadPacketsI(final PlayerData playerData) {
        super(playerData, "Invalid Packets (I)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying flying = (PacketPlayInFlying)packet;
            if (!flying.g() && flying.h()) {
                if (this.lastYaw == flying.d() && this.lastPitch == flying.e()) {
                    if (!this.ignore) {
                        this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], false);
                    }
                    this.ignore = false;
                }
                this.lastYaw = flying.d();
                this.lastPitch = flying.e();
            }
            else {
                this.ignore = true;
            }
        }
        else if (packet instanceof PacketPlayInSteerVehicle) {
            this.ignore = true;
        }
    }
}
