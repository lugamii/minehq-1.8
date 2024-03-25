// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.aimassist;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.RotationUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.RotationCheck;

public class AimAssistE extends RotationCheck
{
    private float lastYawRate;
    private float lastPitchRate;
    
    public AimAssistE(final PlayerData playerData) {
        super(playerData, "Aim (E)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        if (System.currentTimeMillis() > this.playerData.getLastAttackTime() + 10000L) {
            return;
        }
        final float diffYaw = Math.abs(update.getFrom().getYaw() - update.getTo().getYaw());
        final float diffPitch = Math.abs(update.getFrom().getPitch() - update.getTo().getPitch());
        final float diffPitchRate = Math.abs(this.lastPitchRate - diffPitch);
        final float diffYawRate = Math.abs(this.lastYawRate - diffYaw);
        final float diffPitchRatePitch = Math.abs(diffPitchRate - diffPitch);
        final float diffYawRateYaw = Math.abs(diffYawRate - diffYaw);
        if (diffPitch > 0.001 && diffPitch < 0.0094 && diffPitchRate > 1.0f && diffYawRate > 1.0f && diffYaw > 3.0f && this.lastYawRate > 1.5 && (diffPitchRatePitch > 1.0f || diffYawRateYaw > 1.0f)) {
            final AlertData[] alertData = { new AlertData("DPR", diffPitchRate), new AlertData("DYR", diffYawRate), new AlertData("LPR", this.lastPitchRate), new AlertData("LYR", this.lastYawRate), new AlertData("DP", diffPitch), new AlertData("DY", diffYaw), new AlertData("DPRP", diffPitchRatePitch), new AlertData("DYRY", diffYawRateYaw), new AlertData("Client", this.playerData.getClient().getName()) };
            this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
        }
        this.lastPitchRate = diffPitch;
        this.lastYawRate = diffYaw;
    }
}
