package com.netroaki.chex.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ArrakisEnvironmentConfig {
  public static final ServerConfig SERVER;
  public static final ForgeConfigSpec SERVER_SPEC;

  static {
    final Pair<ServerConfig, ForgeConfigSpec> specPair =
        new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    SERVER_SPEC = specPair.getRight();
    SERVER = specPair.getLeft();
  }

  public static class ServerConfig {
    // Heat System
    public final ForgeConfigSpec.IntValue heatDamageInterval;
    public final ForgeConfigSpec.DoubleValue heatDamageAmount;
    public final ForgeConfigSpec.DoubleValue maxHeatMultiplier;

    // Dust Storms
    public final ForgeConfigSpec.IntValue dustStormCheckInterval;
    public final ForgeConfigSpec.DoubleValue dustStormChance;
    public final ForgeConfigSpec.IntValue minDustStormDuration;
    public final ForgeConfigSpec.IntValue maxDustStormDuration;
    public final ForgeConfigSpec.DoubleValue dustStormFogDensity;

    // Visual Effects
    public final ForgeConfigSpec.BooleanValue enableHeatDistortion;
    public final ForgeConfigSpec.BooleanValue enableDustParticles;
    public final ForgeConfigSpec.BooleanValue enableRedSky;

    // Audio
    public final ForgeConfigSpec.BooleanValue enableWindSounds;
    public final ForgeConfigSpec.DoubleValue windSoundVolume;

    public ServerConfig(ForgeConfigSpec.Builder builder) {
      builder.comment("Arrakis Environment Configuration").push("arrakis_environment");

      builder.comment("Heat System Settings").push("heat");

      heatDamageInterval =
          builder
              .comment("Ticks between heat damage checks (20 ticks = 1 second)")
              .defineInRange("heatDamageInterval", 100, 20, 1200);

      heatDamageAmount =
          builder
              .comment("Amount of damage taken from heat exposure")
              .defineInRange("heatDamageAmount", 2.0, 0.5, 20.0);

      maxHeatMultiplier =
          builder
              .comment("Maximum heat multiplier at peak temperature")
              .defineInRange("maxHeatMultiplier", 1.5, 0.5, 5.0);

      builder.pop();

      builder.comment("Dust Storm Settings").push("dust_storm");

      dustStormCheckInterval =
          builder
              .comment("Ticks between dust storm checks")
              .defineInRange("dustStormCheckInterval", 200, 20, 2400);

      dustStormChance =
          builder
              .comment("Chance of a dust storm starting when conditions are met")
              .defineInRange("dustStormChance", 0.2, 0.0, 1.0);

      minDustStormDuration =
          builder
              .comment("Minimum duration of a dust storm in ticks")
              .defineInRange("minDustStormDuration", 1200, 100, 72000);

      maxDustStormDuration =
          builder
              .comment("Maximum duration of a dust storm in ticks")
              .defineInRange("maxDustStormDuration", 3600, 100, 144000);

      dustStormFogDensity =
          builder
              .comment("Density of fog during dust storms (0.0 to 1.0)")
              .defineInRange("dustStormFogDensity", 0.5, 0.0, 1.0);

      builder.pop();

      builder.comment("Visual Effect Settings").push("visuals");

      enableHeatDistortion =
          builder.comment("Enable heat distortion effects").define("enableHeatDistortion", true);

      enableDustParticles =
          builder.comment("Enable dust particle effects").define("enableDustParticles", true);

      enableRedSky =
          builder.comment("Enable red sky effect during the day").define("enableRedSky", true);

      builder.pop();

      builder.comment("Audio Settings").push("audio");

      enableWindSounds =
          builder.comment("Enable wind sound effects").define("enableWindSounds", true);

      windSoundVolume =
          builder
              .comment("Volume of wind sounds (0.0 to 1.0)")
              .defineInRange("windSoundVolume", 0.7, 0.0, 1.0);

      builder.pop();
      builder.pop();
    }
  }
}
