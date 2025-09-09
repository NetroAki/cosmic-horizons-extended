package com.netroaki.chex.config;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.RocketTiers;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashMap;
import java.util.Map;

public final class CHEXConfig {
    private static ForgeConfigSpec COMMON_SPEC;
    private static final Map<RocketTiers, ForgeConfigSpec.ConfigValue<String>> FUEL_BY_TIER = new HashMap<>();
    private static ForgeConfigSpec.BooleanValue SUIT_BOUNCE_BACK;

    private CHEXConfig() {
    }

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("behavior");
        SUIT_BOUNCE_BACK = builder
                .comment("If true, under-tier suits are bounced back on dimension entry; otherwise apply debuffs.")
                .define("suitBounceBack", false);
        builder.pop();

        builder.push("fuelMapping");
        defineFuel(builder, RocketTiers.T1, "cosmic_horizons_extended:kerosene");
        defineFuel(builder, RocketTiers.T2, "cosmic_horizons_extended:cryo_lox");
        defineFuel(builder, RocketTiers.T3, "cosmic_horizons_extended:kerosene");
        defineFuel(builder, RocketTiers.T4, "cosmic_horizons_extended:cryo_lox");
        defineFuel(builder, RocketTiers.T5, "cosmic_horizons_extended:lh2_lox");
        defineFuel(builder, RocketTiers.T6, "cosmic_horizons_extended:lh2_lox");
        defineFuel(builder, RocketTiers.T7, "cosmic_horizons_extended:lh2_lox");
        defineFuel(builder, RocketTiers.T8, "cosmic_horizons_extended:lh2_lox");
        defineFuel(builder, RocketTiers.T9, "cosmic_horizons_extended:lh2_lox");
        defineFuel(builder, RocketTiers.T10, "cosmic_horizons_extended:lh2_lox");
        builder.pop();
        COMMON_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, CHEX.MOD_ID + "-common.toml");
    }

    private static void defineFuel(ForgeConfigSpec.Builder builder, RocketTiers tier, String defaultFluid) {
        FUEL_BY_TIER.put(tier, builder.comment("Fluid ID for tier " + tier.getTier())
                .define(tier.name(), defaultFluid));
    }

    public static String getFuelForTier(RocketTiers tier) {
        ForgeConfigSpec.ConfigValue<String> val = FUEL_BY_TIER.get(tier);
        return val == null ? "minecraft:empty" : val.get();
    }

    public static boolean suitBounceBack() {
        return SUIT_BOUNCE_BACK != null && SUIT_BOUNCE_BACK.get();
    }
}
