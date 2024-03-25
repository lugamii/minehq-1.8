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

public class FlyE extends PositionCheck
{
    private int verbose;
    
    public FlyE(final PlayerData playerData) {
        super(playerData, "Flight (E)");
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
        }
        else if (this.playerData.getAirTicks() > 6 && motionY >= 0.0 && this.verbose++ > 3) {
            final AlertData[] alertData = { new AlertData("MotionY ", motionY), new AlertData("Client", this.playerData.getClient().getName()) };
            this.alert(player, AlertType.EXPERIMENTAL, alertData, true);
        }
    }
}
