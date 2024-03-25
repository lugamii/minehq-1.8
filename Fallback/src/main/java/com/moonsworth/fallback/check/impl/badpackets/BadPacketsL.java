// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.badpackets;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class BadPacketsL extends PacketCheck
{
    private boolean sent;
    private boolean vehicle;
    
    public BadPacketsL(final PlayerData playerData) {
        super(playerData, "Invalid Packets (L)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.sent) {
                this.alert(player, AlertType.EXPERIMENTAL, new AlertData[0], false);
            }
            this.vehicle = false;
            this.sent = false;
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace blockPlace = (PacketPlayInBlockPlace)packet;
            if (blockPlace.getFace() == 255) {
                final ItemStack itemStack = blockPlace.getItemStack();
                if (itemStack != null && itemStack.getName().toLowerCase().contains("sword") && this.playerData.isSprinting() && !this.vehicle) {
                    this.sent = true;
                }
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
