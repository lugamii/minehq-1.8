// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class AutoClickerP extends PacketCheck
{
    private int clicks;
    private int verbose;
    private ArrayList<Integer> av;
    private ArrayList<Double> av2;
    private ArrayList<Double> patterns;
    
    public AutoClickerP(final PlayerData playerData) {
        super(playerData, "AutoClicker (P)");
        this.av = new ArrayList<Integer>();
        this.av2 = new ArrayList<Double>();
        this.patterns = new ArrayList<Double>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet type) {
        if (type instanceof PacketPlayInArmAnimation) {
            ++this.clicks;
        }
        else if (type instanceof PacketPlayInBlockDig) {
            this.verbose = 0;
            this.clicks = Math.max(0, this.clicks - 1);
        }
        else if (type instanceof PacketPlayInFlying && this.playerData.currentTick % 20 == 0) {
            this.av.add(this.clicks);
            if (this.av.size() > 7) {
                this.av.remove(0);
            }
            if (this.av.size() >= 7) {
                int added = 0;
                for (int i = 0; i < this.av.size() - 1; ++i) {
                    added += this.av.get(i);
                }
                final double value = added / this.av.size();
                for (int j = 0; j < this.av.size() - 1; ++j) {
                    this.av2.add((this.av.get(j) - value) * (this.av.get(j) - value));
                }
                double value2 = 0.0;
                for (int k = 0; k < this.av2.size() - 1; ++k) {
                    value2 += this.av2.get(k);
                }
                final double value3 = 0.16666667;
                final double next = Math.sqrt(value2 * value3);
                if (this.patterns.contains(next) && this.clicks > 5 && next > 1.05) {
                    if (this.verbose++ > 3) {
                        final AlertData[] alertData = { new AlertData("V ", next), new AlertData("Clicks ", this.clicks), new AlertData("Verbose ", this.verbose), new AlertData("Client", this.playerData.getClient().getName()) };
                        this.alert(player, AlertType.RELEASE, alertData, true);
                    }
                }
                else {
                    this.verbose = Math.max(0, this.verbose - 1);
                }
                this.av2.clear();
                this.patterns.add(next);
                if (this.patterns.size() > 5) {
                    this.patterns.remove(0);
                }
            }
            this.clicks = 0;
        }
    }
}
