// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.fly;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PositionCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;

public class FlyB extends PositionCheck
{
    public FlyB(final PlayerData playerData) {
        super(playerData, "Flight (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        int vl = (int)this.getVl();
        if (!this.playerData.isInLiquid() && !this.playerData.isOnGround()) {
            final double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
            final double offsetY = update.getTo().getY() - update.getFrom().getY();
            if (offsetH > 0.0 && offsetY == 0.0) {
                final AlertData[] alertData = { new AlertData("H", offsetH), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                if (++vl >= 10 && this.alert(player, AlertType.RELEASE, alertData, true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 15) {
                        this.ban(player);
                    }
                }
            }
            else {
                vl = 0;
            }
        }
        else {
            vl = 0;
        }
        this.setVl(vl);
    }
}
