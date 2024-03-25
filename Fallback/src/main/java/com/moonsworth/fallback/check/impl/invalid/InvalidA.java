// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.invalid;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class InvalidA extends PositionCheck
{
    private int verbose;
    
    public InvalidA(final PlayerData playerData) {
        super(playerData, "Invalid (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate type) {
        final double Y = Math.abs(type.getTo().getY() - type.getFrom().getY());
        if (Y > 0.39 && this.playerData.getAirTicks() == 0 && this.playerData.getGroundTicks() > 5 && !this.playerData.isOnStairs()) {
            if (this.verbose++ > 3) {
                final AlertData[] data = { new AlertData("Y", Y), new AlertData("Client", this.playerData.getClient().getName()) };
                this.alert(player, AlertType.RELEASE, data, true);
            }
        }
        else {
            this.verbose = Math.max(0, this.verbose - 1);
        }
    }
}
