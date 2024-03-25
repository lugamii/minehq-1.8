// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.badpackets;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class BadPacketsD extends PacketCheck
{
    private boolean sent;
    
    public BadPacketsD(final PlayerData playerData) {
        super(playerData, "Invalid Packets (D)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction.EnumPlayerAction action = ((PacketPlayInEntityAction)packet).b();
            if (action == PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING || action == PacketPlayInEntityAction.EnumPlayerAction.STOP_SNEAKING) {
                if (this.sent) {
                    if (this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
                        final int violations = this.playerData.getViolations(this, 60000L);
                        if (!this.playerData.isBanning() && violations > 2) {
                            this.ban(player);
                        }
                    }
                }
                else {
                    this.sent = true;
                }
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
