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

public class AimAssistJ extends RotationCheck
{
    private int verbose;
    private double lastDeltaPitch;
    
    public AimAssistJ(final PlayerData playerData) {
        super(playerData, "Aim (J)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        final double pitch = Math.abs(update.getTo().getPitch() - update.getFrom().getPitch());
        final double deltaPitch = this.lastDeltaPitch;
        this.lastDeltaPitch = pitch;
        final double pitchAcceleration = Math.abs(pitch - deltaPitch);
        final double offset = Math.pow(2.0, 24.0);
        final double gcd = (double)this.gcd((long)(pitch * offset), (long)(deltaPitch * offset));
        final double simple = gcd / offset;
        final double magic = pitch % simple;
        if (pitch > 0.0 && magic > 1.0E-4 && pitchAcceleration > 2.0 && simple < 0.006 && simple > 0.0) {
            if (this.verbose++ > 3) {
                final AlertData[] data = { new AlertData("GCD", simple), new AlertData("PA", pitchAcceleration), new AlertData("M", magic) };
                this.alert(player, AlertType.EXPERIMENTAL, data, true);
            }
        }
        else {
            this.verbose = 0;
        }
    }
    
    private long gcd(final long a, final long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }
}
