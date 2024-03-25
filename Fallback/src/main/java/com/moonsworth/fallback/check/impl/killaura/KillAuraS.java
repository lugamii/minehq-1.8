// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Player;

public class KillAuraS extends PacketCheck
{
    private boolean sentArmAnimation;
    private boolean sentAttack;
    private boolean sentBlockPlace;
    private boolean sentUseEntity;
    
    public KillAuraS(final PlayerData playerData) {
        super(playerData, "Kill Aura (S)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            this.sentArmAnimation = true;
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            if (((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.sentAttack = true;
            }
            else {
                this.sentUseEntity = true;
            }
        }
        else if (packet instanceof PacketPlayInBlockPlace && ((PacketPlayInBlockPlace)packet).getItemStack() != null && ((PacketPlayInBlockPlace)packet).getItemStack().getName().toLowerCase().contains("sword")) {
            this.sentBlockPlace = true;
        }
        else if (packet instanceof PacketPlayInFlying) {
            if (this.sentArmAnimation && !this.sentAttack && this.sentBlockPlace && this.sentUseEntity && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 2) {
                    this.ban(player);
                }
            }
            final boolean b = false;
            this.sentUseEntity = false;
            this.sentBlockPlace = false;
            this.sentAttack = false;
            this.sentArmAnimation = false;
        }
    }
}
