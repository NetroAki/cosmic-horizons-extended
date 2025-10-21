package com.netroaki.chex.config.legacy;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class PandoraHazardsConfig {
  public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  public static final ForgeConfigSpec SPEC;

  public static void register() {
    ModLoadingContext.get()
        .registerConfig(ModConfig.Type.COMMON, SPEC, "chex-pandora-hazards.toml");
  }

  // General Settings
  public static final ForgeConfigSpec.IntValue HAZARD_CHECK_INTERVAL;

  // Levitation Updrafts
  public static final ForgeConfigSpec.BooleanValue ENABLE_LEVITATION_UPDRAFTS;
  public static final ForgeConfigSpec.DoubleValue LEVITATION_STRENGTH;
  public static final ForgeConfigSpec.IntValue LEVITATION_DURATION;
  public static final ForgeConfigSpec.IntValue LEVITATION_IMMUNE_TIER;

  // Heat Aura
  public static final ForgeConfigSpec.BooleanValue ENABLE_HEAT_AURA;
  public static final ForgeConfigSpec.IntValue HEAT_DAMAGE_INTERVAL;
  public static final ForgeConfigSpec.DoubleValue HEAT_DAMAGE_AMOUNT;
  public static final ForgeConfigSpec.IntValue HEAT_IMMUNE_TIER;

  // Spore Blindness
  public static final ForgeConfigSpec.BooleanValue ENABLE_SPORE_BLINDNESS;
  public static final ForgeConfigSpec.IntValue BLINDNESS_DURATION;
  public static final ForgeConfigSpec.IntValue BLINDNESS_IMMUNE_TIER;

  // Ambient Audio
  public static final ForgeConfigSpec.BooleanValue ENABLE_AMBIENT_AUDIO;
  public static final ForgeConfigSpec.IntValue AMBIENT_SOUND_INTERVAL;
  public static final ForgeConfigSpec.DoubleValue AMBIENT_SOUND_VOLUME;

  static {
    BUILDER.push("Pandora Hazards Configuration");

    // General Settings
    BUILDER.comment("General settings for Pandora hazards");
    HAZARD_CHECK_INTERVAL =
        BUILDER
            .comment("How often (in ticks) to check for hazards (20 ticks = 1 second)")
            .defineInRange("hazardCheckInterval", 10, 1, 100);

    // Levitation Updrafts
    BUILDER.push("Levitation Updrafts");
    ENABLE_LEVITATION_UPDRAFTS =
        BUILDER
            .comment("Enable levitation updrafts in Pandora's floating islands")
            .define("enableLevitationUpdrafts", true);
    LEVITATION_STRENGTH =
        BUILDER
            .comment("Strength of the levitation effect (0.0 to 1.0)")
            .defineInRange("levitationStrength", 0.1, 0.01, 1.0);
    LEVITATION_DURATION =
        BUILDER
            .comment("Duration of the levitation effect in ticks (20 ticks = 1 second)")
            .defineInRange("levitationDuration", 100, 20, 600);
    LEVITATION_IMMUNE_TIER =
        BUILDER
            .comment(
                "Suit tier required to be immune to levitation (0 = no immunity, higher = better"
                    + " protection)")
            .defineInRange("levitationImmuneTier", 3, 0, 10);
    BUILDER.pop();

    // Heat Aura
    BUILDER.push("Heat Aura");
    ENABLE_HEAT_AURA =
        BUILDER.comment("Enable heat damage in volcanic areas").define("enableHeatAura", true);
    HEAT_DAMAGE_INTERVAL =
        BUILDER
            .comment("Interval between heat damage ticks (20 ticks = 1 second)")
            .defineInRange("heatDamageInterval", 40, 10, 200);
    HEAT_DAMAGE_AMOUNT =
        BUILDER
            .comment("Amount of damage dealt by heat (in half-hearts)")
            .defineInRange("heatDamageAmount", 1.0, 0.1, 10.0);
    HEAT_IMMUNE_TIER =
        BUILDER
            .comment("Suit tier required to be immune to heat damage")
            .defineInRange("heatImmuneTier", 4, 0, 10);
    BUILDER.pop();

    // Spore Blindness
    BUILDER.push("Spore Blindness");
    ENABLE_SPORE_BLINDNESS =
        BUILDER
            .comment("Enable spore blindness in fungal biomes")
            .define("enableSporeBlindness", true);
    BLINDNESS_DURATION =
        BUILDER
            .comment("Base duration of spore blindness in ticks (20 ticks = 1 second)")
            .defineInRange("blindnessDuration", 200, 20, 1200);
    BLINDNESS_IMMUNE_TIER =
        BUILDER
            .comment("Suit tier required to be immune to spore blindness")
            .defineInRange("blindnessImmuneTier", 2, 0, 10);
    BUILDER.pop();

    // Ambient Audio
    BUILDER.push("Ambient Audio");
    ENABLE_AMBIENT_AUDIO =
        BUILDER.comment("Enable ambient sounds in Pandora").define("enableAmbientAudio", true);
    AMBIENT_SOUND_INTERVAL =
        BUILDER
            .comment("Average interval between ambient sounds in ticks (20 ticks = 1 second)")
            .defineInRange("ambientSoundInterval", 200, 20, 1200);
    AMBIENT_SOUND_VOLUME =
        BUILDER
            .comment("Volume of ambient sounds (0.0 to 1.0)")
            .defineInRange("ambientSoundVolume", 0.7, 0.1, 1.0);
    BUILDER.pop();

    BUILDER.pop();
    SPEC = BUILDER.build();
  }

  // Helper methods to get config values
  public static int getHazardCheckInterval() {
    return HAZARD_CHECK_INTERVAL.get();
  }

  public static boolean isLevitationEnabled() {
    return ENABLE_LEVITATION_UPDRAFTS.get();
  }

  public static float getLevitationStrength() {
    return LEVITATION_STRENGTH.get().floatValue();
  }

  public static int getLevitationDuration() {
    return LEVITATION_DURATION.get();
  }

  public static int getLevitationImmuneTier() {
    return LEVITATION_IMMUNE_TIER.get();
  }

  public static boolean isHeatAuraEnabled() {
    return ENABLE_HEAT_AURA.get();
  }

  public static int getHeatDamageInterval() {
    return HEAT_DAMAGE_INTERVAL.get();
  }

  public static float getHeatDamageAmount() {
    return HEAT_DAMAGE_AMOUNT.get().floatValue();
  }

  public static int getHeatImmuneTier() {
    return HEAT_IMMUNE_TIER.get();
  }

  public static boolean isSporeBlindnessEnabled() {
    return ENABLE_SPORE_BLINDNESS.get();
  }

  public static int getBlindnessDuration() {
    return BLINDNESS_DURATION.get();
  }

  public static int getBlindnessImmuneTier() {
    return BLINDNESS_IMMUNE_TIER.get();
  }

  public static boolean isAmbientAudioEnabled() {
    return ENABLE_AMBIENT_AUDIO.get();
  }

  public static int getAmbientSoundInterval() {
    return AMBIENT_SOUND_INTERVAL.get();
  }

  public static float getAmbientSoundVolume() {
    return AMBIENT_SOUND_VOLUME.get().floatValue();
  }
}
