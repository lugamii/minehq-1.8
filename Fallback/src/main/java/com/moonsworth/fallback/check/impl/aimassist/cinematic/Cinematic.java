// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.aimassist.cinematic;

import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.GraphUtil;
import com.moonsworth.fallback.util.update.RotationUpdate;
import org.bukkit.entity.Player;
import com.google.common.collect.Lists;

import java.util.List;
import com.moonsworth.fallback.check.checks.RotationCheck;

public class Cinematic extends RotationCheck
{
    private long lastSmooth;
    private long lastHighRate;
    private double lastDeltaYaw;
    private double lastDeltaPitch;
    private List<Double> yawSamples;
    private List<Double> pitchSamples;
    
    public Cinematic(final PlayerData playerData) {
        super(playerData, "Cinematic");
        this.lastSmooth = 0L;
        this.lastHighRate = 0L;
        this.lastDeltaYaw = 0.0;
        this.lastDeltaPitch = 0.0;
        this.yawSamples = Lists.newArrayList();
        this.pitchSamples = Lists.newArrayList();
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate rotationUpdate) {
        final long now = System.currentTimeMillis();
        final double deltaYaw = Math.abs(rotationUpdate.getTo().getYaw() - rotationUpdate.getFrom().getYaw()) % 360.0f;
        final double deltaPitch = Math.abs(rotationUpdate.getTo().getPitch() - rotationUpdate.getFrom().getPitch());
        final double differenceYaw = Math.abs(deltaYaw - this.lastDeltaYaw);
        final double differencePitch = Math.abs(deltaPitch - this.lastDeltaPitch);
        final double joltYaw = Math.abs(differenceYaw - deltaYaw);
        final double joltPitch = Math.abs(differencePitch - deltaPitch);
        final boolean cinematic = now - this.lastHighRate > 250L || now - this.lastSmooth < 9000L;
        if (joltYaw > 1.0 && joltPitch > 1.0) {
            this.lastHighRate = now;
        }
        if (deltaYaw > 0.0 && deltaPitch > 0.0) {
            this.yawSamples.add(deltaYaw);
            this.pitchSamples.add(deltaPitch);
        }
        if (this.yawSamples.size() == 20 && this.pitchSamples.size() == 20) {
            final GraphUtil.GraphResult resultsYaw = GraphUtil.getGraph(this.yawSamples);
            final GraphUtil.GraphResult resultsPitch = GraphUtil.getGraph(this.pitchSamples);
            final int negativesYaw = resultsYaw.getNegatives();
            final int negativesPitch = resultsPitch.getNegatives();
            final int positivesYaw = resultsYaw.getPositives();
            final int positivesPitch = resultsPitch.getPositives();
            if (positivesYaw > negativesYaw || positivesPitch > negativesPitch) {
                this.lastSmooth = now;
            }
            this.yawSamples.clear();
            this.pitchSamples.clear();
        }
        this.playerData.setCinematic(cinematic);
        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }
}
