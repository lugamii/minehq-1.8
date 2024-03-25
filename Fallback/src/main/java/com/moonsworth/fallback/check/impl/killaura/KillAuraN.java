// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.AbstractCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import org.bukkit.entity.Player;

public class KillAuraN extends AbstractCheck<int[]>
{
    private int doubleSwings;
    private int doubleAttacks;
    private int bareSwings;
    
    public KillAuraN(final PlayerData playerData) {
        super(playerData, int[].class, "Kill Aura (N)");
    }
    
    @Override
    public void handleCheck(final Player player, final int[] ints) {
        final int swings = ints[0];
        final int attacks = ints[1];
        if (swings > 1 && attacks == 0) {
            ++this.doubleSwings;
        }
        else if (swings == 1 && attacks == 0) {
            ++this.bareSwings;
        }
        else if (attacks > 1) {
            ++this.doubleAttacks;
        }
        if (this.doubleSwings + this.doubleAttacks == 20) {
            double vl = this.getVl();
            if (this.doubleSwings == 0) {
                if (this.bareSwings > 10 && ++vl > 3.0) {
                    final AlertData[] alertData = { new AlertData("BS", this.bareSwings), new AlertData("VL", vl) };
                    this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                }
            }
            else {
                --vl;
            }
            this.setVl(vl);
            this.doubleSwings = 0;
            this.doubleAttacks = 0;
            this.bareSwings = 0;
        }
    }
}
