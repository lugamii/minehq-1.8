// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.alert;

import java.math.RoundingMode;
import java.math.BigDecimal;

public class AlertData
{
    private String name;
    private Object value;
    
    public AlertData(final String name, final Object value) {
        this.name = name;
        if (value.getClass() == Float.TYPE || value instanceof Float) {
            this.value = round(BigDecimal.valueOf((float)value), 2);
        }
        else if (value.getClass() == Double.TYPE || value instanceof Double) {
            this.value = round(BigDecimal.valueOf((double)value), 2);
        }
        else {
            this.value = value;
        }
    }
    
    public AlertData(final String s) {
    }
    
    private static double round(BigDecimal bigDecimal, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
    
    public String getName() {
        return this.name;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AlertData)) {
            return false;
        }
        final AlertData other = (AlertData)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof AlertData;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "AlertData(name=" + this.getName() + ", value=" + this.getValue() + ")";
    }
}
