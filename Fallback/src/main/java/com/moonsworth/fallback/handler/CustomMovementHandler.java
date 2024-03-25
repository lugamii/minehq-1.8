// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.handler;

import java.beans.ConstructorProperties;

import com.moonsworth.fallback.Fallback;
import com.moonsworth.fallback.check.ICheck;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.BlockPos;
import com.moonsworth.fallback.util.BlockUtil;
import com.moonsworth.fallback.util.update.PositionUpdate;
import com.moonsworth.fallback.util.update.RotationUpdate;
import dev.lugami.spigot.handler.MovementHandler;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CustomMovementHandler implements MovementHandler
{
    private Fallback plugin;
    
    public void handleUpdateLocation(final Player player, final Location to, final Location from, final PacketPlayInFlying packet) {
        if (player.getAllowFlight()) {
            return;
        }
        if (player.isInsideVehicle()) {
            return;
        }
        if (!player.getWorld().isChunkLoaded(to.getBlockX() >> 4, to.getBlockZ() >> 4)) {
            return;
        }
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }
        playerData.updateTimers();
        playerData.setWasOnGround(playerData.isOnGround());
        playerData.setWasInLiquid(playerData.isInLiquid());
        playerData.setWasUnderBlock(playerData.isUnderBlock());
        playerData.setWasInWeb(playerData.isInWeb());
        playerData.setOnGround(BlockUtil.isOnGround(to, 0) || BlockUtil.isOnGround(to, 1));
        Label_0319: {
            if (!playerData.isOnGround()) {
                for (final BlockPos position : playerData.getFakeBlocks()) {
                    final int x = position.getX();
                    final int z = position.getZ();
                    final int blockX = to.getBlock().getX();
                    final int blockZ = to.getBlock().getZ();
                    for (int xOffset = -1; xOffset <= 1; ++xOffset) {
                        for (int zOffset = -1; zOffset <= 1; ++zOffset) {
                            if (x == blockX + xOffset && z == blockZ + zOffset) {
                                final int y = position.getY();
                                final int pY = to.getBlock().getY();
                                if (pY - y <= 1 && pY > y) {
                                    playerData.setOnGround(true);
                                }
                                if (playerData.isOnGround()) {
                                    break Label_0319;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (playerData.isOnGround()) {
            playerData.setLastGroundY(to.getY());
            playerData.setAirTicks(0);
        }
        else {
            playerData.setAirTicks(playerData.getAirTicks() + 1);
        }
        playerData.setInLiquid(BlockUtil.isOnLiquid(to, 0) || BlockUtil.isOnLiquid(to, 1) || BlockUtil.isOnLiquid(to, -1));
        playerData.setInWeb(BlockUtil.isOnWeb(to, 0));
        playerData.setOnIce(BlockUtil.isOnIce(to, 1) || BlockUtil.isOnIce(to, 2));
        if (playerData.isOnIce()) {
            playerData.setMovementsSinceIce(0);
        }
        else {
            playerData.setMovementsSinceIce(playerData.getMovementsSinceIce() + 1);
        }
        playerData.setOnStairs(BlockUtil.isOnStairs(to, 0) || BlockUtil.isOnStairs(to, 1));
        playerData.setOnLadder(BlockUtil.isLadder(to.getBlock()));
        playerData.setOnCarpet(BlockUtil.isOnCarpet(to, 0) || BlockUtil.isOnCarpet(to, 1));
        playerData.setUnderBlock(BlockUtil.isOnGround(to, -2));
        if (playerData.isUnderBlock()) {
            playerData.setMovementsSinceUnderBlock(0);
        }
        else {
            playerData.setMovementsSinceUnderBlock(playerData.getMovementsSinceUnderBlock() + 1);
        }
        if (to.getY() != from.getY() && playerData.getVelocityV() > 0) {
            playerData.setVelocityV(playerData.getVelocityV() - 1);
        }
        if (Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ()) > 0.0 && playerData.getVelocityH() > 0) {
            playerData.setVelocityH(playerData.getVelocityH() - 1);
        }
        Class<? extends ICheck>[] checks;
        for (int length = (checks = PlayerData.CHECKS).length, i = 0; i < length; ++i) {
            final Class<? extends ICheck> checkClass = checks[i];
            if (!Fallback.instance.getDisabledChecks().contains(checkClass.getSimpleName().toUpperCase())) {
                final ICheck check = (ICheck)playerData.getCheck(checkClass);
                if (check != null && check.getType() == PositionUpdate.class) {
                    check.handleCheck(player, new PositionUpdate(player, to, from, packet));
                }
            }
        }
        if (playerData.getVelocityY() > 0.0 && to.getY() > from.getY()) {
            playerData.setVelocityY(0.0);
        }
    }
    
    public void handleUpdateRotation(final Player player, final Location to, final Location from, final PacketPlayInFlying packet) {
        if (player.getAllowFlight()) {
            return;
        }
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }
        final double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        final double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));
        playerData.setMovementSpeed(Math.sqrt(x * x + z * z));
        for (final Class<? extends ICheck> checkClass : PlayerData.CHECKS) {
            if (!Fallback.instance.getDisabledChecks().contains(checkClass.getSimpleName().toUpperCase())) {
                final ICheck check = (ICheck)playerData.getCheck(checkClass);
                if (check != null && check.getType() == RotationUpdate.class) {
                    check.handleCheck(player, new RotationUpdate(player, to, from, packet));
                }
            }
        }
    }
    
    @ConstructorProperties({ "plugin" })
    public CustomMovementHandler(final Fallback plugin) {
        this.plugin = plugin;
    }
}
