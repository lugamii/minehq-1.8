// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.check.impl.aimassist.sensitivity;

import java.util.HashMap;

import com.moonsworth.fallback.player.PlayerData;
import com.moonsworth.fallback.util.EvictingList;
import com.moonsworth.fallback.util.MathUtil;
import com.moonsworth.fallback.util.update.RotationUpdate;
import org.bukkit.entity.Player;

import java.util.Map;
import com.moonsworth.fallback.check.checks.RotationCheck;

public class Sensitivity extends RotationCheck
{
    private static Map<Double, Double> sensitivityMap;
    private final EvictingList<Double> samples;
    private double lastDeltaPitch;
    private double lastMode;
    private double lastConstant;
    private double recordedConstant;
    
    public Sensitivity(final PlayerData playerData) {
        super(playerData, "Sensitivity");
        this.samples = new EvictingList<Double>(50);
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate rotationUpdate) {
        final double deltaPitch = Math.abs(rotationUpdate.getTo().getPitch() - rotationUpdate.getFrom().getPitch());
        final long expandedPitch = (long)Math.abs(deltaPitch * MathUtil.EXPANDER);
        final long lastExpandedPitch = (long)Math.abs(this.lastDeltaPitch * MathUtil.EXPANDER);
        final double pitchGcd = MathUtil.getGcd(expandedPitch, lastExpandedPitch) / MathUtil.EXPANDER;
        this.samples.add(pitchGcd);
        if (this.samples.size() == 50) {
            final double mode = MathUtil.getMode(this.samples);
            final long expandedMode = (long)(mode * MathUtil.EXPANDER);
            final long lastExpandedMode = (long)(this.lastMode * MathUtil.EXPANDER);
            final double modeGcd = (double)MathUtil.getGcd(expandedMode, lastExpandedMode);
            final double constant = Math.round((Math.cbrt(modeGcd / 0.15 / 8.0) - 0.33333333333333337) * 1000.0) / 1000.0;
            if (Math.abs(constant - this.lastConstant) < 0.01) {
                this.playerData.setVerifyingSensitivity(false);
                this.recordedConstant = constant;
            }
            else {
                this.playerData.setVerifyingSensitivity(true);
            }
            final double sensitivity = this.getSensitivity(this.recordedConstant);
            if (sensitivity > -1.0) {
                this.playerData.setSensitivity(sensitivity);
            }
            this.lastConstant = constant;
            this.lastMode = mode;
            this.samples.clear();
        }
        this.lastDeltaPitch = deltaPitch;
    }
    
    public double getSensitivity(final double constant) {
        for (final double val : Sensitivity.sensitivityMap.keySet()) {
            if (Math.abs(val - constant) <= 0.4) {
                return Sensitivity.sensitivityMap.get(val);
            }
        }
        return -1.0;
    }
    
    static {
        Sensitivity.sensitivityMap = new HashMap<Double, Double>();
        for (double d = 50.9; d < 204.8; d += 0.7725) {
            Sensitivity.sensitivityMap.put(d, Sensitivity.sensitivityMap.size() * 0.005);
        }
    }
}
