package com.netroaki.chex.world.aqua_mundus.mechanics;

import net.minecraftforge.common.ForgeConfigSpec;

/** Configuration for Aqua Mundus mechanics. */
public class AquaMundusConfig {
  public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  public static final ForgeConfigSpec SPEC;

  // Pressure settings
  public static final ForgeConfigSpec.DoubleValue BASE_PRESSURE_DAMAGE;
  public static final ForgeConfigSpec.DoubleValue PRESSURE_INCREASE_PER_METER;
  public static final ForgeConfigSpec.DoubleValue MAX_PRESSURE_DAMAGE;

  // Oxygen settings
  public static final ForgeConfigSpec.IntValue OXYGEN_DEPLETION_INTERVAL;
  public static final ForgeConfigSpec.IntValue OXYGEN_RESTORE_RATE;
  public static final ForgeConfigSpec.IntValue MAX_OXYGEN_LEVEL;

  // Thermal settings
  public static final ForgeConfigSpec.DoubleValue COLD_BIOME_THRESHOLD;
  public static final ForgeConfigSpec.DoubleValue HOT_BIOME_THRESHOLD;

  static {
    BUILDER.push("Aqua Mundus Mechanics");

    BUILDER.comment("Pressure Settings");
    BASE_PRESSURE_DAMAGE =
        BUILDER
            .comment("Base damage from pressure (per second)")
            .defineInRange("basePressureDamage", 1.0, 0.0, 20.0);

    PRESSURE_INCREASE_PER_METER =
        BUILDER
            .comment("Pressure damage increase per meter of depth")
            .defineInRange("pressureIncreasePerMeter", 0.1, 0.0, 5.0);

    MAX_PRESSURE_DAMAGE =
        BUILDER
            .comment("Maximum pressure damage (per second)")
            .defineInRange("maxPressureDamage", 5.0, 0.0, 20.0);

    BUILDER.comment("Oxygen Settings");
    OXYGEN_DEPLETION_INTERVAL =
        BUILDER
            .comment("Ticks between oxygen depletion")
            .defineInRange("oxygenDepletionInterval", 20, 1, 100);

    OXYGEN_RESTORE_RATE =
        BUILDER
            .comment("Oxygen restored per tick when at surface")
            .defineInRange("oxygenRestoreRate", 2, 1, 100);

    MAX_OXYGEN_LEVEL =
        BUILDER
            .comment("Maximum oxygen level (ticks of air)")
            .defineInRange("maxOxygenLevel", 300, 20, 3600);

    BUILDER.comment("Thermal Settings");
    COLD_BIOME_THRESHOLD =
        BUILDER
            .comment("Temperature below which cold effects are applied")
            .defineInRange("coldBiomeThreshold", 0.15, -1.0, 2.0);

    HOT_BIOME_THRESHOLD =
        BUILDER
            .comment("Temperature above which hot effects are applied")
            .defineInRange("hotBiomeThreshold", 0.95, -1.0, 2.0);

    BUILDER.pop();
    SPEC = BUILDER.build();
  }
}
