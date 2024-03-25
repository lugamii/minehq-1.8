// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.timer;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class TimerB extends PacketCheck
{
    private long lastPacketTime;
    private double balance;
    
    public TimerB(final PlayerData playerData) {
        super(playerData, "Timer (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        double vl = this.getVl();
        if (packet instanceof PacketPlayInFlying && !this.playerData.isAllowTeleport()) {
            final long time = System.currentTimeMillis();
            final long lastPacketTime = (this.lastPacketTime != 0L) ? this.lastPacketTime : (time - 50L);
            this.lastPacketTime = time;
            final long rate = time - lastPacketTime;
            this.balance += 50.0;
            this.balance -= rate;
            if (this.balance >= 10.0) {
                final AlertData[] alertData = { new AlertData("BL", this.balance), new AlertData("R", rate), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                this.balance = 0.0;
                if (++vl < 5.0 || this.alert(player, AlertType.EXPERIMENTAL, alertData, false)) {}
            }
            this.setVl(vl);
        }
    }
}
