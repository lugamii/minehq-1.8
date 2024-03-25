// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.velocity;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class VelocityC extends PositionCheck
{
    public VelocityC(final PlayerData playerData) {
        super(playerData, "Velocity (C)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        final double offsetY = update.getTo().getY() - update.getFrom().getY();
        final double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
        final double velocityH = Math.hypot(this.playerData.getVelocityX(), this.playerData.getVelocityZ());
        final EntityPlayer entityPlayer = ((CraftPlayer)update.getPlayer()).getHandle();
        if (this.playerData.getVelocityY() > 0.0 && this.playerData.isWasOnGround() && !this.playerData.isUnderBlock() && !this.playerData.isWasUnderBlock() && !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() && update.getFrom().getY() % 1.0 == 0.0 && offsetY > 0.0 && offsetY < 0.41999998688697815 && velocityH > 0.45 && !entityPlayer.world.c(entityPlayer.getBoundingBox().grow(1.0, 0.0, 1.0))) {
            final double ratio = offsetH / velocityH;
            double vl = this.getVl();
            if (ratio < 0.62) {
                final AlertData[] alertData = { new AlertData("P", Math.round(ratio * 100.0) + "%"), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                if ((vl += 1.1) >= 8.0 && this.alert(player, AlertType.RELEASE, alertData, false) && !this.playerData.isBanning() && vl >= 20.0) {
                    this.ban(player);
                }
            }
            else {
                vl -= 0.4;
            }
            this.setVl(vl);
        }
    }
}
