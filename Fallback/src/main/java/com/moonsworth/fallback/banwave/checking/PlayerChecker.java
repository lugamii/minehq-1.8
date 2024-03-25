// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.banwave.checking;

import java.util.ArrayList;

import com.moonsworth.fallback.Fallback;
import com.moonsworth.fallback.check.AbstractCheck;
import com.moonsworth.fallback.player.PlayerData;
import org.bukkit.entity.Player;

public class PlayerChecker
{
    public static ResultTypes checkPlayer(final Player player) {
        final PlayerData data = Fallback.instance.getPlayerDataManager().getPlayerData(player);
        final ArrayList<AbstractCheck> flagged = data.flaggedChecks;
        for (final AbstractCheck check : flagged) {
            final String name = check.getName().toLowerCase();
            final double percentage = data.getViolations(check) / 20 * 100;
            if (percentage > 50.0 && data.getViolations(check) > 2 && !name.contains("timer")) {
                return ResultTypes.FAILED;
            }
            if (data.getViolations(check) > 8) {
                return ResultTypes.UNSURE;
            }
        }
        return ResultTypes.PASS;
    }
}
