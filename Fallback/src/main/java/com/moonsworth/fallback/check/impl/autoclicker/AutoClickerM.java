// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import java.util.OptionalDouble;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;

import java.util.Collection;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.LinkedList;
import java.util.Queue;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class AutoClickerM extends PacketCheck
{
    private Queue<Integer> flyingPackets;
    private int currentCount;
    
    public AutoClickerM(final PlayerData playerData) {
        super(playerData, "Auto Clicker (M)");
        this.flyingPackets = new LinkedList<Integer>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            ++this.currentCount;
        }
        else if (packet instanceof PacketPlayInArmAnimation) {
            if (this.playerData.isDigging() || this.playerData.isPlacing()) {
                return;
            }
            if (this.currentCount >= 10) {
                this.currentCount = 0;
                return;
            }
            this.flyingPackets.add(this.currentCount);
            if (this.flyingPackets.size() >= 75) {
                this.handleFlyingPackets(player);
                this.flyingPackets.clear();
            }
            this.currentCount = 0;
        }
    }
    
    private void handleFlyingPackets(final Player player) {
        final double rangeDifference = this.getRangeDifference(this.flyingPackets);
        if (rangeDifference < 2.0) {
            final AlertData[] alertData = { new AlertData("rangeDiff=%s", rangeDifference) };
            this.alert(player, AlertType.RELEASE, alertData, false);
        }
        final double kurtosis = new Kurtosis().evaluate(this.flyingPackets.stream().mapToDouble(Number::doubleValue).toArray());
    }
    
    private double getRangeDifference(final Collection<? extends Number> numbers) {
        final OptionalDouble minOptional = numbers.stream().mapToDouble(Number::doubleValue).min();
        final OptionalDouble maxOptional = numbers.stream().mapToDouble(Number::doubleValue).max();
        if (!minOptional.isPresent() || !maxOptional.isPresent()) {
            return 500.0;
        }
        return maxOptional.getAsDouble() - minOptional.getAsDouble();
    }
}
