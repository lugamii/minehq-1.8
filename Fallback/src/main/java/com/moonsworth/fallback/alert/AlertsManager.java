// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.alert;

import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.UUID;
import java.util.Set;

public class AlertsManager
{
    private Set<UUID> alertsToggled;
    
    public AlertsManager() {
        this.alertsToggled = new HashSet<UUID>();
    }
    
    public boolean hasAlertsToggled(final Player player) {
        return this.alertsToggled.contains(player.getUniqueId());
    }
    
    public void toggleAlerts(final Player player) {
        if (!this.alertsToggled.remove(player.getUniqueId())) {
            this.alertsToggled.add(player.getUniqueId());
        }
    }
    
    public Set<UUID> getAlertsToggled() {
        return this.alertsToggled;
    }
}
