// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.motion;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.MathUtil;
import com.moonsworth.fallback.util.Verbose;
import org.bukkit.potion.PotionEffectType;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class MotionB extends PacketCheck
{
    private int hits;
    private int value;
    private Verbose verbose;
    
    public MotionB(final PlayerData playerData) {
        super(playerData, "Motion (B)");
        this.verbose = new Verbose();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet type) {
        if (type instanceof PacketPlayInFlying) {
            if (System.currentTimeMillis() - this.playerData.getLastAttack() < 150L) {
                final double speed = this.playerData.getMovementSpeed();
                if (speed > 0.0) {
                    if (this.hits > 0) {
                        ++this.value;
                    }
                    final double max = (this.value > 2 && this.playerData.isSprinting()) ? this.getBaseSpeed(player, 0.281f) : ((double)this.getBaseSpeed(player, 0.282f));
                    if (speed > max && this.hits > 0 && this.verbose.flag(4, 600L)) {
                        final AlertData[] alertData = { new AlertData("Speed", speed), new AlertData("Max", max), new AlertData("Hits", this.hits), new AlertData("Client", this.playerData.getClient().getName()) };
                        this.alert(player, AlertType.RELEASE, alertData, true);
                    }
                }
                else {
                    this.value = 0;
                }
            }
            else {
                this.value = 0;
            }
            this.hits = 0;
        }
        else if (type instanceof PacketPlayInUseEntity) {
            ++this.hits;
            this.playerData.setLastAttack(System.currentTimeMillis());
        }
    }
    
    private float getBaseSpeed(final Player player, final float normal) {
        return normal + MathUtil.getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.059f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
}
