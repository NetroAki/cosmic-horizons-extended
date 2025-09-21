package com.netroaki.chex.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanetOverrideMergerTest {

  @Test
  void mergeAppliesOverrideValuesAndUnionsHazards() {
    PlanetOverrideMerger.PlanetInfo base =
        new PlanetOverrideMerger.PlanetInfo(
            "Mars", 2, "chex:suits/suit2", "chex:rp1", "Base description", Set.of("vacuum"));

    PlanetOverridesCore.Entry override =
        new PlanetOverridesCore.Entry(
            3,
            4,
            null,
            "New Mars",
            "Override description",
            "chex:lox",
            Set.of("radiation", "vacuum"));

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(base, override);

    assertEquals("New Mars", merged.name());
    assertEquals(3, merged.requiredRocketTier());
    assertEquals("chex:suits/suit4", merged.requiredSuitTag());
    assertEquals("chex:lox", merged.fuel());
    assertEquals("Override description", merged.description());
    assertEquals(Set.of("vacuum", "radiation"), merged.hazards());
  }

  @Test
  void mergeRetainsBaseValuesWhenOverrideMissingFields() {
    PlanetOverrideMerger.PlanetInfo base =
        new PlanetOverrideMerger.PlanetInfo(
            "Europa", 3, "chex:suits/suit3", "chex:rp1", "Icy moon", Set.of("vacuum", "radiation"));

    PlanetOverridesCore.Entry override =
        new PlanetOverridesCore.Entry(null, null, null, null, null, null, null);

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(base, override);

    assertEquals(base.name(), merged.name());
    assertEquals(base.requiredRocketTier(), merged.requiredRocketTier());
    assertEquals(base.requiredSuitTag(), merged.requiredSuitTag());
    assertEquals(base.fuel(), merged.fuel());
    assertEquals(base.description(), merged.description());
    assertEquals(base.hazards(), merged.hazards());
  }

  @Test
  void mergeAllowsClearingHazardsWhenExplicitlyProvided() {
    PlanetOverrideMerger.PlanetInfo base =
        new PlanetOverrideMerger.PlanetInfo(
            "Venus",
            4,
            "chex:suits/suit3",
            "chex:lox",
            "Toxic clouds",
            Set.of("radiation", "acid"));

    PlanetOverridesCore.Entry override =
        new PlanetOverridesCore.Entry(null, null, null, null, null, null, Set.of());

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(base, override);

    assertTrue(
        merged.hazards().isEmpty(), "Hazards should be cleared when override specifies empty set");
  }
}
