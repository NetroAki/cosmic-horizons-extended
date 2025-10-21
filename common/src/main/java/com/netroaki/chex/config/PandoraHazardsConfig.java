package com.netroaki.chex.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber
public class PandoraHazardsConfig {
  public static final String CATEGORY_HAZARDS = "hazards";
  public static final String CATEGORY_AMBIENCE = "ambience";

  // Hazard Toggles
  public static ForgeConfigSpec.BooleanValue ENABLE_LEVITATION_UPDRAFTS;
  public static ForgeConfigSpec.BooleanValue ENABLE_HEAT_AURA;
  public static ForgeConfigSpec.BooleanValue ENABLE_SPORE_BLINDNESS;

  // Hazard Settings
  public static ForgeConfigSpec.IntValue UPDRAFT_STRENGTH;
  public static ForgeConfigSpec.IntValue HEAT_DAMAGE_INTERVAL;
  public static ForgeConfigSpec.IntValue SPORE_EFFECT_DURATION;

  // Ambience Toggles
  public static ForgeConfigSpec.BooleanValue ENABLE_BIOLUME_HUM;
  public static ForgeConfigSpec.BooleanValue ENABLE_WIND_SOUNDS;
  public static ForgeConfigSpec.BooleanValue ENABLE_PARTICLES;

  // Ambience Settings
  public static ForgeConfigSpec.DoubleValue BIOLUME_HUM_VOLUME;
  public static ForgeConfigSpec.DoubleValue WIND_SOUND_VOLUME;
  public static ForgeConfigSpec.IntValue PARTICLE_DENSITY;

  private static ForgeConfigSpec COMMON_CONFIG;

  static {
    ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    COMMON_BUILDER.comment("Pandora Hazards Configuration").push(CATEGORY_HAZARDS);

    // Hazard Toggles
    COMMON_BUILDER.comment("Enable or disable specific hazards in Pandora");
    ENABLE_LEVITATION_UPDRAFTS =
        COMMON_BUILDER
            .comment("Enable levitation updrafts in floating mountains")
            .define("enable_levitation_updrafts", true);
    ENABLE_HEAT_AURA =
        COMMON_BUILDER
            .comment("Enable heat aura in volcanic wastelands")
            .define("enable_heat_aura", true);
    ENABLE_SPORE_BLINDNESS =
        COMMON_BUILDER
            .comment("Enable spore blindness in bioluminescent forests")
            .define("enable_spore_blindness", true);

    // Hazard Settings
    COMMON_BUILDER.comment("Hazard Settings").push("settings");
    UPDRAFT_STRENGTH =
        COMMON_BUILDER
            .comment("Strength of levitation effect (1-10)")
            .defineInRange("updraft_strength", 5, 1, 10);
    HEAT_DAMAGE_INTERVAL =
        COMMON_BUILDER
            .comment("Ticks between heat damage (20 = 1 second)")
            .defineInRange("heat_damage_interval", 40, 10, 200);
    SPORE_EFFECT_DURATION =
        COMMON_BUILDER
            .comment("Duration of spore effects in ticks (20 = 1 second)")
            .defineInRange("spore_effect_duration", 100, 20, 600);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.pop();

    // Ambience Settings
    COMMON_BUILDER.comment("Pandora Ambience Configuration").push(CATEGORY_AMBIENCE);

    // Ambience Toggles
    COMMON_BUILDER.comment("Enable or disable ambience features");
    ENABLE_BIOLUME_HUM =
        COMMON_BUILDER
            .comment("Enable biolume hum sound in bioluminescent forests")
            .define("enable_biolume_hum", true);
    ENABLE_WIND_SOUNDS =
        COMMON_BUILDER
            .comment("Enable wind sounds in sky islands and floating mountains")
            .define("enable_wind_sounds", true);
    ENABLE_PARTICLES =
        COMMON_BUILDER.comment("Enable ambient particles").define("enable_particles", true);

    // Ambience Settings
    COMMON_BUILDER.comment("Ambience Settings").push("settings");
    BIOLUME_HUM_VOLUME =
        COMMON_BUILDER
            .comment("Volume of biolume hum (0.0 - 1.0)")
            .defineInRange("biolume_hum_volume", 0.7, 0.0, 1.0);
    WIND_SOUND_VOLUME =
        COMMON_BUILDER
            .comment("Volume of wind sounds (0.0 - 1.0)")
            .defineInRange("wind_sound_volume", 0.5, 0.0, 1.0);
    PARTICLE_DENSITY =
        COMMON_BUILDER
            .comment("Density of ambient particles (1-10)")
            .defineInRange("particle_density", 5, 1, 10);
    COMMON_BUILDER.pop();
    COMMON_BUILDER.pop();

    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static void register() {
    ModLoadingContext.get()
        .registerConfig(
            ModConfig.Type.COMMON, COMMON_CONFIG, "cosmic_horizons_extended-hazards.toml");
  }

  @SubscribeEvent
  public static void onLoad(final ModConfigEvent.Loading configEvent) {
    // Config loaded - update any values that need updating
  }

  @SubscribeEvent
  public static void onReload(final ModConfigEvent.Reloading configEvent) {
    // Config reloaded - update any values that need updating
  }
}
