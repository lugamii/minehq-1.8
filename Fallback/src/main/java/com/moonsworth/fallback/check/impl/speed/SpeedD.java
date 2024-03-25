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

public class SpeedD extends PositionCheck
{
    private int verbose;
    
    public SpeedD(final PlayerData playerData) {
        super(playerData, "Speed (D)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        final double motionX = Math.abs(update.getTo().getX() - update.getFrom().getX());
        final double motionZ = Math.abs(update.getTo().getZ() - update.getFrom().getZ());
        final double speed = Math.sqrt(Math.pow(motionX, 2.0) + Math.pow(motionZ, 2.0));
        if (player.getAllowFlight() || this.playerData.getDeathTicks() > 0 || System.currentTimeMillis() - this.playerData.getVelocityH() <= 2L || System.currentTimeMillis() - this.playerData.getVelocityV() <= 2L || this.playerData.getIceTimer().hasNotPassed(20) || this.playerData.getBlockAboveTimer().hasNotPassed(15)) {
            this.verbose = 0;
            return;
        }
        if (player.getVehicle() != null || (player.getMaximumNoDamageTicks() < 20 && player.getNoDamageTicks() >= 3) || player.isFlying() || player.getGameMode() != GameMode.SURVIVAL || player.getNoDamageTicks() >= 3 || player.hasMetadata("modmode") || player.hasMetadata("noflag")) {
            return;
        }
        final double max = this.getBaseSpeed(player);
        if (speed >= max || motionX >= max || motionZ >= max) {
            if (this.verbose++ > 5) {
                final AlertData[] alertData = { new AlertData("SPEED ", speed), new AlertData("MotionX ", motionX), new AlertData("MotionZ ", motionZ), new AlertData("Client", this.playerData.getClient().getName()) };
                this.alert(player, AlertType.RELEASE, alertData, true);
            }
        }
        else {
            this.verbose = 0;
        }
    }
    
    private float getBaseSpeed(final Player player) {
        return 0.34f + this.getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
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
