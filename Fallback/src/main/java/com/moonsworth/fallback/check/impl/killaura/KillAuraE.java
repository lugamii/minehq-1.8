// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.CustomLocation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class KillAuraE extends PacketCheck
{
    private long lastAttack;
    private boolean attack;
    
    public KillAuraE(final PlayerData playerData) {
        super(playerData, "Kill Aura (E)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        double vl = this.getVl();
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && !this.playerData.isAllowTeleport()) {
            final CustomLocation lastMovePacket = this.playerData.getLastMovePacket();
            if (lastMovePacket == null) {
                return;
            }
            final long delay = System.currentTimeMillis() - lastMovePacket.getTimestamp();
            if (delay <= 25.0) {
                this.lastAttack = System.currentTimeMillis();
                this.attack = true;
            }
            else {
                vl -= 0.25;
            }
        }
        else if (packet instanceof PacketPlayInFlying && this.attack) {
            final long time = System.currentTimeMillis() - this.lastAttack;
            if (time >= 25L) {
                final AlertData[] alertData = { new AlertData("T", time), new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                if (++vl < 10.0 || this.alert(player, AlertType.EXPERIMENTAL, alertData, false)) {}
            }
            else {
                vl -= 0.25;
            }
            this.attack = false;
        }
        this.setVl(vl);
    }
}
