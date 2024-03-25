// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class KillAuraR extends PacketCheck
{
    private boolean sentUseEntity;
    
    public KillAuraR(final PlayerData playerData) {
        super(playerData, "Kill Aura (R)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace) {
            if (((PacketPlayInBlockPlace)packet).getFace() != 255 && this.sentUseEntity && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 2) {
                    this.ban(player);
                }
            }
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            this.sentUseEntity = true;
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sentUseEntity = false;
        }
    }
}
