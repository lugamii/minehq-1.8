// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.LinkedList;

public class AutoClickerI extends PacketCheck
{
    private Deque<Integer> recentCounts;
    private int flyingCount;
    
    public AutoClickerI(final PlayerData playerData) {
        super(playerData, "Auto Clicker (I)");
        this.recentCounts = new LinkedList<>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) {
            if (this.flyingCount < 10 && this.playerData.getLastAnimationPacket() + 2000L > System.currentTimeMillis()) {
                this.recentCounts.add(this.flyingCount);
                if (this.recentCounts.size() == 100) {
                    double average = 0.0;
                    for (final double flyingCount : this.recentCounts) {
                        average += flyingCount;
                    }
                    average /= this.recentCounts.size();
                    double stdDev = 0.0;
                    for (final long l : this.recentCounts) {
                        stdDev += Math.pow(l - average, 2.0);
                    }
                    stdDev /= this.recentCounts.size();
                    stdDev = Math.sqrt(stdDev);
                    double vl = this.getVl();
                    if (stdDev < 0.2) {
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
        }
        else if (packet instanceof PacketPlayInBlockPlace && ((PacketPlayInBlockPlace)packet).getItemStack() != null && ((PacketPlayInBlockPlace)packet).getItemStack().getName().toLowerCase().contains("sword")) {
            this.flyingCount = 0;
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.flyingCount;
        }
    }
}
