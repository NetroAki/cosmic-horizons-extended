package com.netroaki.chex.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class AquaMundusConfig {
  public static final ServerConfig SERVER;
  public static final ForgeConfigSpec SERVER_SPEC;

  static {
    final Pair<ServerConfig, ForgeConfigSpec> specPair =
        new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    SERVER_SPEC = specPair.getRight();
    SERVER = specPair.getLeft();
  }

  public static class ServerConfig {
    // Oxygen System
    public final ForgeConfigSpec.IntValue oxygenDepletionRate;
    public final ForgeConfigSpec.IntValue oxygenDamageInterval;
    public final ForgeConfigSpec.IntValue maxOxygenLevel;

    // Pressure System
    public final ForgeConfigSpec.DoubleValue pressureDamage;
    public final ForgeConfigSpec.IntValue maxSafeDepth;
    public final ForgeConfigSpec.BooleanValue enablePressureDamage;

    // Thermal System
    public final ForgeConfigSpec.DoubleValue thermalDamage;
    public final ForgeConfigSpec.IntValue thermalVentRadius;
    public final ForgeConfigSpec.BooleanValue enableThermalDamage;

    public ServerConfig(ForgeConfigSpec.Builder builder) {
      builder.comment("Aqua Mundus Configuration").push("aqua_mundus");

      // Oxygen System Configuration
      builder.comment("Oxygen System Settings").push("oxygen");

      oxygenDepletionRate =
          builder
              .comment("How quickly oxygen depletes (ticks between depletion)")
              .defineInRange("oxygenDepletionRate", 20, 1, 100);

      oxygenDamageInterval =
          builder
              .comment("Ticks between damage when out of oxygen")
              .defineInRange("oxygenDamageInterval", 20, 1, 100);

      maxOxygenLevel =
          builder
              .comment("Maximum oxygen level (seconds of air)")
              .defineInRange("maxOxygenLevel", 300, 60, 1200);

      builder.pop();

      // Pressure System Configuration
      builder.comment("Pressure System Settings").push("pressure");

      pressureDamage =
          builder
              .comment("Base damage per second when pressure is too high")
              .defineInRange("pressureDamage", 2.0, 0.1, 10.0);

      maxSafeDepth =
          builder
              .comment("Maximum safe depth before pressure damage starts (in blocks)")
              .defineInRange("maxSafeDepth", 15, 1, 100);

      enablePressureDamage =
          builder.comment("Enable pressure damage system").define("enablePressureDamage", true);

      builder.pop();

      // Thermal System Configuration
      builder.comment("Thermal System Settings").push("thermal");

      thermalDamage =
          builder
              .comment("Damage per second when too close to thermal vents")
              .defineInRange("thermalDamage", 1.0, 0.1, 10.0);

      thermalVentRadius =
          builder
              .comment("Radius to check for thermal vents (in blocks)")
              .defineInRange("thermalVentRadius", 5, 1, 20);

      enableThermalDamage =
          builder.comment("Enable thermal damage system").define("enableThermalDamage", true);

      builder.pop();

      builder.pop();
    }
  }
}
