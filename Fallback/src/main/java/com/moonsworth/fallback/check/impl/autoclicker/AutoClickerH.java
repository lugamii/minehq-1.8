// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.LinkedList;

public class AutoClickerH extends PacketCheck
{
    private Deque<Integer> recentCounts;
    private int flyingCount;
    private boolean release;
    
    public AutoClickerH(final PlayerData playerData) {
        super(playerData, "Auto Clicker (H)");
        this.recentCounts = new LinkedList<Integer>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation && !this.playerData.isDigging() && !this.playerData.isPlacing() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && !this.playerData.isFakeDigging()) {
            if (this.flyingCount < 10) {
                if (this.release) {
                    this.release = false;
                    this.flyingCount = 0;
                    return;
                }
                this.recentCounts.add(this.flyingCount);
                if (this.recentCounts.size() == 100) {
                    double average = 0.0;
                    for (final int i : this.recentCounts) {
                        average += i;
                    }
                    average /= this.recentCounts.size();
                    double stdDev = 0.0;
                    for (final int j : this.recentCounts) {
                        stdDev += Math.pow(j - average, 2.0);
                    }
                    stdDev /= this.recentCounts.size();
                    stdDev = Math.sqrt(stdDev);
                    double vl = this.getVl();
                    if (stdDev < 0.45) {
                        if ((vl += 1.4) >= 4.0) {
                            final AlertData[] alertData = { new AlertData("STD", stdDev), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                            this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                        }
                    }
                    else {
                        vl -= 0.8;
                    }
                    this.setVl(vl);
                    this.recentCounts.clear();
                }
            }
            this.flyingCount = 0;
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.flyingCount;
        }
        else if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) {
            this.release = true;
        }
    }
}
