// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.inventory;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class InventoryA extends PacketCheck
{
    public InventoryA(final PlayerData playerData) {
        super(playerData, "Inventory (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInWindowClick && ((PacketPlayInWindowClick)packet).c() == 0 && !this.playerData.isInventoryOpen()) {
            if (this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 5) {
                    this.ban(player);
                }
            }
            this.playerData.setInventoryOpen(true);
        }
    }
}
