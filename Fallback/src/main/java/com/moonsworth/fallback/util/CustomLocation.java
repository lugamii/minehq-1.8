// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import java.util.StringJoiner;
import org.bukkit.Location;

public class CustomLocation
{
    private long timestamp;
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    
    public CustomLocation(final double x, final double y, final double z) {
        this(x, y, z, 0.0f, 0.0f);
    }
    
    public CustomLocation(final String world, final double x, final double y, final double z) {
        this(world, x, y, z, 0.0f, 0.0f);
    }
    
    public CustomLocation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this("world", x, y, z, yaw, pitch);
    }
    
    public static CustomLocation fromBukkitLocation(final Location location) {
        return new CustomLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    public static CustomLocation stringToLocation(final String string) {
        final String[] split = string.split(", ");
        final double x = Double.parseDouble(split[0]);
        final double y = Double.parseDouble(split[1]);
        final double z = Double.parseDouble(split[2]);
        final CustomLocation customLocation = new CustomLocation(x, y, z);
        if (split.length == 4) {
            customLocation.setWorld(split[3]);
        }
        else if (split.length >= 5) {
            customLocation.setYaw(Float.parseFloat(split[3]));
            customLocation.setPitch(Float.parseFloat(split[4]));
            if (split.length >= 6) {
                customLocation.setWorld(split[5]);
            }
        }
        return customLocation;
    }
    
    public static String locationToString(final CustomLocation loc) {
        final StringJoiner joiner = new StringJoiner(", ");
        joiner.add(Double.toString(loc.getX()));
        joiner.add(Double.toString(loc.getY()));
        joiner.add(Double.toString(loc.getZ()));
        if (loc.getYaw() == 0.0f && loc.getPitch() == 0.0f) {
            if (loc.getWorld().equals("world")) {
                return joiner.toString();
            }
            joiner.add(loc.getWorld());
            return joiner.toString();
        }
        else {
            joiner.add(Float.toString(loc.getYaw()));
            joiner.add(Float.toString(loc.getPitch()));
            if (loc.getWorld().equals("world")) {
                return joiner.toString();
            }
            joiner.add(loc.getWorld());
            return joiner.toString();
        }
    }
    
    public Location toBukkitLocation() {
        return new Location(this.toBukkitWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
    }
    
    public double getGroundDistanceTo(final CustomLocation location) {
        return Math.sqrt(Math.pow(this.x - location.x, 2.0) + Math.pow(this.z - location.z, 2.0));
    }
    
    public double getDistanceTo(final CustomLocation location) {
        return Math.sqrt(Math.pow(this.x - location.x, 2.0) + Math.pow(this.y - location.y, 2.0) + Math.pow(this.z - location.z, 2.0));
    }
    
    public World toBukkitWorld() {
        if (this.world == null) {
            return Bukkit.getServer().getWorlds().get(0);
        }
        return Bukkit.getServer().getWorld(this.world);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof CustomLocation)) {
            return false;
        }
        final CustomLocation location = (CustomLocation)obj;
        return location.x == this.x && location.y == this.y && location.z == this.z && location.pitch == this.pitch && location.yaw == this.yaw;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder((Object)this).append("x", this.x).append("y", this.y).append("z", this.z).append("yaw", this.yaw).append("pitch", this.pitch).append("world", (Object)this.world).append("timestamp", this.timestamp).toString();
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public String getWorld() {
        return this.world;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setWorld(final String world) {
        this.world = world;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public CustomLocation(final String world, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.timestamp = System.currentTimeMillis();
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
