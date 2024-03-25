// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.inventory;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class InventoryB extends PacketCheck
{
    public InventoryB(final PlayerData playerData) {
        super(playerData, "Inventory (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (((packet instanceof PacketPlayInEntityAction && ((PacketPlayInEntityAction)packet).b() == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING) || packet instanceof PacketPlayInArmAnimation) && this.playerData.isInventoryOpen()) {
            if (this.alert(player, AlertType.RELEASE, new AlertData[0], true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 5) {
                    this.ban(player);
                }
            }
            this.playerData.setInventoryOpen(false);
        }
    }
}
