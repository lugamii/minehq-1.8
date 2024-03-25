// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.speed;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.GameMode;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class SpeedF extends PositionCheck
{
    private double verbose;
    private double lastSpeed;
    
    public SpeedF(final PlayerData playerData) {
        super(playerData, "Speed (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        final double motionX = Math.abs(update.getTo().getX() - update.getFrom().getX());
        final double motionZ = Math.abs(update.getTo().getZ() - update.getFrom().getZ());
        final double speed = Math.sqrt(Math.pow(motionX, 2.0) + Math.pow(motionZ, 2.0));
        if (player.getAllowFlight() || this.playerData.getDeathTicks() > 0 || System.currentTimeMillis() - this.playerData.getVelocityH() <= 0L || this.playerData.getIceTimer().hasNotPassed(20) || this.playerData.isOnStairs() || this.playerData.getBlockAboveTimer().hasNotPassed(15)) {
            this.verbose = 0.0;
            return;
        }
        if (player.getVehicle() != null || (player.getMaximumNoDamageTicks() < 20 && player.getNoDamageTicks() >= 1) || player.isFlying() || player.getGameMode() != GameMode.SURVIVAL || player.getNoDamageTicks() >= 1 || player.hasMetadata("modmode") || player.hasMetadata("noflag")) {
            return;
        }
        final double lastSpeed = this.lastSpeed;
        this.lastSpeed = speed;
        final double accel = Math.abs(speed - lastSpeed);
        if (speed > this.getBaseSpeed(player) && accel > 0.006) {
            final double verbose = this.verbose;
            this.verbose = verbose + 1.0;
            if (verbose > 3.0) {
                final AlertData[] alertData = { new AlertData("SPEED ", speed), new AlertData("ACCEL", accel), new AlertData("Verbose ", this.verbose), new AlertData("Client", this.playerData.getClient().getName()) };
                this.alert(player, AlertType.EXPERIMENTAL, alertData, true);
            }
        }
        else {
            this.verbose -= ((this.verbose > 0.0) ? 1.0 : 0.0);
        }
    }
    
    private float getBaseSpeed(final Player player) {
        final float magic = 0.34f;
        return magic + this.getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    private int getPotionEffectLevel(final Player player, final PotionEffectType pet) {
        for (final PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
}
