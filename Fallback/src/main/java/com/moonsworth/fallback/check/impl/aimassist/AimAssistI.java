// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.aimassist;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.RotationUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.RotationCheck;

public class AimAssistI extends RotationCheck
{
    public AimAssistI(final PlayerData playerData) {
        super(playerData, "Aim (I)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        final double yaw = Math.abs(update.getTo().getYaw() - update.getFrom().getYaw()) % 360.0f;
        final double offset = yaw % 3.141592653589793;
        final double value = yaw % offset;
        final double magic = yaw - (offset + value);
        if (yaw > 1400.0 && magic > 1000.0 && value < 3.0 && offset < 3.0 && System.currentTimeMillis() - this.playerData.getLastAttack() < 250L) {
            final AlertData[] data = { new AlertData("Y", yaw), new AlertData("V", value), new AlertData("M", magic), new AlertData("O", offset), new AlertData("Client", this.playerData.getClient().getName()) };
            this.alert(player, AlertType.DEVELOPMENT, data, false);
        }
    }
}
