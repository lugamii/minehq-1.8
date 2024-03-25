// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AutoClickerG extends PacketCheck
{
    private static int HIGH_CLICK_THRESHOLD;
    private static int LOW_CLICK_THRESHHOLD;
    private List<Long> clickData;
    private int lastCheck;
    private int lastVlDecrement;
    
    public AutoClickerG(final PlayerData playerData) {
        super(playerData, "Auto Clicker (G)");
        this.clickData = new ArrayList<Long>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        final long now = System.currentTimeMillis();
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            this.clickData.add(now);
        }
        else if (packet instanceof PacketPlayInFlying) {
            if (++this.lastVlDecrement == 100) {
                this.setVl(this.getVl() - 1.0);
                this.lastVlDecrement = 0;
            }
            if (++this.lastCheck == 20) {
                this.clickData.removeIf(time -> time == null || now - time > 1000L);
                double vl = this.getVl();
                final int size = this.clickData.size();
                if (size >= 100) {
                    vl += 10.0;
                    final AlertData[] alertData = new AlertData[0];
                    this.alert(player, AlertType.EXPERIMENTAL, alertData, true);
                    this.clickData.clear();
                    this.setVl(vl);
                    return;
                }
                if (size >= AutoClickerG.HIGH_CLICK_THRESHOLD) {
                    ++vl;
                }
                else if (size >= AutoClickerG.LOW_CLICK_THRESHHOLD) {
                    vl += 0.25;
                }
                if (vl > 15.0) {
                    final AlertData[] alertData = { new AlertData("S", size), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                    this.alert(player, AlertType.EXPERIMENTAL, alertData, true);
                }
                this.setVl(vl);
                this.lastCheck = 0;
            }
        }
    }
    
    static {
        AutoClickerG.HIGH_CLICK_THRESHOLD = 15;
        AutoClickerG.LOW_CLICK_THRESHHOLD = 10;
    }
}
