// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.scaffold;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.CustomLocation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class ScaffoldB extends PacketCheck
{
    private long lastPlace;
    private boolean place;
    
    public ScaffoldB(final PlayerData playerData) {
        super(playerData, "Placement (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        double vl = this.getVl();
        if (packet instanceof PacketPlayInBlockPlace && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && !this.playerData.isAllowTeleport()) {
            final CustomLocation lastMovePacket = this.playerData.getLastMovePacket();
            if (lastMovePacket == null) {
                return;
            }
            final long delay = System.currentTimeMillis() - lastMovePacket.getTimestamp();
            if (delay <= 25.0) {
                this.lastPlace = System.currentTimeMillis();
                this.place = true;
            }
            else {
                vl -= 0.25;
            }
        }
        else if (packet instanceof PacketPlayInFlying && this.place) {
            final long time = System.currentTimeMillis() - this.lastPlace;
            if (time >= 25L) {
                if (++vl >= 10.0) {
                    final AlertData[] alertData = { new AlertData("T", time), new AlertData("VL", vl) };
                    this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                }
            }
            else {
                vl -= 0.25;
            }
            this.place = false;
        }
        this.setVl(vl);
    }
}
