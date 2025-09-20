package com.netroaki.chex.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loader-neutral core for travel graph lookups and storage.
 * Uses String planet IDs to avoid loader dependencies.
 */
public final class TravelGraphCore {
    private TravelGraphCore() {}

    private static final Map<Integer, Set<String>> TIER_TO_PLANETS = new ConcurrentHashMap<>();
    private static final Map<String, Integer> PLANET_TO_TIER = new ConcurrentHashMap<>();
    private static final Map<String, String> PLANET_SOURCES = new ConcurrentHashMap<>();

    public static void clear() {
        TIER_TO_PLANETS.clear();
        PLANET_TO_TIER.clear();
        PLANET_SOURCES.clear();
    }

    public static void initializeDefaultGraph() {
        clear();
        add(1, "cosmos:earth_moon", "cosmos");
        add(2, "cosmos:venus", "cosmos");
        add(3, "cosmos:mars", "cosmos");
    }

    public static void loadFromMapping(Map<Integer, Set<String>> mapping) {
        clear();
        mapping.forEach((tier, set) -> set.forEach(id -> add(tier, id, ns(id))));
    }

    public static void add(int tier, String planetId, String source) {
        TIER_TO_PLANETS.computeIfAbsent(tier, k -> new HashSet<>()).add(planetId);
        PLANET_TO_TIER.put(planetId, tier);
        PLANET_SOURCES.put(planetId, source);
    }

    public static Set<String> getPlanetsForTier(int tier) {
        return new HashSet<>(TIER_TO_PLANETS.getOrDefault(tier, Collections.emptySet()));
    }

    public static int getTierForPlanet(String planetId) {
        return PLANET_TO_TIER.getOrDefault(planetId, Integer.MAX_VALUE);
    }

    public static boolean isPlanetAccessible(String planetId, int tier) {
        return getTierForPlanet(planetId) <= tier;
    }

    public static String getPlanetSource(String planetId) {
        return PLANET_SOURCES.getOrDefault(planetId, "unknown");
    }

    public static Set<String> getAllPlanets() {
        return new HashSet<>(PLANET_TO_TIER.keySet());
    }

    private static String ns(String id) {
        int i = id.indexOf(':');
        return i > 0 ? id.substring(0, i) : "unknown";
    }
}


