package com.netroaki.chex.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PandoraConfig {
  public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  public static final ForgeConfigSpec SPEC;

  // Hazard Toggles
  public static final ForgeConfigSpec.BooleanValue ENABLE_LEVITATION_UPDRAFTS;
  public static final ForgeConfigSpec.BooleanValue ENABLE_HEAT_AURA;
  public static final ForgeConfigSpec.BooleanValue ENABLE_SPORE_BLINDNESS;

  // Hazard Settings
  public static final ForgeConfigSpec.IntValue UPDRAFT_STRENGTH;
  public static final ForgeConfigSpec.IntValue HEAT_DAMAGE_INTERVAL;
  public static final ForgeConfigSpec.IntValue SPORE_EFFECT_DURATION;

  // Ambience Toggles
  public static final ForgeConfigSpec.BooleanValue ENABLE_BIOLUME_HUM;
  public static final ForgeConfigSpec.BooleanValue ENABLE_WIND_SOUNDS;
  public static final ForgeConfigSpec.BooleanValue ENABLE_PARTICLES;

  // Ambience Settings
  public static final ForgeConfigSpec.DoubleValue BIOLUME_HUM_VOLUME;
  public static final ForgeConfigSpec.DoubleValue WIND_SOUND_VOLUME;
  public static final ForgeConfigSpec.IntValue PARTICLE_DENSITY;

  static {
    BUILDER.push("Pandora Hazards & Ambience");

    // Hazard Toggles
    BUILDER.comment("Enable or disable specific hazards in Pandora");
    ENABLE_LEVITATION_UPDRAFTS =
        BUILDER
            .comment("Enable levitation updrafts in floating mountains")
            .define("enable_levitation_updrafts", true);
    ENABLE_HEAT_AURA =
        BUILDER.comment("Enable heat aura in volcanic wastelands").define("enable_heat_aura", true);
    ENABLE_SPORE_BLINDNESS =
        BUILDER
            .comment("Enable spore blindness in bioluminescent forests")
            .define("enable_spore_blindness", true);

    // Hazard Settings
    BUILDER.comment("Configure hazard settings");
    UPDRAFT_STRENGTH =
        BUILDER
            .comment("Strength of levitation effect (1-10)")
            .defineInRange("updraft_strength", 5, 1, 10);
    HEAT_DAMAGE_INTERVAL =
        BUILDER
            .comment("Ticks between heat damage (20 = 1 second)")
            .defineInRange("heat_damage_interval", 40, 10, 200);
    SPORE_EFFECT_DURATION =
        BUILDER
            .comment("Duration of spore effects in ticks (20 = 1 second)")
            .defineInRange("spore_effect_duration", 100, 20, 600);

    // Ambience Toggles
    BUILDER.comment("Enable or disable ambience features");
    ENABLE_BIOLUME_HUM =
        BUILDER
            .comment("Enable biolume hum sound in bioluminescent forests")
            .define("enable_biolume_hum", true);
    ENABLE_WIND_SOUNDS =
        BUILDER
            .comment("Enable wind sounds in sky islands and floating mountains")
            .define("enable_wind_sounds", true);
    ENABLE_PARTICLES = BUILDER.comment("Enable ambient particles").define("enable_particles", true);

    // Ambience Settings
    BUILDER.comment("Configure ambience settings");
    BIOLUME_HUM_VOLUME =
        BUILDER
            .comment("Volume of biolume hum (0.0 - 1.0)")
            .defineInRange("biolume_hum_volume", 0.7, 0.0, 1.0);
    WIND_SOUND_VOLUME =
        BUILDER
            .comment("Volume of wind sounds (0.0 - 1.0)")
            .defineInRange("wind_sound_volume", 0.5, 0.0, 1.0);
    PARTICLE_DENSITY =
        BUILDER
            .comment("Density of ambient particles (1-10)")
            .defineInRange("particle_density", 5, 1, 10);

    BUILDER.pop();
    SPEC = BUILDER.build();
  }
}
