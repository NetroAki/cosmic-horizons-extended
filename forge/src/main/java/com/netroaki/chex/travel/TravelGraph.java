package com.netroaki.chex.travel;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.core.TravelGraphCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.netroaki.chex.config.TravelConfigLoader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Travel graph system that defines which planets are accessible at each rocket
 * tier
 * Based on the checklist: T1: LEO, T2: Moon, T3: Mars orbit, T4: Mars surface,
 * etc.
 */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class TravelGraph implements ResourceManagerReloadListener {

    private static final Map<Integer, Set<ResourceLocation>> TIER_TO_PLANETS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        initializeDefaultGraph();
        CHEX.LOGGER.info("Travel graph initialized with {} planets across {} tiers",
                TravelGraphCore.getAllPlanets().size(), TIER_TO_PLANETS.size());
    }

    /**
     * Initialize the default travel graph based on the checklist progression
     */
    public static void initializeDefaultGraph() {
        TravelGraphCore.initializeDefaultGraph();
        // Extend defaults with CHEX extras, then sync
        TravelGraphCore.add(1, "cosmos:earth_moon", "cosmos");
        TravelGraphCore.add(1, "cosmos:mercury", "cosmos");
        TravelGraphCore.add(2, "cosmos:earth_moon", "cosmos");
        TravelGraphCore.add(2, "cosmos:mercury", "cosmos");
        TravelGraphCore.add(2, "cosmos:venus", "cosmos");
        TravelGraphCore.add(3, "cosmos:mars", "cosmos");
        TravelGraphCore.add(3, "cosmic_horizons_extended:pandora", "chex");
        TravelGraphCore.add(4, "cosmos:mars", "cosmos");
        TravelGraphCore.add(4, "cosmic_horizons_extended:arrakis", "chex");
        TravelGraphCore.add(4, "cosmos:jupiter", "cosmos");
        TravelGraphCore.add(5, "cosmos:jupiter", "cosmos");
        TravelGraphCore.add(5, "cosmos:europa", "cosmos");
        TravelGraphCore.add(5, "cosmic_horizons_extended:aqua_mundus", "chex");
        TravelGraphCore.add(6, "cosmos:saturn", "cosmos");
        TravelGraphCore.add(6, "cosmos:uranus", "cosmos");
        TravelGraphCore.add(6, "cosmic_horizons_extended:inferno_prime", "chex");
        TravelGraphCore.add(7, "cosmos:neptune", "cosmos");
        TravelGraphCore.add(7, "cosmos:pluto", "cosmos");
        TravelGraphCore.add(7, "cosmic_horizons_extended:kepler_452b", "chex");
        TravelGraphCore.add(7, "cosmic_horizons_extended:crystalis", "chex");
        TravelGraphCore.add(8, "cosmos:alpha_system", "cosmos");
        TravelGraphCore.add(8, "cosmos:b_1400_centauri", "cosmos");
        TravelGraphCore.add(8, "cosmos:j_1407b", "cosmos");
        TravelGraphCore.add(8, "cosmic_horizons_extended:exotica", "chex");
        TravelGraphCore.add(9, "cosmos:j_1900", "cosmos");
        TravelGraphCore.add(9, "cosmos:glacio", "cosmos");
        TravelGraphCore.add(9, "cosmic_horizons_extended:aurelia_ringworld", "chex");
        TravelGraphCore.add(9, "cosmic_horizons_extended:shattered_dyson_swarm", "chex");
        TravelGraphCore.add(10, "cosmos:gaia_bh_1", "cosmos");
        TravelGraphCore.add(10, "cosmos:solar_system", "cosmos");
        TravelGraphCore.add(10, "cosmic_horizons_extended:torus_world", "chex");
        TravelGraphCore.add(10, "cosmic_horizons_extended:hollow_world", "chex");
        TravelGraphCore.add(10, "cosmic_horizons_extended:neutron_star_forge", "chex");
        syncFromCore();

        CHEX.LOGGER.info("Default travel graph loaded: {} planets across {} tiers",
                TravelGraphCore.getAllPlanets().size(), TIER_TO_PLANETS.size());
    }

    public static void loadFromConfigOrDefaults() {
        // Try config first
        boolean loaded = false;
        var mapOpt = TravelConfigLoader.load();
        if (mapOpt.isPresent() && !mapOpt.get().isEmpty()) {
            // Convert RL mapping to string mapping for core
            Map<Integer, Set<String>> asStrings = new HashMap<>();
            mapOpt.get().forEach((tier, set) -> {
                Set<String> ids = new HashSet<>();
                for (ResourceLocation rl : set)
                    ids.add(rl.toString());
                asStrings.put(tier, ids);
            });
            TravelGraphCore.loadFromMapping(asStrings);
            syncFromCore();
            loaded = true;
            CHEX.LOGGER.info("Travel graph loaded from config: {} planets across {} tiers",
                    TravelGraphCore.getAllPlanets().size(), TIER_TO_PLANETS.size());
        }
        if (!loaded) {
            initializeDefaultGraph();
        }
    }

    private static void syncFromCore() {
        TIER_TO_PLANETS.clear();
        for (int i = 1; i <= 10; i++) {
            Set<ResourceLocation> planets = new HashSet<>();
            for (String id : TravelGraphCore.getPlanetsForTier(i)) {
                planets.add(ResourceLocation.parse(id));
            }
            if (!planets.isEmpty())
                TIER_TO_PLANETS.put(i, planets);
        }
    }

    /**
     * Get all planets accessible at a given nodule tier
     */
    public static Set<ResourceLocation> getPlanetsForTier(int tier) {
        return new HashSet<>(TIER_TO_PLANETS.getOrDefault(tier, Collections.emptySet()));
    }

    /**
     * Get the minimum nodule tier required to access a planet
     */
    public static int getTierForPlanet(ResourceLocation planet) {
        return TravelGraphCore.getTierForPlanet(planet.toString());
    }

    /**
     * Check if a planet is accessible at a given nodule tier
     */
    public static boolean isPlanetAccessible(ResourceLocation planet, int noduleTier) {
        return TravelGraphCore.isPlanetAccessible(planet.toString(), noduleTier);
    }

    /**
     * Get the source of a planet (cosmos, chex, etc.)
     */
    public static String getPlanetSource(ResourceLocation planet) {
        return TravelGraphCore.getPlanetSource(planet.toString());
    }

    /**
     * Get all planets in the travel graph
     */
    public static Set<ResourceLocation> getAllPlanets() {
        Set<ResourceLocation> r = new HashSet<>();
        for (String id : TravelGraphCore.getAllPlanets())
            r.add(ResourceLocation.parse(id));
        return r;
    }

    /**
     * Get a summary of the travel graph for debugging
     */
    public static String getTravelGraphSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CHEX Travel Graph Summary ===\n");

        for (int tier = 1; tier <= 10; tier++) {
            Set<ResourceLocation> planets = TIER_TO_PLANETS.get(tier);
            if (planets != null && !planets.isEmpty()) {
                sb.append(String.format("T%d (%d planets):\n", tier, planets.size()));
                for (ResourceLocation planet : planets) {
                    String source = getPlanetSource(planet);
                    sb.append(String.format("  - %s (%s)\n", planet, source));
                }
            }
        }

        return sb.toString();
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        // Reload travel graph from config files if they exist
        loadFromConfigOrDefaults();
        CHEX.LOGGER.info("Travel graph reloaded from resources");
    }
}