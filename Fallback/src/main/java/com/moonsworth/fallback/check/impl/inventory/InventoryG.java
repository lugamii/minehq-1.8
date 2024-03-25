// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.inventory;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.check.checks.PacketCheck;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Player;

public class InventoryG extends PacketCheck
{
    private boolean sent;
    private boolean vehicle;
    
    public InventoryG(final PlayerData playerData) {
        super(playerData, "Inventory (G)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.sent) {
                this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], true);
            }
            this.vehicle = false;
            this.sent = false;
        }
        else if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand)packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            if (this.playerData.isSprinting() && !this.vehicle) {
                this.sent = true;
            }
        }
        else if (packet instanceof PacketPlayInEntityAction && ((PacketPlayInEntityAction)packet).b() == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
            this.sent = false;
        }
        else if (packet instanceof PacketPlayInSteerVehicle) {
            this.vehicle = true;
        }
    }
}
