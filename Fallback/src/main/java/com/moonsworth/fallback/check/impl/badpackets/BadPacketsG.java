// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.badpackets;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class BadPacketsG extends PacketCheck
{
    private PacketPlayInEntityAction.EnumPlayerAction lastAction;
    
    public BadPacketsG(final PlayerData playerData) {
        super(playerData, "Invalid Packets (G)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction.EnumPlayerAction playerAction = ((PacketPlayInEntityAction)packet).b();
            if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING || playerAction == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                if (this.lastAction == playerAction && this.playerData.getLastAttackPacket() + 10000L > System.currentTimeMillis() && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 2) {
                        this.ban(player);
                    }
                }
                this.lastAction = playerAction;
            }
        }
    }
}
