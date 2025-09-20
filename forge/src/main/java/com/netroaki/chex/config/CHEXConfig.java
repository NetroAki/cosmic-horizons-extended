package com.netroaki.chex.config;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.NoduleTiers;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashMap;
import java.util.Map;

public final class CHEXConfig {
    private static ForgeConfigSpec COMMON_SPEC;
    private static final Map<NoduleTiers, ForgeConfigSpec.ConfigValue<String>> FUEL_BY_TIER = new HashMap<>();
    private static ForgeConfigSpec.BooleanValue SUIT_BOUNCE_BACK;
    private static ForgeConfigSpec.BooleanValue ENABLE_TERRABLENDER_OVERLAY;
    private static ForgeConfigSpec.IntValue RADIATION_TICK_INTERVAL;
    private static ForgeConfigSpec.DoubleValue RADIATION_DAMAGE_BASE;
    private static ForgeConfigSpec.DoubleValue RADIATION_DAMAGE_PER_AMPLIFIER;
    private static ForgeConfigSpec.BooleanValue ACCEPT_HIGHER_TIER_FUEL;
    private static final Map<NoduleTiers, ForgeConfigSpec.IntValue> FUEL_VOLUME_BY_TIER = new HashMap<>();

    private CHEXConfig() {
    }

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("behavior");
        SUIT_BOUNCE_BACK = builder
                .comment("If true, under-tier suits are bounced back on dimension entry; otherwise apply debuffs.")
                .define("suitBounceBack", false);
        ENABLE_TERRABLENDER_OVERLAY = builder
                .comment("Enable CHEX TerraBlender region overlay (preserves original CH biomes by default).")
                .define("enableTerraBlenderOverlay", true);
        ACCEPT_HIGHER_TIER_FUEL = builder
                .comment("Allow higher-tier fuels to satisfy lower-tier launches.")
                .define("acceptHigherTierFuel", true);
        RADIATION_TICK_INTERVAL = builder
                .comment("Tick interval (ticks) for radiation periodic damage.")
                .defineInRange("radiationTickInterval", 40, 1, 1200);
        RADIATION_DAMAGE_BASE = builder
                .comment("Radiation base damage per tick (health points).")
                .defineInRange("radiationDamageBase", 1.0D, 0.0D, 20.0D);
        RADIATION_DAMAGE_PER_AMPLIFIER = builder
                .comment("Additional radiation damage per amplifier level.")
                .defineInRange("radiationDamagePerAmplifier", 0.5D, 0.0D, 10.0D);
        builder.pop();

        builder.push("fuelMapping");
        defineFuel(builder, NoduleTiers.T1, "cosmic_horizons_extended:kerosene");
        defineFuel(builder, NoduleTiers.T2, "cosmic_horizons_extended:rp1");
        defineFuel(builder, NoduleTiers.T3, "cosmic_horizons_extended:lox");
        defineFuel(builder, NoduleTiers.T4, "cosmic_horizons_extended:lh2");
        defineFuel(builder, NoduleTiers.T5, "cosmic_horizons_extended:lh2");
        defineFuel(builder, NoduleTiers.T6, "cosmic_horizons_extended:lh2");
        defineFuel(builder, NoduleTiers.T7, "cosmic_horizons_extended:lh2");
        defineFuel(builder, NoduleTiers.T8, "cosmic_horizons_extended:lh2");
        defineFuel(builder, NoduleTiers.T9, "cosmic_horizons_extended:lh2");
        defineFuel(builder, NoduleTiers.T10, "cosmic_horizons_extended:lh2");
        builder.pop();

        builder.push("fuelVolumes");
        defineFuelVolume(builder, NoduleTiers.T1, 1000);
        defineFuelVolume(builder, NoduleTiers.T2, 1500);
        defineFuelVolume(builder, NoduleTiers.T3, 2000);
        defineFuelVolume(builder, NoduleTiers.T4, 2500);
        defineFuelVolume(builder, NoduleTiers.T5, 3000);
        defineFuelVolume(builder, NoduleTiers.T6, 3500);
        defineFuelVolume(builder, NoduleTiers.T7, 4000);
        defineFuelVolume(builder, NoduleTiers.T8, 4500);
        defineFuelVolume(builder, NoduleTiers.T9, 5000);
        defineFuelVolume(builder, NoduleTiers.T10, 6000);
        builder.pop();
        COMMON_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, CHEX.MOD_ID + "-common.toml");
    }

    private static void defineFuel(ForgeConfigSpec.Builder builder, NoduleTiers tier, String defaultFluid) {
        FUEL_BY_TIER.put(tier, builder.comment("Fluid ID for tier " + tier.getTier())
                .define(tier.name(), defaultFluid));
    }

    public static String getFuelForTier(NoduleTiers tier) {
        ForgeConfigSpec.ConfigValue<String> val = FUEL_BY_TIER.get(tier);
        return val == null ? "minecraft:empty" : val.get();
    }

    public static boolean suitBounceBack() {
        return SUIT_BOUNCE_BACK != null && SUIT_BOUNCE_BACK.get();
    }

    public static boolean enableTerraBlenderOverlay() {
        return ENABLE_TERRABLENDER_OVERLAY != null && ENABLE_TERRABLENDER_OVERLAY.get();
    }

    public static boolean acceptHigherTierFuel() {
        return ACCEPT_HIGHER_TIER_FUEL != null && ACCEPT_HIGHER_TIER_FUEL.get();
    }

    private static void defineFuelVolume(ForgeConfigSpec.Builder builder, NoduleTiers tier, int defaultMb) {
        FUEL_VOLUME_BY_TIER.put(tier, builder.comment("Fuel volume (mB) for tier " + tier.getTier())
                .defineInRange("T" + tier.getTier(), defaultMb, 0, 64000));
    }

    public static int getFuelVolumeForTier(NoduleTiers tier) {
        ForgeConfigSpec.IntValue val = FUEL_VOLUME_BY_TIER.get(tier);
        return val == null ? 0 : val.get();
    }

    // Hypoxia config removed; handled by external oxygen mod

    public static int radiationTickInterval() {
        return RADIATION_TICK_INTERVAL != null ? RADIATION_TICK_INTERVAL.get() : 40;
    }

    public static double radiationDamageBase() {
        return RADIATION_DAMAGE_BASE != null ? RADIATION_DAMAGE_BASE.get() : 1.0D;
    }

    public static double radiationDamagePerAmplifier() {
        return RADIATION_DAMAGE_PER_AMPLIFIER != null ? RADIATION_DAMAGE_PER_AMPLIFIER.get() : 0.5D;
    }
}
