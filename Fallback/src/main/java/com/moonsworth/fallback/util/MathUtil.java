// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.MathHelper;

public final class MathUtil
{
    public static double EXPANDER;
    
    public static float[] getRotationFromPosition(final CustomLocation playerLocation, final CustomLocation targetLocation) {
        final double xDiff = targetLocation.getX() - playerLocation.getX();
        final double zDiff = targetLocation.getZ() - playerLocation.getZ();
        final double yDiff = targetLocation.getY() - (playerLocation.getY() + 0.12);
        final double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static int getPotionEffectLevel(final Player player, final PotionEffectType pet) {
        for (final PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    public static double getMagnitude(final Location from, final Location to) {
        if (from.getWorld() != to.getWorld()) {
            return 0.0;
        }
        final double deltaX = to.getX() - from.getX();
        final double deltaZ = to.getZ() - from.getZ();
        return deltaX * deltaX + deltaZ * deltaZ;
    }
    
    /*public static int getPotionLevel(final Player player, final PotionEffectType effect) {
        final int effectId = effect.getId();
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            return 0;
        }
        return player.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().getId() == effectId).map((Function<? super Object, ? extends Integer>)PotionEffect::getAmplifier).findAny().orElse(0) + 1;
    }*/
    
    public static long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }
    
    public static double getStandardDeviation(final Iterable<? extends Number> data) {
        final double variance = getVariance(data);
        return Math.sqrt(variance);
    }

    public static <T extends Number> T getMode(Collection<T> collect) {
        Map<T, Integer> repeated = new HashMap();
        collect.forEach((val) -> {
            int number = (Integer)repeated.getOrDefault(val, 0);
            repeated.put(val, number + 1);
        });
        return (T) ((Tuple)repeated.keySet().stream().map((key) -> {
            return new Tuple(key, repeated.get(key));
        }).max(Comparator.comparing((tup) -> {
            return (Integer)tup.b();
        }, Comparator.naturalOrder())).orElseThrow(NullPointerException::new)).a();
    }
    
    public static double getVariance(final Iterable<? extends Number> data) {
        int count = 0;
        double sum = 0.0;
        double variance = 0.0;
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }
        final double average = sum / count;
        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }
        return variance;
    }
    
    public static double getHorizontalDistance(final Location from, final Location to) {
        final double deltaX = to.getX() - from.getX();
        final double deltaZ = to.getZ() - from.getZ();
        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }
    
    public static double average(final Collection<Integer> numbers) {
        double average = 0.0;
        for (final int i : numbers) {
            average += i;
        }
        average /= numbers.size();
        return average;
    }
    
    public static double stDeviation(final Collection<Integer> numbers) {
        final double average = average(numbers);
        double stdDev = 0.0;
        for (final int j : numbers) {
            stdDev += Math.pow(j - average, 2.0);
        }
        stdDev /= numbers.size();
        stdDev = Math.sqrt(stdDev);
        return stdDev;
    }
    
    public static float getBaseSpeed(final Player player) {
        return 0.25f + getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.062f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }
    
    public static int pingFormula(final long ping) {
        return (int)Math.ceil(ping / 2L / 50.0) + 2;
    }
    
    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }
    
    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        MathUtil.EXPANDER = Math.pow(2.0, 24.0);
    }
}
