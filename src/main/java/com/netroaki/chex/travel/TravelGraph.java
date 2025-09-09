package com.netroaki.chex.travel;

import com.netroaki.chex.registry.RocketTiers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class TravelGraph {
    private static final Map<RocketTiers, List<ResourceKey<Level>>> tierToPlanets = new EnumMap<>(RocketTiers.class);

    private TravelGraph() {
    }

    public static void clear() {
        tierToPlanets.clear();
    }

    public static void addReachable(RocketTiers tier, ResourceKey<Level> planet) {
        tierToPlanets.computeIfAbsent(tier, t -> new ArrayList<>()).add(planet);
    }

    public static List<ResourceKey<Level>> getReachable(RocketTiers tier) {
        return tierToPlanets.getOrDefault(tier, List.of());
    }
}
