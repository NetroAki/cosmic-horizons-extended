package com.netroaki.chex.registry;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;

/** Planet definition record containing all planet information */
public record PlanetDef(
    ResourceLocation id,
    String name,
    String description,
    NoduleTiers requiredRocketTier,
    String requiredSuitTag,
    String fuelType,
    int gravityLevel,
    boolean hasAtmosphere,
    boolean requiresOxygen,
    Set<String> hazards,
    Set<String> availableMinerals,
    String biomeType,
    boolean isOrbit) {

  public PlanetDef {
    hazards = hazards == null ? Set.of() : unmodifiableLowercaseSet(hazards);
    availableMinerals =
        availableMinerals == null
            ? Set.of()
            : Collections.unmodifiableSet(new LinkedHashSet<>(availableMinerals));
  }

  private static Set<String> unmodifiableLowercaseSet(Set<String> source) {
    LinkedHashSet<String> lowered = new LinkedHashSet<>();
    for (String value : source) {
      if (value != null && !value.isBlank()) {
        lowered.add(value.toLowerCase());
      }
    }
    return Collections.unmodifiableSet(lowered);
  }

  /** Check if this planet is accessible with the given nodule tier */
  public boolean isAccessibleWith(NoduleTiers rocketTier) {
    return requiredRocketTier.getTier() <= rocketTier.getTier();
  }

  /** Get the gravity multiplier for this planet */
  public float getGravityMultiplier() {
    return gravityLevel / 10.0f; // Normalize to 0.1-1.0 range
  }

  /** Check if this planet requires a specific suit tier */
  public boolean requiresSuitTier(String suitTag) {
    return requiredSuitTag.equals(suitTag);
  }

  /** Get a formatted description of the planet's requirements */
  public String getRequirementsDescription() {
    return String.format(
        "Requires: %s nodule, %s suit, %s fuel",
        requiredRocketTier.getId(), requiredSuitTag, fuelType);
  }

  /** Check if the planet lists a hazard by id (case-insensitive). */
  public boolean hasHazard(String hazardId) {
    if (hazardId == null || hazardId.isBlank()) {
      return false;
    }
    return hazards.contains(hazardId.toLowerCase());
  }
}
