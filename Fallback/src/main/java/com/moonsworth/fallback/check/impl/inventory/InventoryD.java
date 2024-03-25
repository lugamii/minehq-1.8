// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.inventory;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class InventoryD extends PacketCheck
{
    private int stage;
    
    public InventoryD(final PlayerData playerData) {
        super(playerData, "Inventory (D)");
        this.stage = 0;
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (this.stage == 0) {
            if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand)packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
                ++this.stage;
            }
        }
        else if (this.stage == 1) {
            if (packet instanceof PacketPlayInFlying.PacketPlayInLook) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (this.stage == 2) {
            if (packet instanceof PacketPlayInFlying.PacketPlayInLook) {
                this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], false);
            }
            this.stage = 0;
        }
    }
}
