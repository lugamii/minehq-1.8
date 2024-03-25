// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.fly;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PositionCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;

public class FlyA extends PositionCheck
{
    public FlyA(final PlayerData playerData) {
        super(playerData, "Flight (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        int vl = (int)this.getVl();
        if (!this.playerData.isInLiquid() && !this.playerData.isOnGround() && this.playerData.getVelocityV() == 0) {
            if (update.getFrom().getY() >= update.getTo().getY()) {
                return;
            }
            final double distance = update.getTo().getY() - this.playerData.getLastGroundY();
            double limit = 2.0;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                for (final PotionEffect effect : player.getActivePotionEffects()) {
                    if (effect.getType().equals((Object)PotionEffectType.JUMP)) {
                        final int level = effect.getAmplifier() + 1;
                        limit += Math.pow(level + 4.2, 2.0) / 16.0;
                        break;
                    }
                }
            }
            if (distance > limit) {
                final AlertData[] alertData = { new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                if (++vl >= 10 && this.alert(player, AlertType.RELEASE, alertData, true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 8) {
                        this.ban(player);
                    }
                }
            }
            else {
                vl = 0;
            }
        }
        else {
            vl = 0;
        }
        this.setVl(vl);
    }
}
