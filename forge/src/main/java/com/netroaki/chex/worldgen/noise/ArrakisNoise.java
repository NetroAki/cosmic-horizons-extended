package com.netroaki.chex.worldgen.noise;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public class ArrakisNoise {
    private final ImprovedNoise[] noiseLevels;
    private final int levels;
    private final double minLimitNoiseValue;
    private final double maxLimitNoiseValue;
    private final double mainNoiseScaleX;
    private final double mainNoiseScaleY;
    private final double mainNoiseScaleZ;

    public ArrakisNoise(RandomSource random, int levels) {
        this.levels = levels;
        this.noiseLevels = new ImprovedNoise[levels];
        this.minLimitNoiseValue = 1.0D / (Math.pow(2.0D, levels) - 1.0D);
        this.maxLimitNoiseValue = (Math.pow(2.0D, levels) - 1.0D) / Math.pow(2.0D, levels - 1);
        
        for (int i = 0; i < levels; ++i) {
            this.noiseLevels[i] = new ImprovedNoise(random);
        }
        
        this.mainNoiseScaleX = 1.0D;
        this.mainNoiseScaleY = 1.0D;
        this.mainNoiseScaleZ = 1.0D;
    }
    
    public double getValue(double x, double y, double z) {
        double value = 0.0D;
        double scale = 1.0D;
        double totalScale = 0.0D;
        
        for (int i = 0; i < this.levels; ++i) {
            double nx = x * this.mainNoiseScaleX * scale;
            double ny = y * this.mainNoiseScaleY * scale;
            double nz = z * this.mainNoiseScaleZ * scale;
            
            // Use different noise functions for different levels
            double noise;
            if (i % 2 == 0) {
                noise = this.noiseLevels[i].noise(nx, ny, nz) * 2.0D;
            } else {
                noise = this.noiseLevels[i].noise(
                    nx * 0.5D + 0.5D, 
                    ny * 0.5D + 0.5D, 
                    nz * 0.5D + 0.5D
                ) * 2.0D - 1.0D;
            }
            
            value += noise * (1.0D / scale);
            totalScale += 1.0D / scale;
            scale *= 2.0D;
        }
        
        return value / totalScale;
    }
    
    public double getCanyonValue(double x, double z, double scale, double width, double depth) {
        double value = getValue(x / scale, 0, z / scale) * 0.5D + 0.5D;
        double distance = Math.sqrt(x * x + z * z) / width;
        double canyon = Mth.clamp(1.0D - distance * distance, 0.0D, 1.0D);
        return value * canyon * depth;
    }
    
    public double getMesaValue(double x, double z, double radius, double steepness) {
        double distance = Math.sqrt(x * x + z * z);
        double normalized = 1.0D - Mth.clamp(distance / radius, 0.0D, 1.0D);
        return Math.pow(normalized, steepness);
    }
}
