// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.aimassist;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.MathUtil;
import com.moonsworth.fallback.util.update.RotationUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.RotationCheck;

public class AimAssistA extends RotationCheck
{
    private float suspiciousYaw;
    
    public AimAssistA(final PlayerData playerData) {
        super(playerData, "Aim (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
            return;
        }
        final float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        if (diffYaw > 1.0f && Math.round(diffYaw) == diffYaw && diffYaw % 1.5f != 0.0f) {
            final AlertData[] data = { new AlertData("Y", diffYaw) };
            new AlertData("Client", this.playerData.getClient().getName());
            if (diffYaw == this.suspiciousYaw && this.alert(player, AlertType.RELEASE, data, true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 20) {
                    this.ban(player);
                }
            }
            this.suspiciousYaw = (float)Math.round(diffYaw);
        }
        else {
            this.suspiciousYaw = 0.0f;
        }
    }
}
