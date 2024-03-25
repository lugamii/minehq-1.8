// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.motion;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class MotionA extends PositionCheck
{
    private int illegalMovements;
    private int legalMovements;
    
    public MotionA(final PlayerData playerData) {
        super(playerData, "Motion (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        if (this.playerData.getVelocityH() == 0) {
            final double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
            if (player.hasMetadata("modmode")) {
                return;
            }
            int speed = 0;
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals((Object)PotionEffectType.SPEED)) {
                    speed = effect.getAmplifier() + 1;
                    break;
                }
            }
            double threshold;
            if (this.playerData.isOnGround()) {
                threshold = 0.34;
                if (this.playerData.isOnStairs()) {
                    threshold = 0.45;
                }
                else if (this.playerData.isOnIce() || this.playerData.getMovementsSinceIce() < 40) {
                    if (this.playerData.isUnderBlock()) {
                        threshold = 1.3;
                    }
                    else {
                        threshold = 0.8;
                    }
                }
                else if (this.playerData.isUnderBlock() || this.playerData.getMovementsSinceUnderBlock() < 40) {
                    threshold = 0.7;
                }
                else if (this.playerData.isOnCarpet()) {
                    threshold = 0.7;
                }
                threshold += 0.06 * speed;
            }
            else {
                threshold = 0.36;
                if (this.playerData.isOnStairs()) {
                    threshold = 0.45;
                }
                else if (this.playerData.isOnIce() || this.playerData.getMovementsSinceIce() < 40) {
                    if (this.playerData.isUnderBlock()) {
                        threshold = 1.3;
                    }
                    else {
                        threshold = 0.8;
                    }
                }
                else if (this.playerData.isUnderBlock() || this.playerData.getMovementsSinceUnderBlock() < 40) {
                    threshold = 0.7;
                }
                else if (this.playerData.isOnCarpet()) {
                    threshold = 0.7;
                }
                threshold += 0.02 * speed;
            }
            threshold += ((player.getWalkSpeed() > 0.2f) ? (player.getWalkSpeed() * 10.0f * 0.33f) : 0.0f);
            if (offsetH > threshold) {
                ++this.illegalMovements;
            }
            else {
                ++this.legalMovements;
            }
            final int total = this.illegalMovements + this.legalMovements;
            if (total == 20) {
                final double percentage = this.illegalMovements / 20.0 * 100.0;
                final AlertData[] alertData = { new AlertData("P", percentage + "%"), new AlertData("Client", this.playerData.getClient().getName()) };
                if (percentage >= 45.0 && this.alert(player, AlertType.RELEASE, alertData, true)) {
                    final int violations = this.playerData.getViolations(this, 30000L);
                    if (!this.playerData.isBanning() && violations > 5) {
                        this.ban(player);
                    }
                }
                final boolean b = false;
                this.legalMovements = (b ? 1 : 0);
                this.illegalMovements = (b ? 1 : 0);
            }
        }
    }
}
