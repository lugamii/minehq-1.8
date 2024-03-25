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

public class FlyC extends PositionCheck
{
    public FlyC(final PlayerData playerData) {
        super(playerData, "Flight (C)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate type) {
        final double motionY = Math.abs(type.getTo().getY() - type.getFrom().getY());
        int verbose = (int)this.getVl();
        if (motionY < 0.1 && !this.playerData.isInLiquid() && !player.getAllowFlight() && !this.playerData.isOnGround() && this.playerData.getVelocityV() == 0) {
            if (verbose++ > 5) {
                final AlertData[] alertData = { new AlertData("MotionY: ", motionY), new AlertData("Client", this.playerData.getClient().getName()) };
                this.alert(player, AlertType.RELEASE, alertData, true);
            }
        }
        else {
            verbose = Math.max(0, verbose - 1);
        }
        this.setVl(verbose);
    }
}
