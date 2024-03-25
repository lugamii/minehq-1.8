// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.BlockPos;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.entity.Player;

public class AutoClickerJ extends PacketCheck
{
    private int stage;
    
    public AutoClickerJ(final PlayerData playerData) {
        super(playerData, "Auto Clicker (J)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (this.stage == 0) {
            if (packet instanceof PacketPlayInArmAnimation) {
                ++this.stage;
            }
        }
        else if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig blockDig = (PacketPlayInBlockDig)packet;
            final BlockPos blockPos = new BlockPos(blockDig.a());
            if (this.playerData.getFakeBlocks().contains(blockPos)) {
                this.stage = 0;
                return;
            }
            double vl = this.getVl();
            final PacketPlayInBlockDig.EnumPlayerDigType digType = ((PacketPlayInBlockDig)packet).c();
            if (digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                if (this.stage == 1) {
                    ++this.stage;
                }
                else {
                    this.stage = 0;
                }
            }
            else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                if (this.stage == 2) {
                    final AlertData[] alertData = { new AlertData("VL", vl), new AlertData("Client", this.playerData.getClient().getName()) };
                    if ((vl += 1.4) < 15.0 || this.alert(player, AlertType.RELEASE, alertData, true)) {}
                }
                else {
                    this.stage = 0;
                    vl -= 0.25;
                }
            }
            else {
                this.stage = 0;
            }
            this.setVl(vl);
        }
        else {
            this.stage = 0;
        }
    }
}
