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
import java.util.LinkedList;
import java.util.Deque;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class TimerA extends PacketCheck
{
    private Deque<Long> delays;
    private long lastPacketTime;
    
    public TimerA(final PlayerData playerData) {
        super(playerData, "Timer (A)");
        this.delays = new LinkedList<Long>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying && !this.playerData.isAllowTeleport() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L) {
            this.delays.add(System.currentTimeMillis() - this.lastPacketTime);
            if (this.delays.size() == 40) {
                double average = 0.0;
                for (final long l : this.delays) {
                    average += l;
                }
                average /= this.delays.size();
                double vl = this.getVl();
                if (average <= 49.0) {
                    final AlertData[] alertData = { new AlertData("AVG", average), new AlertData("R", 50.0 / average), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                    if ((vl += 1.25) < 4.0 || this.alert(player, AlertType.RELEASE, alertData, false)) {}
                }
                else {
                    vl -= 0.5;
                }
                this.setVl(vl);
                this.delays.clear();
            }
            this.lastPacketTime = System.currentTimeMillis();
        }
    }
}
