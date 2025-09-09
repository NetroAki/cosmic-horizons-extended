package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.integration.CosmicHorizonsIntegration;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Registry for all planets in the CHEX mod system.
 */
public class PlanetRegistry {

        private static final Map<ResourceLocation, PlanetDef> PLANETS = new HashMap<>();
        private static final Map<ResourceLocation, PlanetDef> CHEX_PLANETS = new HashMap<>();

        public static void initialize() {
                CHEX.LOGGER.info("Initializing planet registry...");

                // Initialize Cosmic Horizons integration
                CosmicHorizonsIntegration.initialize();

                registerExistingCHPlanets();
                registerCHEXPlanets();

                CHEX.LOGGER.info("Planet registry initialized with {} total planets ({} CHEX planets)",
                                PLANETS.size(), CHEX_PLANETS.size());
                CHEX.LOGGER.info("Integration status: {}", CosmicHorizonsIntegration.getIntegrationStatus());
        }

        private static void registerExistingCHPlanets() {
                // Earth Moon
                registerPlanet(ResourceLocation.fromNamespaceAndPath("cosmos", "earth_moon"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath("cosmos", "earth_moon"),
                                                "Earth Moon",
                                                "The natural satellite of Earth",
                                                RocketTiers.T1,
                                                "chex:suits/suit1",
                                                "minecraft:lava",
                                                1, false, true,
                                                Set.of("iron", "silicon", "aluminum"),
                                                "lunar", false));

                // Mercury
                registerPlanet(ResourceLocation.fromNamespaceAndPath("cosmos", "mercury"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath("cosmos", "mercury"),
                                                "Mercury",
                                                "The closest planet to the Sun",
                                                RocketTiers.T2,
                                                "chex:suits/suit1",
                                                "minecraft:lava",
                                                2, false, true,
                                                Set.of("iron", "nickel", "sulfur"),
                                                "barren", false));

                // Mars
                registerPlanet(ResourceLocation.fromNamespaceAndPath("cosmos", "mars"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath("cosmos", "mars"),
                                                "Mars",
                                                "The red planet",
                                                RocketTiers.T2,
                                                "chex:suits/suit1",
                                                "minecraft:lava",
                                                2, true, false,
                                                Set.of("iron", "silicon", "aluminum", "water"),
                                                "desert", false));
        }

        private static void registerCHEXPlanets() {
                // Alpha Centauri A
                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "alpha_centauri_a"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "alpha_centauri_a"),
                                                "Alpha Centauri A",
                                                "The primary star of the Alpha Centauri system",
                                                RocketTiers.T8,
                                                "chex:suits/suit4",
                                                "chex:lh2",
                                                5, true, false,
                                                Set.of("hydrogen", "helium", "lithium"),
                                                "stellar", false));

                // Kepler-452b
                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "kepler_452b"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "kepler_452b"),
                                                "Kepler-452b",
                                                "Earth's older cousin",
                                                RocketTiers.T9,
                                                "chex:suits/suit5",
                                                "chex:lh2",
                                                3, true, true,
                                                Set.of("iron", "silicon", "aluminum", "water", "carbon"),
                                                "temperate", false));

                // PANDORA SYSTEM (Avatar-inspired) - Goldilocks Zone
                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_forest"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_forest"),
                                                "Pandora Forest World",
                                                "A lush bioluminescent forest world with diverse ecosystems",
                                                RocketTiers.T6,
                                                "chex:suits/suit3",
                                                "chex:rp1",
                                                4, true, true,
                                                Set.of("unobtanium", "bioluminescent_crystal", "exotic_wood",
                                                                "crystal_formations", "aquatic_crystals"),
                                                "temperate_zone", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_mountains"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_mountains"),
                                                "Pandora Floating Mountains",
                                                "Mysterious floating mountain ranges with high-altitude ecosystems",
                                                RocketTiers.T7,
                                                "chex:suits/suit4",
                                                "chex:rp1",
                                                3, true, false,
                                                Set.of("unobtanium", "magnetic_ore", "crystal_formation",
                                                                "wind_crystals", "cloud_essence"),
                                                "high_altitude_zone", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_ocean"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_ocean"),
                                                "Pandora Ocean Depths",
                                                "Deep bioluminescent ocean world with layered ecosystems",
                                                RocketTiers.T8,
                                                "chex:suits/suit4",
                                                "chex:lox",
                                                2, true, true,
                                                Set.of("deep_sea_crystal", "bioluminescent_essence", "oceanic_metal",
                                                                "thermal_vent_crystals", "kelp_essence"),
                                                "aquatic_zone", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_volcanic"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_volcanic"),
                                                "Pandora Volcanic Wasteland",
                                                "Extreme volcanic terrain with diverse extreme ecosystems",
                                                RocketTiers.T9,
                                                "chex:suits/suit5",
                                                "chex:lh2",
                                                1, false, false,
                                                Set.of("volcanic_crystal", "molten_metal", "thermal_energy_core",
                                                                "ash_crystals", "lava_essence"),
                                                "extreme_zone", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_skylands"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "pandora_skylands"),
                                                "Pandora Sky Islands",
                                                "Floating islands in the upper atmosphere with atmospheric ecosystems",
                                                RocketTiers.T10,
                                                "chex:suits/suit5",
                                                "chex:lh2",
                                                0, false, false,
                                                Set.of("sky_crystal", "levitation_essence", "cloud_metal",
                                                                "atmospheric_crystals", "wind_essence"),
                                                "atmospheric_zone", false));

                // ARRAKIS SYSTEM (Dune-inspired) - Goldilocks Zone
                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis_desert"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis_desert"),
                                                "Arrakis Desert World",
                                                "The desert planet where spice flows with diverse desert ecosystems",
                                                RocketTiers.T6,
                                                "chex:suits/suit3",
                                                "chex:kerosene",
                                                4, true, true,
                                                Set.of("spice_melange", "desert_glass", "sandworm_essence",
                                                                "crystalline_salt", "water_crystals"),
                                                "desert_zone", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis_spice_mines"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID,
                                                                "arrakis_spice_mines"),
                                                "Arrakis Spice Mines",
                                                "Underground spice extraction facilities with industrial ecosystems",
                                                RocketTiers.T7,
                                                "chex:suits/suit4",
                                                "chex:rp1",
                                                3, true, false,
                                                Set.of("pure_spice", "crystallized_spice", "spice_essence",
                                                                "mining_equipment", "industrial_crystals"),
                                                "underground_zone", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis_polar_caps"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID,
                                                                "arrakis_polar_caps"),
                                                "Arrakis Polar Ice Caps",
                                                "Frozen polar regions with diverse ice ecosystems",
                                                RocketTiers.T8,
                                                "chex:suits/suit4",
                                                "chex:lox",
                                                2, true, true,
                                                Set.of("ice_spice", "polar_crystal", "frozen_essence", "ice_formations",
                                                                "permafrost_crystals"),
                                                "polar_zone", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis_sietch"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis_sietch"),
                                                "Arrakis Sietch Stronghold",
                                                "Underground Fremen settlements with urban ecosystems",
                                                RocketTiers.T9,
                                                "chex:suits/suit5",
                                                "chex:lh2",
                                                1, true, false,
                                                Set.of("refined_spice", "sietch_technology", "fremen_artifacts",
                                                                "water_reclamation", "defense_systems"),
                                                "urban_zone", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "arrakis_storm_world"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID,
                                                                "arrakis_storm_world"),
                                                "Arrakis Storm World",
                                                "Extreme storm-ravaged desert regions with atmospheric ecosystems",
                                                RocketTiers.T10,
                                                "chex:suits/suit5",
                                                "chex:lh2",
                                                0, false, false,
                                                Set.of("storm_spice", "lightning_crystal", "storm_essence",
                                                                "electromagnetic_zones", "dust_devil_essence"),
                                                "storm_zone", false));

                // EXOTICA SYSTEM (Unique Planetary Features)
                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "ringworld_prime"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "ringworld_prime"),
                                                "Ringworld Prime",
                                                "A planet with massive ring systems and unique low-gravity mining",
                                                RocketTiers.T6,
                                                "chex:suits/suit3",
                                                "chex:rp1",
                                                4, true, true,
                                                Set.of("ring_particles", "ice_crystals", "metallic_dust",
                                                                "solar_essence", "low_gravity_ore"),
                                                "ring_system", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "aqua_mundus"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "aqua_mundus"),
                                                "Aqua Mundus",
                                                "A water world with massive ice core and underwater ecosystems",
                                                RocketTiers.T7,
                                                "chex:suits/suit4",
                                                "chex:lox",
                                                3, true, true,
                                                Set.of("ice_core", "underwater_crystals", "aquatic_metals",
                                                                "frozen_gases", "deep_sea_essence"),
                                                "water_world", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "inferno_prime"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "inferno_prime"),
                                                "Inferno Prime",
                                                "A lava world with floating obsidian islands and extreme heat",
                                                RocketTiers.T8,
                                                "chex:suits/suit4",
                                                "chex:lh2",
                                                2, false, false,
                                                Set.of("molten_ore", "obsidian_crystals", "volcanic_glass",
                                                                "lava_essence", "heat_resistant_metals"),
                                                "lava_world", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "crystalis"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "crystalis"),
                                                "Crystalis",
                                                "An ice giant with diamond core and diamond rain phenomena",
                                                RocketTiers.T9,
                                                "chex:suits/suit5",
                                                "chex:lh2",
                                                1, false, false,
                                                Set.of("diamond_core", "ice_crystals", "frozen_gases", "diamond_rain",
                                                                "pressure_crystals"),
                                                "ice_giant", false));

                registerCHEXPlanet(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "stormworld"),
                                new PlanetDef(
                                                ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "stormworld"),
                                                "Stormworld",
                                                "A gas giant with massive storm systems and atmospheric mining",
                                                RocketTiers.T10,
                                                "chex:suits/suit5",
                                                "chex:lh2",
                                                0, false, false,
                                                Set.of("atmospheric_gases", "lightning_crystals", "storm_essence",
                                                                "wind_energy", "gas_giant_core"),
                                                "gas_giant", false));
        }

        public static void registerPlanet(ResourceLocation id, PlanetDef planet) {
                PLANETS.put(id, planet);
                CHEX.LOGGER.debug("Registered planet: {} - {}", id, planet.name());
        }

        private static void registerCHEXPlanet(ResourceLocation id, PlanetDef planet) {
                registerPlanet(id, planet);
                CHEX_PLANETS.put(id, planet);
                CHEX.LOGGER.debug("Registered CHEX planet: {} - {}", id, planet.name());
        }

        public static PlanetDef getPlanet(ResourceLocation id) {
                return PLANETS.get(id);
        }

        public static Map<ResourceLocation, PlanetDef> getAllPlanets() {
                return new HashMap<>(PLANETS);
        }

        public static Map<ResourceLocation, PlanetDef> getCHEXPlanets() {
                return new HashMap<>(CHEX_PLANETS);
        }

        public static Map<ResourceLocation, PlanetDef> getPlanetsForTier(RocketTiers tier) {
                Map<ResourceLocation, PlanetDef> accessible = new HashMap<>();
                for (Map.Entry<ResourceLocation, PlanetDef> entry : PLANETS.entrySet()) {
                        if (entry.getValue().requiredRocketTier().getTier() <= tier.getTier()) {
                                accessible.put(entry.getKey(), entry.getValue());
                        }
                }
                return accessible;
        }

        public static boolean isPlanetAccessible(ResourceLocation planetId, RocketTiers rocketTier) {
                PlanetDef planet = getPlanet(planetId);
                if (planet == null)
                        return false;
                return planet.requiredRocketTier().getTier() <= rocketTier.getTier();
        }
}