// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class KillAuraK extends PacketCheck
{
    private int ticksSinceStage;
    private int streak;
    private int stage;
    
    public KillAuraK(final PlayerData playerData) {
        super(playerData, "Kill Aura (K)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.stage == 0) {
                this.stage = 1;
            }
            else {
                final boolean b = false;
                this.stage = 0;
                this.streak = 0;
            }
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            if (this.stage == 1) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (packet instanceof PacketPlayInFlying.PacketPlayInPositionLook) {
            if (this.stage == 2) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (packet instanceof PacketPlayInFlying.PacketPlayInPosition) {
            if (this.stage == 3) {
                if (++this.streak >= 15) {
                    final AlertData[] alertData = { new AlertData("STR", this.streak), new AlertData("Client", this.playerData.getClient().getName()) };
                    this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                }
                this.ticksSinceStage = 0;
            }
            this.stage = 0;
        }
        if (packet instanceof PacketPlayInFlying && ++this.ticksSinceStage > 40) {
            this.streak = 0;
        }
    }
}
