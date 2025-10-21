package com.netroaki.chex.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CrystalisHazardsConfig {
  public static final ForgeConfigSpec SERVER_SPEC;

  public static final ForgeConfigSpec.BooleanValue FROSTBITE_ENABLED;
  public static final ForgeConfigSpec.IntValue FROSTBITE_INTERVAL_TICKS;

  public static final ForgeConfigSpec.BooleanValue BLIZZARD_OVERLAY_ENABLED;
  public static final ForgeConfigSpec.BooleanValue FROST_OVERLAY_ENABLED;
  public static final ForgeConfigSpec.BooleanValue AURORA_ENABLED;

  public static final ForgeConfigSpec.BooleanValue GEYSER_ENCASE_ENABLED;
  public static final ForgeConfigSpec.DoubleValue GEYSER_ENCASE_RADIUS;

  static {
    ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

    b.push("crystalis_hazards");
    FROSTBITE_ENABLED =
        b.comment("Enable ambient frostbite in cold Crystalis biomes")
            .define("frostbiteEnabled", true);
    FROSTBITE_INTERVAL_TICKS =
        b.comment("Tick interval for frostbite checks (performance tuning)")
            .defineInRange("frostbiteIntervalTicks", 20, 5, 200);

    BLIZZARD_OVERLAY_ENABLED =
        b.comment("Enable blizzard screen overlay in snowstorms")
            .define("blizzardOverlayEnabled", true);
    FROST_OVERLAY_ENABLED =
        b.comment("Enable frost overlay when frostbite is active")
            .define("frostOverlayEnabled", true);
    AURORA_ENABLED =
        b.comment("Enable aurora sky rendering at night in cold biomes")
            .define("auroraEnabled", true);

    GEYSER_ENCASE_ENABLED =
        b.comment("Enable cryo geysers encasing nearby entities in frosted ice at eruption start")
            .define("geyserEncaseEnabled", true);
    GEYSER_ENCASE_RADIUS =
        b.comment("Radius around geyser to attempt frosted ice encasing")
            .defineInRange("geyserEncaseRadius", 2.0D, 0.5D, 6.0D);
    b.pop();

    SERVER_SPEC = b.build();
  }
}
