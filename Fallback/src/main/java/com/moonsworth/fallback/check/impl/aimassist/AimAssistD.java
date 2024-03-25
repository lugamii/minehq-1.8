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

public class AimAssistD extends RotationCheck
{
    private float lastYawRate;
    private float lastPitchRate;
    
    public AimAssistD(final PlayerData playerData) {
        super(playerData, "Aim (D)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
            return;
        }
        final float diffPitch = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());
        final float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        final float diffPitchRate = Math.abs(this.lastPitchRate - diffPitch);
        final float diffYawRate = Math.abs(this.lastYawRate - diffYaw);
        final float diffPitchRatePitch = Math.abs(diffPitchRate - diffPitch);
        final float diffYawRateYaw = Math.abs(diffYawRate - diffYaw);
        if (diffPitch < 0.009 && diffPitch > 0.001 && diffPitchRate > 1.0 && diffYawRate > 1.0 && diffYaw > 3.0 && this.lastYawRate > 1.5 && (diffPitchRatePitch > 1.0f || diffYawRateYaw > 1.0f)) {
            final AlertData[] alertData = { new AlertData("DPR", diffPitchRate), new AlertData("DYR", diffYawRate), new AlertData("LPR", this.lastPitchRate), new AlertData("LYR", this.lastYawRate), new AlertData("DP", diffPitch), new AlertData("DY", diffYaw), new AlertData("Client", this.playerData.getClient().getName()) };
            this.alert(player, AlertType.EXPERIMENTAL, alertData, true);
            if (!this.playerData.isBanning() && this.playerData.getViolations(this, 600000L) > 5) {
                this.ban(player);
            }
        }
        this.lastPitchRate = diffPitch;
        this.lastYawRate = diffYaw;
    }
}
