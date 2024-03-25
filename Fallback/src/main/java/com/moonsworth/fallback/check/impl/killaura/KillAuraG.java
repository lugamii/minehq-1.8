// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class KillAuraG extends PacketCheck
{
    private int stage;
    
    public KillAuraG(final PlayerData playerData) {
        super(playerData, "Kill Aura (G)");
        this.stage = 0;
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        final int calculusStage = this.stage % 6;
        if (calculusStage == 0) {
            if (packet instanceof PacketPlayInArmAnimation) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 1) {
            if (packet instanceof PacketPlayInUseEntity) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 2) {
            if (packet instanceof PacketPlayInEntityAction) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 3) {
            if (packet instanceof PacketPlayInFlying) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 4) {
            if (packet instanceof PacketPlayInEntityAction) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 5) {
            if (packet instanceof PacketPlayInFlying) {
                final AlertData[] alertData = { new AlertData("S", this.stage) };
                if (++this.stage >= 30 && this.alert(player, AlertType.RELEASE, alertData, true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 5) {
                        this.ban(player);
                    }
                }
            }
            else {
                this.stage = 0;
            }
        }
    }
}
