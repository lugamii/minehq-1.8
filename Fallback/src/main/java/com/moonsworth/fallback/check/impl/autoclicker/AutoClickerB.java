// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.MathUtil;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.LinkedList;
import java.util.Deque;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class AutoClickerB extends PacketCheck
{
    private Deque<Integer> recentData;
    private int movements;
    
    public AutoClickerB(final PlayerData playerData) {
        super(playerData, "Auto Clicker (B)");
        this.recentData = new LinkedList<Integer>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.movements < 10 && !this.playerData.isDigging() && !this.playerData.isPlacing()) {
                this.recentData.add(this.movements);
                if (this.recentData.size() == 50) {
                    final double stdDev = MathUtil.stDeviation(this.recentData);
                    int vl = (int)this.getVl();
                    if (stdDev < 0.48) {
                        if (++vl >= 5) {
                            final AlertData[] alertData = { new AlertData("STDEV", stdDev), new AlertData("CPS", 1000.0 / (MathUtil.average(this.recentData) * 50.0)), new AlertData("Client", this.playerData.getClient().getName()) };
                            this.alert(player, AlertType.RELEASE, alertData, true);
                            final int violations = this.playerData.getViolations(this, 60000L);
                            if (!this.playerData.isBanning() && violations > 15) {
                                this.ban(player);
                            }
                        }
                        else {
                            --vl;
                        }
                        this.setVl(vl);
                        this.recentData.clear();
                    }
                }
                this.movements = 0;
            }
            else if (packet instanceof PacketPlayInFlying) {
                ++this.movements;
            }
        }
    }
}
