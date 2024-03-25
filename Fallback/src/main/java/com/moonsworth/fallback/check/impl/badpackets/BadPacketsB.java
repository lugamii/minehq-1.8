// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.badpackets;

import com.moonsworth.fallback.alert.AlertData;
import com.moonsworth.fallback.event.AlertType;
import com.moonsworth.fallback.player.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import com.moonsworth.fallback.check.checks.PacketCheck;

public class BadPacketsB extends PacketCheck
{
    public BadPacketsB(final PlayerData playerData) {
        super(playerData, "Invalid Packets (B)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying && Math.abs(((PacketPlayInFlying)packet).e()) > 90.0f && this.alert(player, AlertType.RELEASE, new AlertData[0], false) && !this.playerData.isBanning() && !this.playerData.isRandomBan()) {
            this.randomBan(player, 200.0);
        }
    }
}
