// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;

public class AutoClickerK extends PacketCheck
{
    private int stage;
    private boolean other;
    
    public AutoClickerK(final PlayerData playerData) {
        super(playerData, "Auto Clicker (K)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (this.stage == 0) {
            if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                ++this.stage;
            }
        }
        else if (this.stage == 1) {
            if (packet instanceof PacketPlayInArmAnimation) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (this.stage == 2) {
            if (packet instanceof PacketPlayInFlying) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (this.stage == 3) {
            if (packet instanceof PacketPlayInArmAnimation) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (this.stage == 4) {
            if (packet instanceof PacketPlayInFlying) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (this.stage == 5) {
            if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                if (this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], false)) {
                    this.checkBan(player);
                }
                this.stage = 0;
            }
            else if (packet instanceof PacketPlayInArmAnimation) {
                ++this.stage;
            }
            else if (packet instanceof PacketPlayInFlying) {
                this.other = true;
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (this.stage == 6) {
            if (!this.other) {
                if (packet instanceof PacketPlayInFlying) {
                    ++this.stage;
                }
                else {
                    this.stage = 0;
                }
            }
            else {
                if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                    final AlertData[] alertData = { new AlertData("Type", "B") };
                    if (this.alert(player, AlertType.EXPERIMENTAL, alertData, false)) {
                        this.checkBan(player);
                    }
                    this.other = false;
                }
                this.stage = 0;
            }
        }
        else if (this.stage == 7) {
            if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                final AlertData[] alertData = { new AlertData("Type", "C") };
                if (this.alert(player, AlertType.EXPERIMENTAL, alertData, false)) {
                    this.checkBan(player);
                }
            }
            else {
                this.stage = 0;
            }
        }
    }
    
    private void checkBan(final Player player) {
        final int violations = this.playerData.getViolations(this, 60000L);
        if (!this.playerData.isBanning() && violations > 2) {
            this.ban(player);
        }
    }
}
