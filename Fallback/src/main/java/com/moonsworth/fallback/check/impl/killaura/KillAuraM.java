// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

public class KillAuraM extends PacketCheck
{
    private int swings;
    private int attacks;
    
    public KillAuraM(final PlayerData playerData) {
        super(playerData, "Kill Aura (M)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (!this.playerData.isDigging() && !this.playerData.isPlacing()) {
            if (packet instanceof PacketPlayInFlying) {
                if (this.attacks > 0 && this.swings > this.attacks) {
                    final AlertData[] alertData = { new AlertData("S", this.swings), new AlertData("A", this.attacks), new AlertData("Client", this.playerData.getClient().getName()) };
                    this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                }
                final KillAuraN auraN = this.playerData.getCheck(KillAuraN.class);
                if (auraN != null) {
                    auraN.handleCheck(player, new int[] { this.swings, this.attacks });
                }
                this.swings = 0;
                this.attacks = 0;
            }
            else if (packet instanceof PacketPlayInArmAnimation) {
                ++this.swings;
            }
            else if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                ++this.attacks;
            }
        }
    }
}
