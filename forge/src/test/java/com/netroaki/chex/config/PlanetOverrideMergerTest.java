package com.netroaki.chex.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlanetOverrideMergerTest {

  private PlanetOverrideMerger.PlanetInfo basePlanet(
      Set<String> hazards, Set<String> minerals, String suitTag) {
    return new PlanetOverrideMerger.PlanetInfo(
        "Base Name",
        3,
        suitTag,
        "base:fuel",
        "Base description",
        4,
        true,
        true,
        hazards,
        minerals,
        "base_biome",
        false);
  }

  private PlanetOverridesCore.Entry overrideEntry(
      Integer requiredRocketTier,
      Integer requiredSuitTier,
      String requiredSuitTag,
      String name,
      String description,
      String fuelType,
      Set<String> hazards,
      Integer gravity,
      Boolean hasAtmosphere,
      Boolean requiresOxygen,
      Set<String> minerals,
      String biomeType,
      Boolean isOrbit) {
    return new PlanetOverridesCore.Entry(
        requiredRocketTier,
        requiredSuitTier,
        requiredSuitTag,
        name,
        description,
        fuelType,
        hazards,
        gravity,
        hasAtmosphere,
        requiresOxygen,
        minerals,
        biomeType,
        isOrbit);
  }

  @Test
  @DisplayName("Merges hazards and minerals as union without losing base data")
  void testHazardsAndMineralsUnionMerge() {
    var base =
        basePlanet(Set.of("vacuum", "radiation"), Set.of("iron", "water"), "chex:suits/suit2");
    var override =
        overrideEntry(
            null,
            null,
            null,
            null,
            null,
            null,
            Set.of("extreme_heat"),
            null,
            null,
            null,
            Set.of("silicon"),
            null,
            null);

    var merged = PlanetOverrideMerger.merge(base, override);

    assertEquals(Set.of("vacuum", "radiation", "extreme_heat"), merged.hazards());
    assertEquals(Set.of("iron", "water", "silicon"), merged.availableMinerals());
  }

  @Test
  @DisplayName("Suit tag override takes precedence; otherwise suit tier builds tag; else base")
  void testSuitTagOverrideAndTierFallback() {
    var base = basePlanet(Set.of(), Set.of(), "chex:suits/suit3");

    // direct tag wins
    var withTag =
        overrideEntry(
            null,
            5,
            "chex:suits/suit4",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    var mergedTag = PlanetOverrideMerger.merge(base, withTag);
    assertEquals("chex:suits/suit4", mergedTag.requiredSuitTag());

    // tier builds tag if tag not provided
    var withTier =
        overrideEntry(null, 4, null, null, null, null, null, null, null, null, null, null, null);
    var mergedTier = PlanetOverrideMerger.merge(base, withTier);
    assertEquals("chex:suits/suit4", mergedTier.requiredSuitTag());

    // fallback to base if nothing specified
    var none =
        overrideEntry(null, null, null, null, null, null, null, null, null, null, null, null, null);
    var mergedNone = PlanetOverrideMerger.merge(base, none);
    assertEquals("chex:suits/suit3", mergedNone.requiredSuitTag());
  }

  @Test
  @DisplayName("Overrides replace simple fields and clamp numeric ranges")
  void testOverridesSimpleFieldsAndClamping() {
    var base = basePlanet(Set.of(), Set.of(), "chex:suits/suit1");
    var override =
        overrideEntry(
            999, // rocket tier gets clamped to 10
            null,
            null,
            "New Name",
            "New Desc",
            "chex:new_fuel",
            null,
            42, // gravity clamped to 10
            false,
            false,
            null,
            "new_biome",
            true);

    var merged = PlanetOverrideMerger.merge(base, override);

    assertEquals("New Name", merged.name());
    assertEquals("New Desc", merged.description());
    assertEquals("chex:new_fuel", merged.fuel());
    assertEquals(10, merged.requiredRocketTier());
    assertEquals(10, merged.gravityLevel());
    assertFalse(merged.hasAtmosphere());
    assertFalse(merged.requiresOxygen());
    assertEquals("new_biome", merged.biomeType());
    assertTrue(merged.isOrbit());
  }
}
