// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.invalid;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import org.bukkit.GameMode;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class InvalidB extends PositionCheck
{
    private double verbose;
    private boolean fallen;
    
    public InvalidB(final PlayerData playerData) {
        super(playerData, "Invalid (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate type) {
        final double motionY = type.getTo().getY() - type.getFrom().getY();
        if (System.currentTimeMillis() - this.playerData.getLastTeleportTime() <= 500L || player.getVehicle() != null || (player.getMaximumNoDamageTicks() < 20 && player.getNoDamageTicks() >= 3) || player.isFlying() || player.getGameMode() != GameMode.SURVIVAL || player.getNoDamageTicks() >= 3) {
            this.verbose = 0.0;
            return;
        }
        if (this.playerData.isOnGround() || System.currentTimeMillis() - this.playerData.getLastVelocity() < 2000L || player.getAllowFlight() || this.playerData.isInLiquid() || !this.playerData.isWasUnderBlock()) {
            this.verbose = 0.0;
            this.fallen = false;
        }
        else {
            String tags = "";
            if (motionY < -0.05) {
                this.fallen = true;
            }
            if (motionY >= 0.0 && this.playerData.getAirTicks() > 5) {
                tags += "InvalidY ";
                this.verbose += 0.5;
            }
            if (this.fallen && motionY > -0.08 && motionY != 0.0) {
                tags += "Glide ";
                this.verbose += 0.25;
            }
            if (motionY < -0.6 && this.playerData.getAirTicks() <= 1) {
                tags += "FastFall";
                this.verbose += 0.5;
            }
            if (this.verbose > 1.0) {
                final AlertData[] data = { new AlertData("motionY", motionY), new AlertData("verbose", this.verbose), new AlertData("tags", tags), new AlertData("Client", this.playerData.getClient().getName()) };
                this.alert(player, AlertType.EXPERIMENTAL, data, false);
            }
        }
    }
}
