package com.netroaki.chex.registry;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;

/**
 * Planet definition record containing all planet information.
 * 
 * @param id The unique identifier for this planet
 * @param name The display name of the planet
 * @param description A description of the planet
 * @param requiredRocketTier Minimum rocket tier required to travel to this planet
 * @param requiredSuitTag Required suit type for this planet's environment
 * @param fuelType Type of fuel required for travel to this planet
 * @param gravityLevel Gravity level (1-10, where 1 is very low and 10 is very high)
 * @param hasAtmosphere Whether the planet has an atmosphere
 * @param requiresOxygen Whether the planet requires oxygen for survival
 * @param hazards Set of environmental hazards present on this planet
 * @param availableMinerals Set of minerals that can be found on this planet
 * @param biomeType The primary biome type of this planet
 * @param isOrbit Whether this is an orbital body (true) or a planet/moon (false)
 * @param temperature Temperature in Kelvin (200-500K)
 * @param radiationLevel Radiation level (0-10, where 0 is none and 10 is extreme)
 * @param baseOxygen Base oxygen level (0.0-1.0, where 0 is no oxygen and 1.0 is Earth-like)
 */
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
    boolean isOrbit,
    float temperature,
    int radiationLevel,
    float baseOxygen) {

  public PlanetDef {
    hazards = hazards == null ? Set.of() : unmodifiableLowercaseSet(hazards);
    availableMinerals =
        availableMinerals == null
            ? Set.of()
            : Collections.unmodifiableSet(new LinkedHashSet<>(availableMinerals));
            
    // Validate fields
    if (temperature < 0 || temperature > 1000) {
      throw new IllegalArgumentException("Temperature must be between 0 and 1000 Kelvin");
    }
    if (radiationLevel < 0 || radiationLevel > 10) {
      throw new IllegalArgumentException("Radiation level must be between 0 and 10");
    }
    if (baseOxygen < 0 || baseOxygen > 1.0f) {
      throw new IllegalArgumentException("Base oxygen level must be between 0.0 and 1.0");
    }
    if (gravityLevel < 1 || gravityLevel > 10) {
      throw new IllegalArgumentException("Gravity level must be between 1 and 10");
    }
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
  
  /**
   * Get the gravity in m/s²
   * @return Gravity in m/s² (Earth = 9.8)
   */
  public float getGravityMetersPerSecondSquared() {
    return getGravityMultiplier() * 9.8f;
  }

  /** 
   * Check if the planet requires a specific suit tier or higher
   * @param suitTag The suit tag to check
   * @return true if the suit is sufficient for this planet
   */
  public boolean isSuitSufficient(String suitTag) {
    // Default implementation - can be enhanced with a tier system
    return requiredSuitTag.equals(suitTag) || 
           (hasAtmosphere && !requiresOxygen && (suitTag == null || suitTag.isEmpty()));
  }
  
  /**
   * Get the minimum required suit tier for this planet
   * @return The required suit tag
   */
  public String getRequiredSuitTier() {
    return requiredSuitTag;
  }

  /** 
   * Check if this planet requires a specific suit tier
   * @param suitTag The suit tag to check
   * @return true if the suit is required for this planet
   */
  public boolean requiresSuitTier(String suitTag) {
    return requiredSuitTag.equals(suitTag);
  }

  /** 
   * Get a formatted description of the planet's requirements and environment
   * @return Formatted string with planet requirements and environmental data
   */
  public String getRequirementsDescription() {
    return String.format(
        """
        === %s ===
        Requirements: %s nodule, %s suit, %s fuel
        Environment: %.1fK, %d/10 radiation, %.1f%% O₂, %d/10 gravity
        Hazards: %s
        Available Minerals: %s
        """,
        name,
        requiredRocketTier.getId(),
        requiredSuitTag,
        fuelType,
        temperature,
        radiationLevel,
        baseOxygen * 100,
        gravityLevel,
        String.join(", ", hazards),
        String.join(", ", availableMinerals)
    );
  }
  
  /**
   * Check if the planet is habitable without special equipment
   * @return true if the planet is habitable without special equipment
   */
  public boolean isHabitable() {
    return !requiresOxygen && 
           hasAtmosphere && 
           temperature >= 250 && 
           temperature <= 320 &&
           radiationLevel <= 2 &&
           baseOxygen >= 0.15f;
  }
  
  /**
   * Get the temperature in Celsius
   * @return Temperature in Celsius
   */
  public float getTemperatureCelsius() {
    return temperature - 273.15f;
  }
  
  /**
   * Get the temperature in Fahrenheit
   * @return Temperature in Fahrenheit
   */
  public float getTemperatureFahrenheit() {
    return (temperature - 273.15f) * 9/5 + 32;
  }
  
  /**
   * Get the radiation level as a human-readable string
   * @return Radiation level description
   */
  public String getRadiationDescription() {
    return switch (radiationLevel) {
      case 0 -> "None";
      case 1, 2 -> "Low";
      case 3, 4 -> "Moderate";
      case 5, 6 -> "High";
      case 7, 8 -> "Very High";
      case 9, 10 -> "Extreme";
      default -> "Unknown";
    };
  }

  /** 
   * Check if the planet has a specific hazard
   * @param hazardId The hazard ID to check (case-insensitive)
   * @return true if the hazard is present
   */
  public boolean hasHazard(String hazardId) {
    if (hazardId == null || hazardId.isBlank()) {
      return false;
    }
    return hazards.contains(hazardId.toLowerCase());
  }
  
  /**
   * Get the set of all hazards on this planet
   * @return Unmodifiable set of hazard IDs
   */
  public Set<String> getHazards() {
    return Collections.unmodifiableSet(hazards);
  }
  
  /**
   * Check if this planet has any hazards that would be dangerous to the player
   * @return true if there are any hazardous conditions
   */
  public boolean hasHazardousConditions() {
    return !hazards.isEmpty() || radiationLevel > 3 || temperature > 333 || temperature < 253;
  }
}
