package com.netroaki.chex.gt;

import com.netroaki.chex.CHEX;
import net.minecraftforge.fml.ModList;

public final class GregTechBridge {
    private static final boolean GTCEU_AVAILABLE = ModList.get().isLoaded("gtceu");

    private GregTechBridge() {
    }

    public static boolean isLoaded() {
        return GTCEU_AVAILABLE;
    }

    /**
     * Checks if GTCEu is available and logs the status
     */
    public static boolean isAvailable() {
        boolean available = ModList.get().isLoaded("gtceu");
        if (available) {
            CHEX.LOGGER.info("GTCEu detected and available for integration");
        } else {
            CHEX.LOGGER.info("GTCEu not detected - using fallback systems");
        }
        return available;
    }

    /**
     * Maps GTCEu power tiers to rocket tiers for mineral generation
     */
    public static String getGTTierForRocketTier(int rocketTier) {
        return switch (rocketTier) {
            case 1, 2 -> "LV"; // Low Voltage
            case 3, 4 -> "MV"; // Medium Voltage
            case 5, 6 -> "HV"; // High Voltage
            case 7, 8 -> "EV"; // Extreme Voltage
            case 9, 10 -> "IV"; // Insane Voltage
            default -> "LV";
        };
    }

    /**
     * Gets the appropriate vein size for a given rocket tier
     */
    public static String getVeinSizeForRocketTier(int rocketTier) {
        return switch (rocketTier) {
            case 1, 2 -> "small";
            case 3, 4 -> "medium";
            case 5, 6 -> "large";
            case 7, 8 -> "massive";
            case 9, 10 -> "enormous";
            default -> "small";
        };
    }

    /**
     * Gets the ore count multiplier for a given rocket tier
     */
    public static int getOreCountForRocketTier(int rocketTier) {
        return switch (rocketTier) {
            case 1, 2 -> 2;
            case 3, 4 -> 4;
            case 5, 6 -> 6;
            case 7, 8 -> 8;
            case 9, 10 -> 12;
            default -> 2;
        };
    }

    /**
     * Gets the Y-level range for a given rocket tier
     */
    public static int[] getYRangeForRocketTier(int rocketTier) {
        return switch (rocketTier) {
            case 1, 2 -> new int[] { -32, 40 }; // Surface to shallow underground
            case 3, 4 -> new int[] { -48, 24 }; // Deeper underground
            case 5, 6 -> new int[] { -64, 8 }; // Deep underground
            case 7, 8 -> new int[] { -64, -16 }; // Very deep
            case 9, 10 -> new int[] { -64, -32 }; // Extreme depths
            default -> new int[] { -32, 40 };
        };
    }

    /**
     * Logs GTCEu integration status
     */
    public static void logIntegrationStatus() {
        if (isLoaded()) {
            CHEX.LOGGER.info("GTCEu integration: ENABLED - Using GTCEu ores and mineral generation");
        } else {
            CHEX.LOGGER.info("GTCEu integration: DISABLED - Using fallback ores for mineral generation");
        }
    }

    /**
     * Get GTCEu mineral generation data for a specific planet
     */
    public static java.util.Map<String, Object> getMineralDataForPlanet(String planetId) {
        if (!isAvailable()) {
            return getFallbackMineralData(planetId);
        }

        // In a full implementation, this would query GTCEu's mineral registry
        // For now, return enhanced fallback data
        return getEnhancedFallbackMineralData(planetId);
    }

    /**
     * Check if a specific mineral is available on a planet
     */
    public static boolean isMineralAvailableOnPlanet(String planetId, String mineral) {
        if (!isAvailable()) {
            return getFallbackMineralData(planetId).containsKey(mineral);
        }

        // In a full implementation, this would check GTCEu's mineral registry
        return getEnhancedFallbackMineralData(planetId).containsKey(mineral);
    }

    /**
     * Get the GTCEu tier requirement for a mineral
     */
    public static int getMineralGTTier(String mineral) {
        if (!isAvailable()) {
            return getFallbackMineralTier(mineral);
        }

        // In a full implementation, this would query GTCEu's mineral registry
        return getFallbackMineralTier(mineral);
    }

    /**
     * Enhanced fallback mineral data with more realistic distributions
     */
    private static java.util.Map<String, Object> getEnhancedFallbackMineralData(String planetId) {
        java.util.Map<String, Object> data = new java.util.HashMap<>();

        // Basic minerals available on most planets
        data.put("iron", java.util.Map.of("tier", 1, "abundance", 0.8, "depth", new int[] { -32, 40 }));
        data.put("copper", java.util.Map.of("tier", 1, "abundance", 0.6, "depth", new int[] { -32, 40 }));
        data.put("tin", java.util.Map.of("tier", 2, "abundance", 0.4, "depth", new int[] { -48, 16 }));

        // Planet-specific minerals
        switch (planetId) {
            case "cosmos:earth_moon" -> {
                data.put("silver", java.util.Map.of("tier", 2, "abundance", 0.3, "depth", new int[] { -48, 16 }));
                data.put("lead", java.util.Map.of("tier", 2, "abundance", 0.2, "depth", new int[] { -48, 16 }));
            }
            case "cosmos:mercury" -> {
                data.put("gold", java.util.Map.of("tier", 3, "abundance", 0.5, "depth", new int[] { -64, 0 }));
                data.put("platinum", java.util.Map.of("tier", 4, "abundance", 0.1, "depth", new int[] { -64, -16 }));
            }
            case "cosmos:mars" -> {
                data.put("nickel", java.util.Map.of("tier", 3, "abundance", 0.4, "depth", new int[] { -48, 8 }));
                data.put("cobalt", java.util.Map.of("tier", 3, "abundance", 0.3, "depth", new int[] { -48, 8 }));
            }
            case "cosmic_horizons_extended:alpha_centauri_a" -> {
                data.put("titanium", java.util.Map.of("tier", 5, "abundance", 0.6, "depth", new int[] { -64, -16 }));
                data.put("tungsten", java.util.Map.of("tier", 6, "abundance", 0.3, "depth", new int[] { -64, -32 }));
            }
            case "cosmic_horizons_extended:kepler_452b" -> {
                data.put("uranium", java.util.Map.of("tier", 7, "abundance", 0.4, "depth", new int[] { -64, -32 }));
                data.put("thorium", java.util.Map.of("tier", 7, "abundance", 0.3, "depth", new int[] { -64, -32 }));
            }
            // PANDORA SYSTEM MINERALS
            case "cosmic_horizons_extended:pandora_forest" -> {
                data.put("unobtanium", java.util.Map.of("tier", 6, "abundance", 0.2, "depth", new int[] { -32, 16 }));
                data.put("bioluminescent_crystal",
                        java.util.Map.of("tier", 6, "abundance", 0.6, "depth", new int[] { -16, 32 }));
                data.put("exotic_wood", java.util.Map.of("tier", 6, "abundance", 0.8, "depth", new int[] { 0, 64 }));
            }
            case "cosmic_horizons_extended:pandora_mountains" -> {
                data.put("unobtanium", java.util.Map.of("tier", 7, "abundance", 0.4, "depth", new int[] { -48, 48 }));
                data.put("magnetic_ore", java.util.Map.of("tier", 7, "abundance", 0.5, "depth", new int[] { -32, 32 }));
                data.put("crystal_formation",
                        java.util.Map.of("tier", 7, "abundance", 0.7, "depth", new int[] { -16, 48 }));
            }
            case "cosmic_horizons_extended:pandora_ocean" -> {
                data.put("deep_sea_crystal",
                        java.util.Map.of("tier", 8, "abundance", 0.3, "depth", new int[] { -64, -16 }));
                data.put("bioluminescent_essence",
                        java.util.Map.of("tier", 8, "abundance", 0.6, "depth", new int[] { -48, 0 }));
                data.put("oceanic_metal",
                        java.util.Map.of("tier", 8, "abundance", 0.4, "depth", new int[] { -64, -32 }));
            }
            case "cosmic_horizons_extended:pandora_volcanic" -> {
                data.put("volcanic_crystal",
                        java.util.Map.of("tier", 9, "abundance", 0.5, "depth", new int[] { -32, 16 }));
                data.put("molten_metal", java.util.Map.of("tier", 9, "abundance", 0.3, "depth", new int[] { -48, 0 }));
                data.put("thermal_energy_core",
                        java.util.Map.of("tier", 9, "abundance", 0.1, "depth", new int[] { -64, -16 }));
            }
            case "cosmic_horizons_extended:pandora_skylands" -> {
                data.put("sky_crystal", java.util.Map.of("tier", 10, "abundance", 0.4, "depth", new int[] { 32, 128 }));
                data.put("levitation_essence",
                        java.util.Map.of("tier", 10, "abundance", 0.2, "depth", new int[] { 48, 96 }));
                data.put("cloud_metal", java.util.Map.of("tier", 10, "abundance", 0.3, "depth", new int[] { 16, 80 }));
            }
            // ARRAKIS SYSTEM MINERALS
            case "cosmic_horizons_extended:arrakis_desert" -> {
                data.put("spice_melange",
                        java.util.Map.of("tier", 6, "abundance", 0.8, "depth", new int[] { -16, 16 }));
                data.put("desert_glass", java.util.Map.of("tier", 6, "abundance", 0.6, "depth", new int[] { -8, 8 }));
                data.put("sandworm_essence",
                        java.util.Map.of("tier", 6, "abundance", 0.1, "depth", new int[] { -32, 0 }));
            }
            case "cosmic_horizons_extended:arrakis_spice_mines" -> {
                data.put("pure_spice", java.util.Map.of("tier", 7, "abundance", 0.9, "depth", new int[] { -48, -16 }));
                data.put("crystallized_spice",
                        java.util.Map.of("tier", 7, "abundance", 0.4, "depth", new int[] { -64, -32 }));
                data.put("spice_essence",
                        java.util.Map.of("tier", 7, "abundance", 0.6, "depth", new int[] { -40, -8 }));
            }
            case "cosmic_horizons_extended:arrakis_polar_caps" -> {
                data.put("ice_spice", java.util.Map.of("tier", 8, "abundance", 0.5, "depth", new int[] { -32, 0 }));
                data.put("polar_crystal",
                        java.util.Map.of("tier", 8, "abundance", 0.7, "depth", new int[] { -48, -16 }));
                data.put("frozen_essence",
                        java.util.Map.of("tier", 8, "abundance", 0.3, "depth", new int[] { -64, -32 }));
            }
            case "cosmic_horizons_extended:arrakis_sietch" -> {
                data.put("refined_spice", java.util.Map.of("tier", 9, "abundance", 0.8, "depth", new int[] { -32, 0 }));
                data.put("sietch_technology",
                        java.util.Map.of("tier", 9, "abundance", 0.2, "depth", new int[] { -48, -16 }));
                data.put("fremen_artifacts",
                        java.util.Map.of("tier", 9, "abundance", 0.1, "depth", new int[] { -64, -32 }));
            }
            case "cosmic_horizons_extended:arrakis_storm_world" -> {
                data.put("storm_spice", java.util.Map.of("tier", 10, "abundance", 0.6, "depth", new int[] { -16, 16 }));
                data.put("lightning_crystal",
                        java.util.Map.of("tier", 10, "abundance", 0.3, "depth", new int[] { -32, 32 }));
                data.put("storm_essence",
                        java.util.Map.of("tier", 10, "abundance", 0.4, "depth", new int[] { -24, 24 }));
            }
            // EXOTICA SYSTEM MINERALS
            case "cosmic_horizons_extended:ringworld_prime" -> {
                data.put("ring_particles",
                        java.util.Map.of("tier", 6, "abundance", 0.8, "depth", new int[] { 0, 128 }));
                data.put("ice_crystals", java.util.Map.of("tier", 6, "abundance", 0.6, "depth", new int[] { -16, 32 }));
                data.put("metallic_dust", java.util.Map.of("tier", 6, "abundance", 0.7, "depth", new int[] { -8, 16 }));
                data.put("solar_essence", java.util.Map.of("tier", 6, "abundance", 0.4, "depth", new int[] { 32, 96 }));
                data.put("low_gravity_ore",
                        java.util.Map.of("tier", 6, "abundance", 0.5, "depth", new int[] { 16, 64 }));
            }
            case "cosmic_horizons_extended:aqua_mundus" -> {
                data.put("ice_core", java.util.Map.of("tier", 7, "abundance", 0.9, "depth", new int[] { -64, -32 }));
                data.put("underwater_crystals",
                        java.util.Map.of("tier", 7, "abundance", 0.6, "depth", new int[] { -48, 0 }));
                data.put("aquatic_metals",
                        java.util.Map.of("tier", 7, "abundance", 0.5, "depth", new int[] { -32, 16 }));
                data.put("frozen_gases", java.util.Map.of("tier", 7, "abundance", 0.7, "depth", new int[] { -40, -8 }));
                data.put("deep_sea_essence",
                        java.util.Map.of("tier", 7, "abundance", 0.3, "depth", new int[] { -56, -24 }));
            }
            case "cosmic_horizons_extended:inferno_prime" -> {
                data.put("molten_ore", java.util.Map.of("tier", 8, "abundance", 0.8, "depth", new int[] { -16, 16 }));
                data.put("obsidian_crystals",
                        java.util.Map.of("tier", 8, "abundance", 0.6, "depth", new int[] { -24, 8 }));
                data.put("volcanic_glass", java.util.Map.of("tier", 8, "abundance", 0.7, "depth", new int[] { -8, 8 }));
                data.put("lava_essence", java.util.Map.of("tier", 8, "abundance", 0.4, "depth", new int[] { -32, 0 }));
                data.put("heat_resistant_metals",
                        java.util.Map.of("tier", 8, "abundance", 0.5, "depth", new int[] { -40, -16 }));
            }
            case "cosmic_horizons_extended:crystalis" -> {
                data.put("diamond_core",
                        java.util.Map.of("tier", 9, "abundance", 0.2, "depth", new int[] { -64, -48 }));
                data.put("ice_crystals", java.util.Map.of("tier", 9, "abundance", 0.8, "depth", new int[] { -32, 0 }));
                data.put("frozen_gases",
                        java.util.Map.of("tier", 9, "abundance", 0.6, "depth", new int[] { -48, -16 }));
                data.put("diamond_rain", java.util.Map.of("tier", 9, "abundance", 0.1, "depth", new int[] { 0, 32 }));
                data.put("pressure_crystals",
                        java.util.Map.of("tier", 9, "abundance", 0.4, "depth", new int[] { -56, -32 }));
            }
            case "cosmic_horizons_extended:stormworld" -> {
                data.put("atmospheric_gases",
                        java.util.Map.of("tier", 10, "abundance", 0.9, "depth", new int[] { 0, 128 }));
                data.put("lightning_crystals",
                        java.util.Map.of("tier", 10, "abundance", 0.3, "depth", new int[] { 32, 96 }));
                data.put("storm_essence",
                        java.util.Map.of("tier", 10, "abundance", 0.5, "depth", new int[] { 16, 80 }));
                data.put("wind_energy", java.util.Map.of("tier", 10, "abundance", 0.7, "depth", new int[] { 24, 88 }));
                data.put("gas_giant_core",
                        java.util.Map.of("tier", 10, "abundance", 0.1, "depth", new int[] { -64, -32 }));
            }
        }

        return data;
    }

    /**
     * Get fallback mineral tier
     */
    private static int getFallbackMineralTier(String mineral) {
        return switch (mineral) {
            case "iron", "copper" -> 1;
            case "tin", "silver", "lead" -> 2;
            case "gold", "nickel", "cobalt" -> 3;
            case "platinum" -> 4;
            case "titanium" -> 5;
            case "tungsten" -> 6;
            case "uranium", "thorium" -> 7;
            default -> 1;
        };
    }

    /**
     * Get basic fallback mineral data
     */
    private static java.util.Map<String, Object> getFallbackMineralData(String planetId) {
        java.util.Map<String, Object> data = new java.util.HashMap<>();

        // Basic minerals available on all planets
        data.put("iron", java.util.Map.of("tier", 1, "abundance", 0.8, "depth", new int[] { -32, 40 }));
        data.put("copper", java.util.Map.of("tier", 1, "abundance", 0.6, "depth", new int[] { -32, 40 }));

        return data;
    }
}
