// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

import java.util.Collection;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.LinkedList;
import java.util.Deque;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class AutoClickerQ extends PacketCheck
{
    private Deque<Double> delays;
    private double lastKurtosis;
    private int ticks;
    private double buffer;
    
    public AutoClickerQ(final PlayerData playerData) {
        super(playerData, "AutoClicker (Q)");
        this.delays = new LinkedList<Double>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.ticks < 10) {
                this.delays.add((double)this.ticks);
                if (this.delays.size() == 20) {
                    final double kurtosis = this.getKurtosis(this.delays);
                    if (Double.isNaN(kurtosis) || Math.abs(kurtosis - this.lastKurtosis) < 0.001) {
                        ++this.buffer;
                        if (this.buffer > 2.0) {
                            final AlertData[] alertData = { new AlertData("K ", kurtosis), new AlertData("Client", this.playerData.getClient().getName()) };
                            this.alert(player, AlertType.EXPERIMENTAL, alertData, true);
                        }
                    }
                    else {
                        this.buffer = 0.0;
                    }
                    this.lastKurtosis = kurtosis;
                    this.delays.clear();
                }
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.ticks;
        }
    }
    
    public double getKurtosis(final Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }
        if (count < 3.0) {
            return 0.0;
        }
        final double efficiencyFirst = count * (count + 1.0) / ((count - 1.0) * (count - 2.0) * (count - 3.0));
        final double efficiencySecond = 3.0 * Math.pow(count - 1.0, 2.0) / ((count - 2.0) * (count - 3.0));
        final double average = sum / count;
        double variance = 0.0;
        double varianceSquared = 0.0;
        for (final Number number2 : data) {
            variance += Math.pow(average - number2.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number2.doubleValue(), 4.0);
        }
        return efficiencyFirst * (varianceSquared / Math.pow(variance / sum, 2.0)) - efficiencySecond;
    }
}
