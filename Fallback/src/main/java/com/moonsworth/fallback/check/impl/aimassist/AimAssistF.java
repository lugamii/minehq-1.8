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

public class AimAssistF extends RotationCheck
{
    public AimAssistF(final PlayerData playerData) {
        super(playerData, "Aim (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        float fromYaw = (update.getFrom().getYaw() - 90.0f) % 360.0f;
        float toYaw = (update.getTo().getYaw() - 90.0f) % 360.0f;
        if (fromYaw < 0.0f) {
            fromYaw += 360.0f;
        }
        if (toYaw < 0.0f) {
            toYaw += 360.0f;
        }
        final double diffYaw = Math.abs(toYaw - fromYaw);
        int vl = (int)this.getVl();
        if (diffYaw > 0.0) {
            if (diffYaw % 1.0 == 0.0) {
                vl += 12;
                if (vl > 45) {
                    final AlertData[] alertData = { new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                    this.alert(player, AlertType.RELEASE, alertData, true);
                }
            }
            else {
                vl -= 8;
            }
        }
        else {
            vl -= 8;
        }
        this.setVl(vl);
    }
}
