package com.netroaki.chex.config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public final class PlanetOverrideMerger {
  public record PlanetInfo(
      String name,
      int requiredRocketTier,
      String requiredSuitTag,
      String fuel,
      String description,
      Set<String> hazards,
      Set<String> minerals,
      String biomeType,
      int gravity,
      boolean hasAtmosphere,
      boolean requiresOxygen,
      boolean isOrbit,
      float temperature,
      int radiationLevel,
      float baseOxygen) {
    public PlanetInfo {
      name = sanitize(name, "");
      requiredSuitTag = sanitize(requiredSuitTag, "chex:suits/suit1");
      fuel = sanitize(fuel, "minecraft:coal");
      description = description == null ? "" : description;
      hazards = hazards == null ? Set.of() : Collections.unmodifiableSet(new LinkedHashSet<>(hazards));
      minerals = minerals == null ? Set.of() : Collections.unmodifiableSet(new LinkedHashSet<>(minerals));
      biomeType = sanitize(biomeType, "");
      gravity = Math.max(1, Math.min(gravity, 10)); // Clamp gravity between 1-10
      temperature = Math.max(0, Math.min(temperature, 1000)); // Clamp temperature between 0-1000K
      radiationLevel = Math.max(0, Math.min(radiationLevel, 10)); // Clamp radiation between 0-10
      baseOxygen = Math.max(0, Math.min(baseOxygen, 1.0f)); // Clamp oxygen between 0.0-1.0
    }

    private static String sanitize(String value, String fallback) {
      if (value == null) return fallback;
      String trimmed = value.trim();
      return trimmed.isEmpty() ? fallback : trimmed;
    }
  }

  private PlanetOverrideMerger() {}

  public static PlanetInfo merge(PlanetInfo base, PlanetOverridesCore.Entry override) {
    if (override == null) {
      return base;
    }

    // Handle basic fields with overrides
    int tier = override.requiredRocketTier > 0 ? override.requiredRocketTier : base.requiredRocketTier();
    String suitTag = !override.requiredSuitTag.isEmpty() ? override.requiredSuitTag : base.requiredSuitTag();
    String name = !override.name.isEmpty() ? override.name : base.name();
    
    // Handle fuel type override
    String fuel = !override.fuelType.isEmpty() ? override.fuelType : base.fuel();
    
    // Handle description override
    String description = !override.description.isEmpty() ? override.description : base.description();
    
    // Handle hazards override - if override has hazards, replace completely, otherwise keep base
    Set<String> hazards = override.hazards != null && !override.hazards.isEmpty() 
        ? new LinkedHashSet<>(override.hazards) 
        : new LinkedHashSet<>(base.hazards());
    
    // Handle available minerals override - if override has minerals, replace completely
    Set<String> minerals = override.availableMinerals != null && !override.availableMinerals.isEmpty() 
        ? new LinkedHashSet<>(override.availableMinerals) 
        : new LinkedHashSet<>(base.minerals());
    
    // Handle biome and environment overrides
    String biomeType = !override.biomeType.isEmpty() ? override.biomeType : base.biomeType();
    int gravity = override.gravity != null ? override.gravity : base.gravity();
    boolean hasAtmosphere = override.hasAtmosphere != null ? override.hasAtmosphere : base.hasAtmosphere();
    boolean requiresOxygen = override.requiresOxygen != null ? override.requiresOxygen : base.requiresOxygen();
    boolean isOrbit = override.isOrbit != null ? override.isOrbit : base.isOrbit();
    
    // Handle environmental conditions
    float temperature = override.temperature != null ? override.temperature : base.temperature();
    int radiationLevel = override.radiationLevel != null ? override.radiationLevel : base.radiationLevel();
    float baseOxygen = override.baseOxygen != null ? override.baseOxygen : base.baseOxygen();

    // Ensure values are within valid ranges
    gravity = Math.max(1, Math.min(gravity, 10));
    temperature = Math.max(0, Math.min(temperature, 1000));
    radiationLevel = Math.max(0, Math.min(radiationLevel, 10));
    baseOxygen = Math.max(0, Math.min(baseOxygen, 1.0f));

    return new PlanetInfo(
        name,
        tier,
        suitTag,
        fuel,
        description,
        hazards,
        minerals,
        biomeType,
        gravity,
        hasAtmosphere,
        requiresOxygen,
        isOrbit,
        temperature,
        radiationLevel,
        baseOxygen
    );
  }

  private static int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }

  private static String buildSuitTag(int tier) {
    int clamped = clamp(tier, 1, 5);
    return String.format(Locale.ROOT, "chex:suits/suit%d", clamped);
  }
}
