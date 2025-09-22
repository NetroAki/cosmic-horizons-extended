package com.netroaki.chex.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Configuration for the fuel registry system.
 * Handles settings related to fuel types, fallback behavior, and fuel properties.
 */
public class FuelConfig {
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC, "chex-fuels.toml");
    }

    public static class ServerConfig {
        public final ForgeConfigSpec.BooleanValue enableFallbackFluids;
        public final ForgeConfigSpec.BooleanValue logFuelRegistration;
        public final ForgeConfigSpec.BooleanValue acceptHigherTierFuel;
        public final ForgeConfigSpec.ConfigValue<String> preferredFluidSource;

        ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Fuel Registry Configuration")
                   .push("fuel_registry");

            enableFallbackFluids = builder
                .comment("Enable fallback fluids when GTCEu is not present")
                .define("enableFallbackFluids", true);

            logFuelRegistration = builder
                .comment("Log detailed information about fuel registration")
                .define("logFuelRegistration", false);

            acceptHigherTierFuel = builder
                .comment("Allow using higher tier fuels in lower tier engines (less efficient)")
                .define("acceptHigherTierFuel", true);

            preferredFluidSource = builder
                .comment("Preferred fluid source when multiple options are available (gtceu or chex)")
                .define("preferredFluidSource", "gtceu");

            builder.pop();
        }
    }
}
