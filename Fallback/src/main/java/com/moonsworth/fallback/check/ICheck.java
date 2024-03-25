// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check;

import org.bukkit.entity.Player;

public interface ICheck<T>
{
    void handleCheck(final Player p0, final T p1);
    
    Class<? extends T> getType();
}
