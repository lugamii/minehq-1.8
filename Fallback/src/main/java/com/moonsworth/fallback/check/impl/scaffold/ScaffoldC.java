// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.scaffold;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class ScaffoldC extends PacketCheck
{
    private int looks;
    private int stage;
    
    public ScaffoldC(final PlayerData playerData) {
        super(playerData, "Placement (C)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        double vl = this.getVl();
        if (packet instanceof PacketPlayInFlying.PacketPlayInLook) {
            if (this.stage == 0) {
                ++this.stage;
            }
            else if (this.stage == 4) {
                if ((vl += 1.75) > 3.5) {
                    final AlertData[] alertData = { new AlertData("VL", vl) };
                    this.alert(player, AlertType.EXPERIMENTAL, alertData, false);
                }
                this.stage = 0;
            }
            else {
                this.looks = 0;
                this.stage = 0;
                vl -= 0.2;
            }
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            if (this.stage == 1) {
                ++this.stage;
            }
            else {
                this.looks = 0;
                this.stage = 0;
            }
        }
        else if (packet instanceof PacketPlayInArmAnimation) {
            if (this.stage == 2) {
                ++this.stage;
            }
            else {
                this.looks = 0;
                this.stage = 0;
                vl -= 0.2;
            }
        }
        else if (packet instanceof PacketPlayInFlying.PacketPlayInPositionLook || packet instanceof PacketPlayInFlying.PacketPlayInPosition) {
            if (this.stage == 3) {
                if (++this.looks == 3) {
                    this.stage = 4;
                    this.looks = 0;
                }
            }
            else {
                this.looks = 0;
                this.stage = 0;
            }
        }
        this.setVl(vl);
    }
}
