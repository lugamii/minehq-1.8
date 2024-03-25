// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.fly;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PositionCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class FlyD extends PositionCheck
{
    private boolean fallen;
    private int verbose;
    
    public FlyD(final PlayerData playerData) {
        super(playerData, "Flight (D)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate type) {
        final double motionY = type.getTo().getY() - type.getFrom().getY();
        if (System.currentTimeMillis() - this.playerData.getLastTeleportTime() <= 500L || player.getVehicle() != null || (player.getMaximumNoDamageTicks() < 20 && player.getNoDamageTicks() >= 3) || player.isFlying() || player.getGameMode() != GameMode.SURVIVAL || player.getNoDamageTicks() >= 3) {
            this.verbose = 0;
            return;
        }
        if (this.playerData.isOnGround() || System.currentTimeMillis() - this.playerData.getLastVelocity() < 2000L || player.getAllowFlight() || this.playerData.isInLiquid() || !this.playerData.isWasUnderBlock()) {
            this.verbose = 0;
            this.fallen = false;
        }
        else {
            if (motionY < 0.0) {
                this.fallen = true;
            }
            if (this.fallen && motionY > -0.06 && this.verbose++ > 6) {
                final AlertData[] alertData = { new AlertData("Fallen ", this.fallen), new AlertData("MotionY ", motionY), new AlertData("Client", this.playerData.getClient().getName()) };
                this.alert(player, AlertType.RELEASE, alertData, true);
            }
        }
    }
}
