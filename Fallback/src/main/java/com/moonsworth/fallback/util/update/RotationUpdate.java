// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util.update;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RotationUpdate extends MovementUpdate
{
    public RotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packet) {
        super(player, to, from, packet);
    }
}
