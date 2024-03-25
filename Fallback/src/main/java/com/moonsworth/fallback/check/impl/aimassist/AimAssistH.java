// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.aimassist;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.Verbose;
import com.moonsworth.fallback.util.update.RotationUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.RotationCheck;

public class AimAssistH extends RotationCheck
{
    private final Verbose verbose;
    
    public AimAssistH(final PlayerData playerData) {
        super(playerData, "Aim (H)");
        this.verbose = new Verbose();
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        final double pitch = Math.abs(update.getTo().getPitch() - update.getFrom().getPitch());
        final double yaw = Math.abs(update.getTo().getYaw() - update.getFrom().getYaw());
        if (pitch > 0.0 || yaw > 0.0) {
            this.playerData.hasLooked = true;
            final double offset = pitch % 1.0;
            final double value = pitch % offset;
            final double offset2 = yaw % 1.0;
            final double value2 = yaw % offset2;
            if (value == 0.0 && pitch < 0.1 && pitch > 0.0 && value2 < 0.1 && yaw > 1.4) {
                final AlertData[] data = { new AlertData("P", pitch), new AlertData("Y", yaw), new AlertData("V", value), new AlertData("V2", value2), new AlertData("Client", this.playerData.getClient().getName()) };
                if (this.verbose.flag(2, 550L)) {
                    this.alert(player, AlertType.RELEASE, data, true);
                }
            }
        }
        else {
            this.playerData.hasLooked = false;
        }
    }
}
