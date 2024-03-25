// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.velocity;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.MathUtil;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class VelocityA extends PositionCheck
{
    public VelocityA(final PlayerData playerData) {
        super(playerData, "Velocity (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        int vl = (int)this.getVl();
        if (this.playerData.getVelocityY() > 0.0 && !this.playerData.isUnderBlock() && !this.playerData.isWasUnderBlock() && !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L) {
            final int threshold = 10 + MathUtil.pingFormula(this.playerData.getPing()) * 2;
            if (++vl >= threshold) {
                final AlertData[] alertData = { new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                if (this.alert(player, AlertType.RELEASE, alertData, true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > Math.max(this.playerData.getPing() / 10L, 15L)) {
                        this.ban(player);
                    }
                }
                this.playerData.setVelocityY(0.0);
                vl = 0;
            }
        }
        else {
            vl = 0;
        }
        this.setVl(vl);
    }
}
