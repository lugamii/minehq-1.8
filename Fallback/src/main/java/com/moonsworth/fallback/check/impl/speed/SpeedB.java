// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.speed;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import com.moonsworth.fallback.util.update.PositionUpdate;
import org.bukkit.entity.Player;

import java.util.UUID;
import com.moonsworth.fallback.check.checks.PositionCheck;

public class SpeedB extends PositionCheck
{
    private static UUID MOVE_SPEED;
    private double horizontalSpeed;
    private double blockFriction;
    private double previousHorizontalMove;
    private int blockFrictionX;
    private int blockFrictionY;
    private int blockFrictionZ;
    
    public SpeedB(final PlayerData playerData) {
        super(playerData, "Speed (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        this.horizontalSpeed = this.updateMoveSpeed();
        final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        final Location to = update.getTo();
        final Location from = update.getFrom();
        if (!entityPlayer.server.getAllowFlight() && !player.getAllowFlight()) {
            final double dx = to.getX() - from.getX();
            final double dy = to.getY() - from.getY();
            final double dz = to.getZ() - from.getZ();
            double horizontalSpeed = this.horizontalSpeed;
            double blockFriction = this.blockFriction;
            final boolean canSprint = true;
            final boolean onGround = entityPlayer.onGround;
            if (onGround) {
                if (canSprint) {
                    horizontalSpeed *= 1.3;
                }
                blockFriction *= 0.91;
                horizontalSpeed *= 0.23277136 / (blockFriction * blockFriction * blockFriction);
                if (dy > 1.0E-4) {
                    if (canSprint) {
                        horizontalSpeed += 0.2;
                    }
                    final MobEffect jumpBoost = entityPlayer.getEffect(MobEffectList.JUMP);
                    if (jumpBoost == null && !entityPlayer.world.c(entityPlayer.getBoundingBox().grow(0.5, 0.29, 0.5).a(0.0, 0.3, 0.0)) && dy < 0.3) {
                        horizontalSpeed = 0.01;
                    }
                }
                else if (dy == 0.0 && entityPlayer.world.c(entityPlayer.getBoundingBox().grow(0.5, 0.0, 0.5).a(0.0, 0.5, 0.0))) {}
            }
            else {
                horizontalSpeed = (canSprint ? 0.026 : 0.02);
                blockFriction = 0.91;
            }
            final double offsetH = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dz, 2.0));
            final double speedup = (offsetH - this.previousHorizontalMove) / horizontalSpeed;
            double verbose = this.getVl();
            if (player.hasMetadata("modmode")) {
                return;
            }
            if (player.hasMetadata("noflag")) {
                return;
            }
            if (this.playerData.getTeleportTicks() > 0) {
                return;
            }
            if (this.playerData.getVelocityH() <= 0 && System.currentTimeMillis() > this.playerData.getLastTeleportTime() + 3500L && speedup > 1.08) {
                if ((verbose += speedup) >= 30.0) {
                    final AlertData[] alertData = { new AlertData("SPEED ", speedup), new AlertData("HZ ", horizontalSpeed), new AlertData("BF ", blockFriction), new AlertData("Client", this.playerData.getClient().getName()) };
                    this.alert(player, AlertType.RELEASE, alertData, true);
                }
            }
            else {
                verbose -= 0.25;
            }
            this.setVl(verbose);
            this.previousHorizontalMove = offsetH * blockFriction;
            final int blockX = NumberConversions.floor(to.getX());
            final int blockY = NumberConversions.floor(to.getY());
            final int blockZ = NumberConversions.floor(to.getZ());
            if (blockX != this.blockFrictionX || blockY != this.blockFrictionY || blockZ != this.blockFrictionZ) {
                this.blockFriction = entityPlayer.world.getType(new BlockPosition(blockX, blockY - 1, blockZ)).getBlock().frictionFactor;
                this.blockFrictionX = blockX;
                this.blockFrictionY = blockY;
                this.blockFrictionZ = blockZ;
            }
        }
    }
    
    public double updateMoveSpeed() {
        final AttributeModifiable attribute = (AttributeModifiable)((CraftPlayer)this.getPlayer()).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        double value;
        final double base = value = attribute.b();
        for (Object modifier : attribute.a(0)) {
            value += ((AttributeModifier)modifier).d();
        }
        for (final Object modifier : attribute.a(1)) {
            value += ((AttributeModifier)modifier).d() * base;
        }
        for (final Object modifier : attribute.a(2)) {
            if (!((AttributeModifier)modifier).a().equals(SpeedB.MOVE_SPEED)) {
                value *= 1.0 + ((AttributeModifier)modifier).d();
            }
        }
        return value;
    }
    
    static {
        SpeedB.MOVE_SPEED = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    }
}
