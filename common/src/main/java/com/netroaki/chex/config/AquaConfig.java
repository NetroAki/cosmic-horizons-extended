package com.netroaki.chex.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AquaConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Oxygen System
    public static final ForgeConfigSpec.IntValue OXYGEN_CONSUMPTION_RATE;
    public static final ForgeConfigSpec.IntValue OXYGEN_DAMAGE_INTERVAL;
    public static final ForgeConfigSpec.IntValue OXYGEN_TANK_CAPACITY;
    
    // Pressure System
    public static final ForgeConfigSpec.IntValue PRESSURE_DEPTH_THRESHOLD;
    public static final ForgeConfigSpec.IntValue PRESSURE_DAMAGE_INTERVAL;
    
    // Thermal System
    public static final ForgeConfigSpec.DoubleValue COLD_DAMAGE_THRESHOLD;
    public static final ForgeConfigSpec.DoubleValue HEAT_DAMAGE_THRESHOLD;
    public static final ForgeConfigSpec.IntValue THERMAL_DAMAGE_INTERVAL;

    static {
        BUILDER.push("Aqua Mundus Mechanics Configuration");
        
        // Oxygen System Configuration
        BUILDER.comment("Oxygen System Settings").push("oxygen");
        OXYGEN_CONSUMPTION_RATE = BUILDER
            .comment("Ticks between each oxygen consumption (20 ticks = 1 second)")
            .defineInRange("oxygenConsumptionRate", 20, 1, 200);
            
        OXYGEN_DAMAGE_INTERVAL = BUILDER
            .comment("Ticks between damage when out of oxygen (20 ticks = 1 second)")
            .defineInRange("oxygenDamageInterval", 40, 10, 200);
            
        OXYGEN_TANK_CAPACITY = BUILDER
            .comment("Maximum oxygen capacity in ticks (default: 20 minutes)")
            .defineInRange("oxygenTankCapacity", 24000, 100, 100000);
        BUILDER.pop();
        
        // Pressure System Configuration
        BUILDER.comment("Pressure System Settings").push("pressure");
        PRESSURE_DEPTH_THRESHOLD = BUILDER
            .comment("Depth at which pressure effects start (in blocks)")
            .defineInRange("pressureDepthThreshold", 30, 5, 200);
            
        PRESSURE_DAMAGE_INTERVAL = BUILDER
            .comment("Ticks between damage when under high pressure without protection")
            .defineInRange("pressureDamageInterval", 40, 10, 200);
        BUILDER.pop();
        
        // Thermal System Configuration
        BUILDER.comment("Thermal System Settings").push("thermal");
        COLD_DAMAGE_THRESHOLD = BUILDER
            .comment("Temperature below which cold damage is applied")
            .defineInRange("coldDamageThreshold", 0.1, -2.0, 2.0);
            
        HEAT_DAMAGE_THRESHOLD = BUILDER
            .comment("Temperature above which heat damage is applied")
            .defineInRange("heatDamageThreshold", 1.5, 0.0, 5.0);
            
        THERMAL_DAMAGE_INTERVAL = BUILDER
            .comment("Ticks between thermal damage")
            .defineInRange("thermalDamageInterval", 80, 10, 200);
        BUILDER.pop();
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
    
    // Helper methods to access config values
    public static int getOxygenConsumptionRate() {
        return OXYGEN_CONSUMPTION_RATE.get();
    }
    
    public static int getOxygenDamageInterval() {
        return OXYGEN_DAMAGE_INTERVAL.get();
    }
    
    public static int getOxygenTankCapacity() {
        return OXYGEN_TANK_CAPACITY.get();
    }
    
    public static int getPressureDepthThreshold() {
        return PRESSURE_DEPTH_THRESHOLD.get();
    }
    
    public static int getPressureDamageInterval() {
        return PRESSURE_DAMAGE_INTERVAL.get();
    }
    
    public static double getColdDamageThreshold() {
        return COLD_DAMAGE_THRESHOLD.get();
    }
    
    public static double getHeatDamageThreshold() {
        return HEAT_DAMAGE_THRESHOLD.get();
    }
    
    public static int getThermalDamageInterval() {
        return THERMAL_DAMAGE_INTERVAL.get();
    }
}
