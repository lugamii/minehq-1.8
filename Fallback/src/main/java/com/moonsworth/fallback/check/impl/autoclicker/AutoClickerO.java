// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.LinkedList;
import java.util.Deque;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class AutoClickerO extends PacketCheck
{
    private final Deque<Long> ticks;
    private double lastDeviation;
    private double buffer;
    
    public AutoClickerO(final PlayerData playerData) {
        super(playerData, "AutoClicker (O)");
        this.ticks = new LinkedList<Long>();
        this.buffer = 0.0;
    }
    
    @Override
    public void handleCheck(final Player player, final Packet type) {
        if (type instanceof PacketPlayInArmAnimation) {
            if (!this.playerData.isDigging()) {
                this.ticks.add((long)(this.playerData.getCurrentTick() * 50.0));
            }
            if (this.ticks.size() >= 10) {
                final double deviation = getStandardDeviation(this.ticks.stream().mapToDouble(d -> d).toArray());
                final double diff = Math.abs(deviation - this.lastDeviation);
                if (diff < 10.0) {
                    final double buffer = this.buffer + 1.0;
                    this.buffer = buffer;
                    if (buffer > 5.0) {
                        final AlertData[] alertData = { new AlertData("DD ", diff), new AlertData("Client", this.playerData.getClient().getName()) };
                        this.alert(player, AlertType.RELEASE, alertData, true);
                    }
                }
                else {
                    this.buffer *= 0.75;
                }
                this.lastDeviation = deviation;
                this.ticks.clear();
            }
        }
    }
    
    public static double getStandardDeviation(final double[] numberArray) {
        double sum = 0.0;
        double deviation = 0.0;
        final int length = numberArray.length;
        for (final double num : numberArray) {
            sum += num;
        }
        final double mean = sum / length;
        for (final double num2 : numberArray) {
            deviation += Math.pow(num2 - mean, 2.0);
        }
        return Math.sqrt(deviation / length);
    }
}
