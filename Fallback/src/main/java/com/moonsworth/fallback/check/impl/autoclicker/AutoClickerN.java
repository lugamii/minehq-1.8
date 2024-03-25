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

public class AutoClickerN extends PacketCheck
{
    private int ticks;
    private int clicks;
    private int oldClicks;
    private long lastClick;
    
    public AutoClickerN(final PlayerData playerData) {
        super(playerData, "Auto Clicker (N)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation && !this.playerData.isPlacing() && !this.playerData.isDigging()) {
            this.ticks = 0;
            if (System.currentTimeMillis() - this.lastClick > 250L) {
                this.clicks = this.oldClicks;
            }
            this.lastClick = System.currentTimeMillis();
        }
        if (packet instanceof PacketPlayInFlying) {
            if (++this.ticks <= 2) {
                if (++this.clicks > 100) {
                    if (this.clicks > 220) {
                        final AlertData[] alertData = { new AlertData("CL ", this.clicks), new AlertData("OC ", this.oldClicks) };
                        this.alert(player, AlertType.RELEASE, alertData, true);
                    }
                    if (this.clicks > 400) {
                        final AlertData[] alertData = { new AlertData("LC ", this.lastClick), new AlertData("T ", this.ticks) };
                        this.alert(player, AlertType.RELEASE, alertData, true);
                        this.oldClicks = this.clicks;
                        this.clicks = 0;
                    }
                }
            }
            else if (this.ticks == 3) {
                this.oldClicks = this.clicks;
                this.clicks = 0;
            }
        }
    }
}
