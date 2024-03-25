// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.range;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.CustomLocation;
import com.moonsworth.fallback.util.MathUtil;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

public class RangeA extends PacketCheck
{
    private boolean sameTick;
    
    public RangeA(final PlayerData playerData) {
        super(playerData, "Range (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && !player.getGameMode().equals((Object)GameMode.CREATIVE) && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && !this.sameTick) {
            final PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity)packet;
            if (useEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                final Entity targetEntity = useEntity.a(((CraftPlayer)player).getHandle().getWorld());
                if (targetEntity instanceof EntityPlayer) {
                    final Player target = (Player)targetEntity.getBukkitEntity();
                    final CustomLocation targetLocation = this.playerData.getLastPlayerPacket(target.getUniqueId(), MathUtil.pingFormula(this.playerData.getPing()));
                    if (targetLocation == null) {
                        return;
                    }
                    final long diff = System.currentTimeMillis() - targetLocation.getTimestamp();
                    final long estimate = MathUtil.pingFormula(this.playerData.getPing()) * 50L;
                    final long diffEstimate = diff - estimate;
                    if (diffEstimate >= 500L) {
                        return;
                    }
                    final CustomLocation playerLocation = this.playerData.getLastMovePacket();
                    final PlayerData targetData = this.getPlugin().getPlayerDataManager().getPlayerData(target);
                    if (targetData == null) {
                        return;
                    }
                    final double range = Math.hypot(playerLocation.getX() - targetLocation.getX(), playerLocation.getZ() - targetLocation.getZ());
                    if (range > 6.5) {
                        return;
                    }
                    double threshold = 3.2;
                    if (!targetData.isSprinting() || MathUtil.getDistanceBetweenAngles(playerLocation.getYaw(), targetLocation.getYaw()) <= 90.0) {
                        threshold = 4.0;
                    }
                    double vl = this.getVl();
                    if (range > threshold) {
                        if (++vl >= 12.5) {
                            final boolean ex = this.getPlugin().getRangeVl() == 0.0;
                            final AlertData[] alertData = { new AlertData("P", range - threshold + 3.0), new AlertData("R", range), new AlertData("PI", this.playerData.getPing()), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                            if (!this.alert(player, ex ? AlertType.EXPERIMENTAL : AlertType.RELEASE, alertData, !ex)) {
                                vl = 0.0;
                            }
                        }
                    }
                    else if (range >= 2.0) {
                        vl -= 0.25;
                    }
                    this.setVl(vl);
                    this.sameTick = true;
                }
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sameTick = false;
        }
    }
}
