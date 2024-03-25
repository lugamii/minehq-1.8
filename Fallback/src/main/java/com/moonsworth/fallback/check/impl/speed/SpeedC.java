// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.speed;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.GameMode;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class SpeedC extends PositionCheck
{
    private boolean lastGround;
    private double speed;
    
    public SpeedC(final PlayerData playerData) {
        super(playerData, "Speed (C)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        if (player.getAllowFlight() || !player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        final double x = Math.abs(Math.abs(update.getTo().getX()) - Math.abs(update.getFrom().getX()));
        final double z = Math.abs(Math.abs(update.getTo().getZ()) - Math.abs(update.getFrom().getZ()));
        this.speed = Math.sqrt(x * x + z * z);
        double max = 0.6399999856948853;
        for (final PotionEffect effect : this.getPlayer().getActivePotionEffects()) {
            if (effect.getType().equals((Object)PotionEffectType.SPEED)) {
                max += effect.getAmplifier() + 1;
            }
        }
        final boolean ground = this.playerData.isOnGround();
        if (ground && !this.lastGround && this.speed > max) {
            if (player.hasMetadata("modmode")) {
                return;
            }
            if (player.hasMetadata("noflag")) {
                return;
            }
            final AlertData[] alertData = { new AlertData("SPEED ", this.speed), new AlertData("Client", this.playerData.getClient().getName()) };
            this.alert(player, AlertType.RELEASE, alertData, false);
        }
        this.lastGround = ground;
    }
}
