// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class AutoClickerL extends PacketCheck
{
    private int movements;
    private int failed;
    private int passed;
    private int stage;
    
    public AutoClickerL(final PlayerData playerData) {
        super(playerData, "Auto Clicker (L)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L) {
            if (packet instanceof PacketPlayInArmAnimation) {
                if (this.stage == 0 || this.stage == 1) {
                    ++this.stage;
                }
                else {
                    this.stage = 1;
                }
            }
            else if (packet instanceof PacketPlayInFlying) {
                if (this.stage == 2) {
                    ++this.stage;
                }
                else {
                    this.stage = 0;
                }
                ++this.movements;
            }
            else if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                if (this.stage == 3) {
                    ++this.failed;
                }
                else {
                    ++this.passed;
                }
                if (this.movements >= 200 && this.failed + this.passed > 60) {
                    final double rat = (this.passed == 0) ? -1.0 : (this.failed / this.passed);
                    double vl = this.getVl();
                    if (rat > 2.5) {
                        if ((vl += 1.0 + (rat - 2.0) * 0.75) >= 4.0) {
                            final AlertData[] alertData = { new AlertData("RAT", rat), new AlertData("VL", vl) };
                            this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                        }
                    }
                    else {
                        vl -= 2.0;
                    }
                    this.setVl(vl);
                    this.movements = 0;
                    this.passed = 0;
                    this.failed = 0;
                }
            }
        }
        else {
            this.stage = 0;
        }
    }
}
