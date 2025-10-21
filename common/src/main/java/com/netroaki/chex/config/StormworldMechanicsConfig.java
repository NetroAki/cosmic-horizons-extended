package com.netroaki.chex.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class StormworldMechanicsConfig {
  public static final ForgeConfigSpec SERVER_SPEC;

  public static final ForgeConfigSpec.DoubleValue GRAVITY_HIGH_MULT; // e.g., 1.5x in storm bands
  public static final ForgeConfigSpec.BooleanValue GRAVITY_ENABLE_SLOW_FALLING;

  public static final ForgeConfigSpec.BooleanValue LIGHTNING_ENABLED;
  public static final ForgeConfigSpec.DoubleValue LIGHTNING_STRIKE_CHANCE; // per player per check
  public static final ForgeConfigSpec.IntValue LIGHTNING_CHECK_INTERVAL_TICKS;
  public static final ForgeConfigSpec.IntValue ELECTRIC_DAMAGE_BASE;
  public static final ForgeConfigSpec.IntValue ELECTRIC_REQUIRED_TIER;

  public static final ForgeConfigSpec.BooleanValue HUNGER_ACCEL_ENABLED;
  public static final ForgeConfigSpec.DoubleValue HUNGER_ACCEL_RATE; // exhaustion per check
  public static final ForgeConfigSpec.IntValue HUNGER_CHECK_INTERVAL_TICKS;

  public static final ForgeConfigSpec.BooleanValue PRESSURE_ENABLED;
  public static final ForgeConfigSpec.IntValue PRESSURE_REQUIRED_TIER;
  public static final ForgeConfigSpec.DoubleValue PRESSURE_DAMAGE_PER_TICK;

  static {
    ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

    b.push("stormworld_mechanics");
    GRAVITY_HIGH_MULT =
        b.comment("Movement speed multiplier penalty in high-gravity bands")
            .defineInRange("gravityHighMult", 0.8D, 0.4D, 1.0D);
    GRAVITY_ENABLE_SLOW_FALLING =
        b.comment("Apply Slow Falling in low-gravity vortices/eye")
            .define("gravityEnableSlowFalling", true);

    LIGHTNING_ENABLED = b.define("lightningEnabled", true);
    LIGHTNING_STRIKE_CHANCE =
        b.comment(
                "Chance (0-1) per player per check to attempt a lightning strike nearby in"
                    + " Lightning Fields")
            .defineInRange("lightningStrikeChance", 0.02D, 0.0D, 1.0D);
    LIGHTNING_CHECK_INTERVAL_TICKS = b.defineInRange("lightningCheckIntervalTicks", 40, 5, 400);
    ELECTRIC_DAMAGE_BASE = b.defineInRange("electricDamageBase", 6, 0, 100);
    ELECTRIC_REQUIRED_TIER =
        b.comment("Suit tier required to ignore lightning damage")
            .defineInRange("electricRequiredTier", 4, 0, 10);

    HUNGER_ACCEL_ENABLED = b.define("hungerAccelEnabled", true);
    HUNGER_ACCEL_RATE = b.defineInRange("hungerAccelRate", 0.6D, 0.0D, 5.0D);
    HUNGER_CHECK_INTERVAL_TICKS = b.defineInRange("hungerCheckIntervalTicks", 60, 5, 400);

    PRESSURE_ENABLED = b.define("pressureEnabled", true);
    PRESSURE_REQUIRED_TIER = b.defineInRange("pressureRequiredTier", 5, 0, 10);
    PRESSURE_DAMAGE_PER_TICK = b.defineInRange("pressureDamagePerTick", 1.0D, 0.0D, 20.0D);

    b.pop();

    SERVER_SPEC = b.build();
  }
}
