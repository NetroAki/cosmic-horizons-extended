package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;

import java.util.List;
import java.util.Optional;

public final class NoduleDesigns {

    public static final class NoduleTheme {
        private final String primaryMaterial;
        private final java.util.List<String> supportingMaterials;

        public NoduleTheme(String primaryMaterial, java.util.List<String> supportingMaterials) {
            this.primaryMaterial = primaryMaterial;
            this.supportingMaterials = java.util.List.copyOf(supportingMaterials);
        }

        public String getPrimaryMaterial() {
            return primaryMaterial;
        }

        public java.util.List<String> getSupportingMaterials() {
            return supportingMaterials;
        }
    }

    private NoduleDesigns() {
    }

    public static void initializeDefaults() {
        NoduleDesignsCore.initializeDefaults();
        CHEX.LOGGER.info("Initialized hardcoded nodule themes for tiers 1-10");
    }

    public static Optional<NoduleTheme> getThemeForTier(int tier) {
        return NoduleDesignsCore.getThemeForTier(tier)
                .map(t -> new NoduleTheme(t.getPrimaryMaterial(), t.getSupportingMaterials()));
    }
}
