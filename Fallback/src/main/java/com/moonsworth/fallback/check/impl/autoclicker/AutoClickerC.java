// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import dev.lugami.qlib.util.LinkedList;

import java.util.Deque;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class AutoClickerC extends PacketCheck
{
    private Deque<Integer> recentData;
    private int movements;
    
    public AutoClickerC(final PlayerData playerData) {
        super(playerData, "Auto Clicker (C)");
        this.recentData = (Deque<Integer>)new LinkedList();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.movements < 10 && !this.playerData.isDigging() && !this.playerData.isPlacing()) {
                this.recentData.add(this.movements);
                if (this.recentData.size() == 500) {
                    final int outliers = Math.toIntExact(this.recentData.stream().mapToInt(i -> i).filter(i -> i > 3).count());
                    double vl = this.getVl();
                    if (outliers < 5) {
                        if ((vl += 1.4) >= 4.0) {
                            final AlertData[] alertData = { new AlertData("O", outliers), new AlertData("VL", vl) };
                            new AlertData("Client", this.playerData.getClient().getName());
                            this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                        }
                    }
                    else {
                        vl -= 1.5;
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
