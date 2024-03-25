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

public class KillAuraQ extends PacketCheck
{
    private boolean sentAttack;
    private boolean sentInteract;
    
    public KillAuraQ(final PlayerData playerData) {
        super(playerData, "Kill Aura (Q)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace) {
            if (this.sentAttack && !this.sentInteract && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 2) {
                    this.ban(player);
                }
            }
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity.EnumEntityUseAction action = ((PacketPlayInUseEntity)packet).a();
            if (action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.sentAttack = true;
            }
            else if (action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
                this.sentInteract = true;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            final boolean b = false;
            this.sentInteract = false;
            this.sentAttack = false;
        }
    }
}
