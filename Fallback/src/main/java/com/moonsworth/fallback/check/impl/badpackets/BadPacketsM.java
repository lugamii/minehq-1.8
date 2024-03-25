// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.badpackets;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class BadPacketsM extends PositionCheck
{
    public BadPacketsM(final PlayerData playerData) {
        super(playerData, "Invalid Packets (M)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        double height = 0.9;
        final double difference = update.getTo().getY() - update.getFrom().getY();
        double vl = this.getVl();
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals((Object)PotionEffectType.JUMP)) {
                    final int level = effect.getAmplifier() + 1;
                    height += Math.pow(level + 4.2, 2.0) / 16.0;
                    break;
                }
            }
        }
        if (difference > height) {
            if (player.hasMetadata("noflag")) {
                return;
            }
            if (player.hasMetadata("modmode")) {
                return;
            }
            final AlertData[] alertData = { new AlertData("height", height), new AlertData("diff", difference) };
            if (++vl < 15.0 || this.alert(player, AlertType.EXPERIMENTAL, alertData, true)) {}
            this.playerData.setCheckVl(vl, this);
        }
    }
}
