// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.badpackets;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class BadPacketsA extends PacketCheck
{
    private int streak;
    
    public BadPacketsA(final PlayerData playerData) {
        super(playerData, "Invalid Packets (A)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (((PacketPlayInFlying)packet).g()) {
                this.streak = 0;
            }
            else if (++this.streak > 20 && this.alert(player, AlertType.RELEASE, new AlertData[0], false) && !this.playerData.isBanning()) {
                this.ban(player);
            }
        }
        else if (packet instanceof PacketPlayInSteerVehicle) {
            this.streak = 0;
        }
        else if (packet instanceof PacketPlayOutAttachEntity) {
            this.streak = 0;
        }
    }
}
