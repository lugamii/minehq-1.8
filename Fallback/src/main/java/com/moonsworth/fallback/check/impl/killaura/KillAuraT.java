// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class KillAuraT extends PacketCheck
{
    private int lastTick;
    
    public KillAuraT(final PlayerData playerData) {
        super(playerData, "Kill Aura (T)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        double vl = this.getVl();
        if (packet instanceof PacketPlayInUseEntity) {
            if (this.playerData.currentTick == this.lastTick) {
                final AlertData[] data = { new AlertData("Tick: ", this.playerData.currentTick) };
                final double n = vl;
                vl = n + 1.0;
                if (n > 0.0) {
                    this.alert(player, AlertType.RELEASE, data, true);
                }
            }
            else {
                vl = Math.max(0.0, vl - 1.0);
            }
        }
        else if (packet instanceof PacketPlayInBlockDig) {
            this.lastTick = this.playerData.currentTick;
        }
        this.setVl(vl);
    }
}
