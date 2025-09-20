package com.netroaki.chex.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Loader-neutral core for nodule material themes by tier.
 */
public final class NoduleDesignsCore {

    public static final class NoduleTheme {
        private final String primaryMaterial;
        private final List<String> supportingMaterials;

        public NoduleTheme(String primaryMaterial, List<String> supportingMaterials) {
            this.primaryMaterial = primaryMaterial;
            this.supportingMaterials = List.copyOf(supportingMaterials);
        }

        public String getPrimaryMaterial() {
            return primaryMaterial;
        }

        public List<String> getSupportingMaterials() {
            return supportingMaterials;
        }
    }

    private static final Map<Integer, NoduleTheme> THEMES_BY_TIER = new HashMap<>();

    private NoduleDesignsCore() {
    }

    public static void initializeDefaults() {
        THEMES_BY_TIER.clear();
        THEMES_BY_TIER.put(1, new NoduleTheme("iron", List.of("copper", "tin")));
        THEMES_BY_TIER.put(2, new NoduleTheme("nickel", List.of("silver", "lead")));
        THEMES_BY_TIER.put(3, new NoduleTheme("titanium", List.of("vanadium", "lithium")));
        THEMES_BY_TIER.put(4, new NoduleTheme("tungsten", List.of("molybdenum", "chromium")));
        THEMES_BY_TIER.put(5, new NoduleTheme("platinum", List.of("iridium", "palladium")));
        THEMES_BY_TIER.put(6, new NoduleTheme("spice_melange", List.of("desert_glass", "sandworm_essence")));
        THEMES_BY_TIER.put(7, new NoduleTheme("uranium", List.of("thorium", "osmium")));
        THEMES_BY_TIER.put(8, new NoduleTheme("beryllium", List.of("fluorite", "ruby")));
        THEMES_BY_TIER.put(9, new NoduleTheme("diamond_core", List.of("pressure_crystals", "ice_crystals")));
        THEMES_BY_TIER.put(10, new NoduleTheme("mythic_alloy", List.of("storm_essence", "levitation_essence")));
    }

    public static Optional<NoduleTheme> getThemeForTier(int tier) {
        return Optional.ofNullable(THEMES_BY_TIER.get(tier));
    }
}
