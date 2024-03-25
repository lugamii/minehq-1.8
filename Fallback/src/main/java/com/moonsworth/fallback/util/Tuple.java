// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

public class Tuple<A, B>
{
    private A a;
    private B b;
    
    public Tuple(final A var1, final B var2) {
        this.a = var1;
        this.b = var2;
    }
    
    public A a() {
        return this.a;
    }
    
    public B b() {
        return this.b;
    }
}
