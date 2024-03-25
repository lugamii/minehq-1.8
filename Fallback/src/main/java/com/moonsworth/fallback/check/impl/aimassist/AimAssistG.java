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

public class AimAssistG extends RotationCheck
{
    private double multiplier;
    private float previous;
    private double vl;
    private double streak;
    
    public AimAssistG(final PlayerData playerData) {
        super(playerData, "Aim (G)");
        this.multiplier = Math.pow(2.0, 24.0);
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() >= 2000L) {
            this.setVl(0.0);
            this.vl = 0.0;
            this.streak = 0.0;
            return;
        }
        if (this.playerData.getTeleportTicks() > 0 || this.playerData.getRespawnTicks() > 0 || this.playerData.getStandTicks() > 0) {
            this.vl = 0.0;
            return;
        }
        final float pitchChange = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());
        final long a = (long)(pitchChange * this.multiplier);
        final long b = (long)(this.previous * this.multiplier);
        final long gcd = this.gcd(a, b);
        final float magicVal = pitchChange * 100.0f / this.previous;
        if (magicVal > 60.0f) {
            this.vl = Math.max(0.0, this.vl - 1.0);
            this.streak = Math.max(0.0, this.streak - 0.125);
        }
        if (pitchChange > 0.5 && pitchChange <= 20.0f && gcd < 131072L) {
            final double vl = this.vl + 1.0;
            this.vl = vl;
            if (vl > 1.0) {
                ++this.streak;
            }
            if (this.streak > 6.0) {
                final AlertData[] alertData = { new AlertData("GCD ", gcd), new AlertData("PC ", pitchChange), new AlertData("Client", this.playerData.getClient().getName()) };
                this.alert(player, AlertType.RELEASE, alertData, true);
            }
        }
        else {
            this.vl = Math.max(0.0, this.vl - 1.0);
        }
        this.previous = pitchChange;
    }
    
    private long gcd(final long a, final long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }
}
