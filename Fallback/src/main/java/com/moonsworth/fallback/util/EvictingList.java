// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import java.util.function.Predicate;
import java.util.Collection;
import java.util.LinkedList;

public final class EvictingList<T> extends LinkedList<T>
{
    private int maxSize;
    
    public EvictingList(final int maxSize) {
        this.maxSize = maxSize;
    }
    
    public EvictingList(final Collection<? extends T> c, final int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    @Override
    public boolean add(final T t) {
        if (this.size() >= this.maxSize) {
            this.removeFirst();
        }
        return super.add(t);
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return c.stream().anyMatch(this::add);
    }
    
    @Override
    public Stream<T> stream() {
        return new CopyOnWriteArrayList<T>((Collection<? extends T>)this).stream();
    }
}
