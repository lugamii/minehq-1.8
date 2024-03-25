// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.velocity;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class VelocityB extends PositionCheck
{
    public VelocityB(final PlayerData playerData) {
        super(playerData, "Velocity (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        final double offsetY = update.getTo().getY() - update.getFrom().getY();
        if (this.playerData.getVelocityY() > 0.0 && this.playerData.isWasOnGround() && !this.playerData.isUnderBlock() && !this.playerData.isWasUnderBlock() && !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() && !this.playerData.isOnStairs() && offsetY > 0.0 && offsetY < 0.41999998688697815 && update.getFrom().getY() % 1.0 == 0.0) {
            final double ratioY = offsetY / this.playerData.getVelocityY();
            int vl = (int)this.getVl();
            if (ratioY < 0.99) {
                final int percent = (int)Math.round(ratioY * 100.0);
                final AlertData[] alertData = { new AlertData("P", percent + "%"), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                if (++vl >= 5 && this.alert(player, AlertType.RELEASE, alertData, false) && !this.playerData.isBanning() && vl >= 15) {
                    this.ban(player);
                }
            }
            else {
                --vl;
            }
            this.setVl(vl);
        }
    }
}
