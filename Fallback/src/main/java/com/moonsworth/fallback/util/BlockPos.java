// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

import net.minecraft.server.v1_8_R3.BlockPosition;

import java.io.Serializable;

public class BlockPos implements Comparable<BlockPos>, Serializable
{
    private static long serialVersionUID;
    private int x;
    private int y;
    private int z;
    public static BlockPos ORIGIN;
    
    public BlockPos(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(BlockPosition position) {
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
    }
    
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof BlockPos)) {
            return false;
        }
        final BlockPos p = (BlockPos)other;
        return this.x == p.x && this.y == p.y && this.z == p.z;
    }
    
    @Override
    public int hashCode() {
        return (this.y & 0xFF) | (this.x & 0x7FFF) << 8 | (this.z & 0x7FFF) << 24 | ((this.x < 0) ? Integer.MIN_VALUE : 0) | ((this.z < 0) ? 32768 : 0);
    }
    
    @Override
    public int compareTo(final BlockPos pos) {
        if (this.y != pos.y) {
            return this.y - pos.y;
        }
        if (this.z == pos.z) {
            return this.x - pos.x;
        }
        return this.z - pos.z;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public BlockPos offset(final int x, final int y, final int z) {
        return new BlockPos(this.x + x, this.y + y, this.z + z);
    }
    
    public BlockPos above() {
        return new BlockPos(this.x, this.y + 1, this.z);
    }
    
    public BlockPos above(final int steps) {
        return new BlockPos(this.x, this.y + steps, this.z);
    }
    
    public BlockPos below() {
        return new BlockPos(this.x, this.y - 1, this.z);
    }
    
    public BlockPos below(final int steps) {
        return new BlockPos(this.x, this.y - steps, this.z);
    }
    
    public BlockPos north() {
        return new BlockPos(this.x, this.y, this.z - 1);
    }
    
    public BlockPos north(final int steps) {
        return new BlockPos(this.x, this.y, this.z - steps);
    }
    
    public BlockPos south() {
        return new BlockPos(this.x, this.y, this.z + 1);
    }
    
    public BlockPos south(final int steps) {
        return new BlockPos(this.x, this.y, this.z + steps);
    }
    
    public BlockPos west() {
        return new BlockPos(this.x - 1, this.y, this.z);
    }
    
    public BlockPos west(final int steps) {
        return new BlockPos(this.x - steps, this.y, this.z);
    }
    
    public BlockPos east() {
        return new BlockPos(this.x + 1, this.y, this.z);
    }
    
    public BlockPos east(final int steps) {
        return new BlockPos(this.x + steps, this.y, this.z);
    }
    
    public double dist(final int x, final int y, final int z) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        final int dz = this.z - z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    public double dist(final BlockPos pos) {
        return this.dist(pos.x, pos.y, pos.z);
    }
    
    public double distSqr(final int x, final int y, final int z) {
        final int dx = this.x - x;
        final int dy = this.y - y;
        final int dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public double distSqr(final float x, final float y, final float z) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        final float dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public double distSqr(final double x, final double y, final double z) {
        final double dx = this.x - x;
        final double dy = this.y - y;
        final double dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public double distSqr(final BlockPos pos) {
        return this.distSqr(pos.x, pos.y, pos.z);
    }
    
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
    
    static {
        BlockPos.serialVersionUID = -8966009362001100977L;
        BlockPos.ORIGIN = new BlockPos(0, 0, 0);
    }
}
