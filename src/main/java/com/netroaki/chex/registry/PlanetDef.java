package com.netroaki.chex.registry;

import net.minecraft.resources.ResourceLocation;
import java.util.Set;

/**
 * Planet definition record containing all planet information
 */
public record PlanetDef(
        ResourceLocation id,
        String name,
        String description,
        RocketTiers requiredRocketTier,
        String requiredSuitTag,
        String fuelType,
        int gravityLevel,
        boolean hasAtmosphere,
        boolean requiresOxygen,
        Set<String> availableMinerals,
        String biomeType,
        boolean isOrbit) {

    /**
     * Check if this planet is accessible with the given rocket tier
     */
    public boolean isAccessibleWith(RocketTiers rocketTier) {
        return requiredRocketTier.getTier() <= rocketTier.getTier();
    }

    /**
     * Get the gravity multiplier for this planet
     */
    public float getGravityMultiplier() {
        return gravityLevel / 10.0f; // Normalize to 0.1-1.0 range
    }

    /**
     * Check if this planet requires a specific suit tier
     */
    public boolean requiresSuitTier(String suitTag) {
        return requiredSuitTag.equals(suitTag);
    }

    /**
     * Get a formatted description of the planet's requirements
     */
    public String getRequirementsDescription() {
        return String.format("Requires: %s rocket, %s suit, %s fuel",
                requiredRocketTier.getId(),
                requiredSuitTag,
                fuelType);
    }
}
