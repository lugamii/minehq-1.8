// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.checks;

import com.moonsworth.fallback.check.AbstractCheck;
import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.update.RotationUpdate;

public abstract class RotationCheck extends AbstractCheck<RotationUpdate>
{
    public RotationCheck(final PlayerData playerData, final String name) {
        super(playerData, RotationUpdate.class, name);
    }
}
