// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.inventory;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.CustomLocation;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.LinkedList;
import java.util.Deque;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class InventoryC extends PacketCheck
{
    private Deque<Long> delays;
    
    public InventoryC(final PlayerData playerData) {
        super(playerData, "Inventory (C)");
        this.delays = new LinkedList<Long>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInWindowClick && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && !this.playerData.isAllowTeleport()) {
            final CustomLocation lastMovePacket = this.playerData.getLastMovePacket();
            if (lastMovePacket == null) {
                return;
            }
            final long delay = System.currentTimeMillis() - lastMovePacket.getTimestamp();
            this.delays.add(delay);
            if (this.delays.size() == 10) {
                double average = 0.0;
                for (final long loopDelay : this.delays) {
                    average += loopDelay;
                }
                average /= this.delays.size();
                this.delays.clear();
                double vl = this.getVl();
                if (average <= 35.0) {
                    if ((vl += 1.25) >= 4.0) {
                        final AlertData[] alertData = { new AlertData("AVG", average), new AlertData("VL", vl) };
                        if (!this.alert(player, AlertType.RELEASE, alertData, true)) {
                            vl = 0.0;
                        }
                    }
                }
                else {
                    vl -= 0.5;
                }
                this.setVl(vl);
            }
        }
    }
}
