// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.checks;

import com.moonsworth.fallback.check.AbstractCheck;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.PositionUpdate;

public abstract class PositionCheck extends AbstractCheck<PositionUpdate>
{
    public PositionCheck(final PlayerData playerData, final String name) {
        super(playerData, PositionUpdate.class, name);
    }
}
