// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.autoclicker;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class AutoClickerA extends PacketCheck
{
    private int swings;
    private int movements;
    
    public AutoClickerA(final PlayerData playerData) {
        super(playerData, "Auto Clicker (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation && !this.playerData.isDigging() && !this.playerData.isPlacing()) {
            ++this.swings;
        }
        else if (packet instanceof PacketPlayInFlying && this.swings > 0 && ++this.movements == 20) {
            final AlertData[] alertData = { new AlertData("CPS ", this.swings) };
            new AlertData("Client", this.playerData.getClient().getName());
            if (this.swings > 20 && this.alert(player, AlertType.RELEASE, alertData, true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 15) {
                    this.ban(player);
                }
            }
            final int n = 0;
            this.swings = n;
            this.movements = n;
        }
        this.playerData.setLastCps(this.swings);
    }
}
