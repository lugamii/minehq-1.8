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

public class SpeedA extends PositionCheck
{
    private int verbose;
    private double average;
    
    public SpeedA(final PlayerData playerData) {
        super(playerData, "Speed (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        final double motionX = Math.abs(update.getTo().getX() - update.getFrom().getX());
        final double motionZ = Math.abs(update.getTo().getZ() - update.getFrom().getZ());
        final double speed = Math.sqrt(Math.pow(motionX, 2.0) + Math.pow(motionZ, 2.0));
        if (player.getAllowFlight() || this.playerData.getDeathTicks() > 0 || System.currentTimeMillis() - this.playerData.getLastVelocity() < 650L) {
            this.verbose = 0;
            return;
        }
        if (player.getVehicle() != null || (player.getMaximumNoDamageTicks() < 20 && player.getNoDamageTicks() >= 1) || this.playerData.getVelocityH() <= 0 || player.isFlying() || player.getGameMode() != GameMode.SURVIVAL || player.getNoDamageTicks() >= 1 || player.hasMetadata("modmode") || player.hasMetadata("noflag")) {
            return;
        }
        double max = 0.345;
        max += 0.4 * this.getPotionEffectLevel(player, PotionEffectType.SPEED);
        this.average = (this.average + 14.0) * speed / 15.0;
        if (this.average > max) {
            if (this.verbose++ > 4) {
                final AlertData[] alertData = { new AlertData("SPEED ", speed), new AlertData("MotionX ", motionX), new AlertData("MotionZ ", motionZ), new AlertData("Client", this.playerData.getClient().getName()) };
                this.alert(player, AlertType.RELEASE, alertData, true);
            }
        }
        else {
            this.verbose -= ((this.verbose > 0) ? 1 : 0);
        }
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
