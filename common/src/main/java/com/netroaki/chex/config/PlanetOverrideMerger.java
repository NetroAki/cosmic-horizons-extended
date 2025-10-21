package com.netroaki.chex.config;

import java.util.Locale;
import java.util.Set;

public final class PlanetOverrideMerger {
  /**
   * Represents all the information about a planet that can be overridden. All fields are immutable
   * and non-null (with empty/default values where appropriate).
   */
  public record PlanetInfo(
      String name,
      int requiredRocketTier,
      String requiredSuitTag,
      String fuel,
      String description,
      int gravityLevel,
      boolean hasAtmosphere,
      boolean requiresOxygen,
      Set<String> hazards,
      Set<String> availableMinerals,
      String biomeType,
      boolean isOrbit) {

    public PlanetInfo {
      // Ensure all string fields are non-null and trimmed
      name = sanitize(name, "");
      requiredSuitTag = sanitize(requiredSuitTag, "chex:suits/suit1");
      fuel = sanitize(fuel, "minecraft:lava");
      description = sanitize(description, "");
      biomeType = sanitize(biomeType, "default");

      // Ensure sets are immutable and lowercase
      hazards =
          hazards != null
              ? Set.copyOf(hazards.stream().map(String::toLowerCase).toList())
              : Set.of();
      availableMinerals =
          availableMinerals != null
              ? Set.copyOf(availableMinerals.stream().map(String::toLowerCase).toList())
              : Set.of();
    }

    private static String sanitize(String value, String fallback) {
      if (value == null) return fallback;
      String trimmed = value.trim();
      return trimmed.isEmpty() ? fallback : trimmed;
    }
  }

  private PlanetOverrideMerger() {}

  /** Returns the first non-null value, or the second value if the first is null. */
  private static String firstNonNull(String first, String second) {
    return first != null ? first : second;
  }

  /** Clamps a value between min and max (inclusive). */
  private static int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }

  /**
   * Merges a base planet info with override values.
   *
   * @param base The base planet info to merge into
   * @param override The override values to apply
   * @return A new PlanetInfo with the merged values
   */
  /**
   * Merges a base planet info with override values.
   *
   * @param base The base planet info to merge into
   * @param override The override values to apply
   * @return A new PlanetInfo with the merged values
   */
  public static PlanetInfo merge(PlanetInfo base, PlanetOverridesCore.Entry override) {
    if (override == null) {
      return base;
    }

    // Basic information
    String name = firstNonNull(override.name(), base.name());
    String description = firstNonNull(override.description(), base.description());

    // Requirements
    int requiredRocketTier =
        override.requiredRocketTier() != null
            ? clamp(override.requiredRocketTier(), 1, 10)
            : base.requiredRocketTier();

    // Handle suit tag - prefer explicit tag, fall back to tier-based tag, then base
    // value
    String requiredSuitTag =
        firstNonNull(
            override.requiredSuitTag(),
            override.requiredSuitTier() != null
                ? buildSuitTag(override.requiredSuitTier())
                : base.requiredSuitTag());

    String fuelType = firstNonNull(override.fuelType(), base.fuel());

    // Environmental properties
    int gravityLevel =
        override.gravityLevel() != null
            ? clamp(override.gravityLevel(), 0, 10)
            : base.gravityLevel();

    boolean hasAtmosphere =
        override.hasAtmosphere() != null ? override.hasAtmosphere() : base.hasAtmosphere();

    boolean requiresOxygen =
        override.requiresOxygen() != null ? override.requiresOxygen() : base.requiresOxygen();

    // Content - merge sets to avoid losing base data
    Set<String> hazards =
        override.hazards() != null ? unionSets(base.hazards(), override.hazards()) : base.hazards();

    Set<String> availableMinerals =
        override.availableMinerals() != null
            ? unionSets(base.availableMinerals(), override.availableMinerals())
            : base.availableMinerals();

    String biomeType = firstNonNull(override.biomeType(), base.biomeType());
    boolean isOrbit = override.isOrbit() != null ? override.isOrbit() : base.isOrbit();

    return new PlanetInfo(
        name,
        requiredRocketTier,
        requiredSuitTag,
        fuelType,
        description,
        gravityLevel,
        hasAtmosphere,
        requiresOxygen,
        hazards,
        availableMinerals,
        biomeType,
        isOrbit);
  }

  /** Builds a suit tag from a tier number. */
  private static String buildSuitTag(int tier) {
    int clamped = clamp(tier, 1, 5);
    return String.format(Locale.ROOT, "chex:suits/suit%d", clamped);
  }

  private static Set<String> unionSets(Set<String> a, Set<String> b) {
    if (a == null || a.isEmpty()) return b != null ? Set.copyOf(b) : Set.of();
    if (b == null || b.isEmpty()) return Set.copyOf(a);
    java.util.LinkedHashSet<String> merged = new java.util.LinkedHashSet<>(a);
    merged.addAll(b);
    return Set.copyOf(merged);
  }
}
