// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.killaura;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class KillAuraB extends PacketCheck
{
    private boolean sent;
    private boolean failed;
    private int movements;
    
    public KillAuraB(final PlayerData playerData) {
        super(playerData, "Kill Aura (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (this.playerData.isDigging() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && !this.playerData.isInstantBreakDigging()) {
            int vl = (int)this.getVl();
            if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                this.movements = 0;
                vl = 0;
            }
            else if (packet instanceof PacketPlayInArmAnimation && this.movements >= 2) {
                if (this.sent) {
                    if (!this.failed) {
                        if (++vl >= 5) {
                            final AlertData[] alertData = { new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                            this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                        }
                        this.failed = true;
                    }
                }
                else {
                    this.sent = true;
                }
            }
            else if (packet instanceof PacketPlayInFlying) {
                final boolean b = false;
                this.failed = false;
                this.sent = false;
                ++this.movements;
            }
            this.setVl(vl);
        }
    }
}
