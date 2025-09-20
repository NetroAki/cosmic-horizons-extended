package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Fuel registry system for nodule tiers
 */
public class FuelRegistry {

        private static final Map<Integer, FuelRequirement> TIER_FUEL_REQUIREMENTS = new HashMap<>();
        private static final Map<Integer, Integer> TIER_FUEL_VOLUMES = new HashMap<>();

        public static void initialize() {
                CHEX.LOGGER.info("Initializing fuel registry...");

                // T1: Basic Nodule - Kerosene
                registerFuel(1, "cosmic_horizons_extended:kerosene", 1000);

                // T2: Advanced Nodule - RP-1
                registerFuel(2, "cosmic_horizons_extended:rp1", 1500);

                // T3: Improved Nodule - LOX
                registerFuel(3, "cosmic_horizons_extended:lox", 2000);

                // T4: Enhanced Nodule - LH2
                registerFuel(4, "cosmic_horizons_extended:lh2", 2500);

                CHEX.LOGGER.info("Fuel registry initialized with {} fuel types", TIER_FUEL_REQUIREMENTS.size());
        }

        public static void registerFuel(int tier, String fluidId, int volumeMb) {
                ResourceLocation fluidResource = ResourceLocation.parse(fluidId);
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidResource);

                if (fluid != null) {
                        TIER_FUEL_REQUIREMENTS.put(tier, new FuelRequirement(fluid, fluidResource));
                        TIER_FUEL_VOLUMES.put(tier, volumeMb);
                        CHEX.LOGGER.debug("Registered fuel for T{}: {} ({}mB)", tier, fluidId, volumeMb);
                } else {
                        CHEX.LOGGER.warn("Failed to register fuel for T{}: {} not found", tier, fluidId);
                }
        }

        public static Optional<FuelRequirement> getFuelRequirement(int tier) {
                return Optional.ofNullable(TIER_FUEL_REQUIREMENTS.get(tier));
        }

        public static int getFuelVolume(int tier) {
                return TIER_FUEL_VOLUMES.getOrDefault(tier, 0);
        }

        public static boolean isFuelSufficientForTier(int tier, ResourceLocation fluidId, int availableMb) {
                var reqOpt = getFuelRequirement(tier);
                if (reqOpt.isEmpty())
                        return true;
                var req = reqOpt.get();
                int required = getFuelVolume(tier);
                if (availableMb < required)
                        return false;
                // Exact match or acceptHigherTierFuel allows any registered higher tier fluid
                if (req.getFluidId().equals(fluidId))
                        return true;
                if (com.netroaki.chex.config.CHEXConfig.acceptHigherTierFuel()) {
                        // simple heuristic: allow any of our known fluids
                        String ns = fluidId.getNamespace();
                        return ns.equals(com.netroaki.chex.CHEX.MOD_ID);
                }
                return false;
        }

        public static boolean isValidFuel(int tier, Fluid fluid) {
                return getFuelRequirement(tier)
                                .map(req -> req.getFluid() == fluid)
                                .orElse(false);
        }

        public static class FuelRequirement {
                private final Fluid fluid;
                private final ResourceLocation fluidId;

                public FuelRequirement(Fluid fluid, ResourceLocation fluidId) {
                        this.fluid = fluid;
                        this.fluidId = fluidId;
                }

                public Fluid getFluid() {
                        return fluid;
                }

                public ResourceLocation getFluidId() {
                        return fluidId;
                }
        }
}