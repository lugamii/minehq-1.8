// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.CustomLocation;
import com.moonsworth.fallback.util.MathUtil;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class KillAuraC extends PacketCheck
{
    private float lastYaw;
    
    public KillAuraC(final PlayerData playerData) {
        super(playerData, "Kill Aura (C)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (this.playerData.getLastTarget() == null) {
            return;
        }
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying flying = (PacketPlayInFlying)packet;
            if (flying.h() && !this.playerData.isAllowTeleport()) {
                final CustomLocation targetLocation = this.playerData.getLastPlayerPacket(this.playerData.getLastTarget(), MathUtil.pingFormula(this.playerData.getPing()));
                if (targetLocation == null) {
                    return;
                }
                final CustomLocation playerLocation = this.playerData.getLastMovePacket();
                if (playerLocation.getX() == targetLocation.getX()) {
                    return;
                }
                if (targetLocation.getZ() == playerLocation.getZ()) {
                    return;
                }
                final float yaw = flying.d();
                if (yaw != this.lastYaw) {
                    final float bodyYaw = MathUtil.getDistanceBetweenAngles(yaw, MathUtil.getRotationFromPosition(playerLocation, targetLocation)[0]);
                    if (bodyYaw == 0.0f && this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
                        final int violations = this.playerData.getViolations(this, 60000L);
                        if (!this.playerData.isBanning() && violations > 5) {
                            this.ban(player);
                        }
                    }
                }
                this.lastYaw = yaw;
            }
        }
    }
}
