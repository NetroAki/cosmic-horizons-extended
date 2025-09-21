package com.netroaki.chex.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanetOverrideMergerTest {

  @Test
  void mergeAppliesOverrideValues() {
    PlanetOverrideMerger.PlanetInfo base =
        new PlanetOverrideMerger.PlanetInfo(
            "Earth Moon",
            1,
            "chex:suits/suit1",
            "chex:kerosene",
            "Base description",
            Set.of("vacuum"));

    PlanetOverridesCore.Entry override =
        new PlanetOverridesCore.Entry(
            4,
            null,
            "chex:suits/suit4",
            "Luna",
            "Override description",
            "chex:lox",
            Set.of("radiation", "vacuum"));

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(base, override);

    assertEquals(4, merged.requiredRocketTier());
    assertEquals("chex:suits/suit4", merged.requiredSuitTag());
    assertEquals("chex:lox", merged.fuel());
    assertEquals("Override description", merged.description());
    assertEquals("Luna", merged.name());
    assertEquals(Set.of("radiation", "vacuum"), merged.hazards());
  }

  @Test
  void mergeFallsBackToBaseWhenOverrideMissingFields() {
    PlanetOverrideMerger.PlanetInfo base =
        new PlanetOverrideMerger.PlanetInfo(
            "Pandora",
            6,
            "chex:suits/suit3",
            "chex:rp1",
            "Pandora description",
            Set.of("toxic"));

    PlanetOverridesCore.Entry override =
        new PlanetOverridesCore.Entry(null, null, null, null, null, null, null);

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(base, override);

    assertEquals(base.requiredRocketTier(), merged.requiredRocketTier());
    assertEquals(base.requiredSuitTag(), merged.requiredSuitTag());
    assertEquals(base.fuel(), merged.fuel());
    assertEquals(base.description(), merged.description());
    assertEquals(base.name(), merged.name());
    assertEquals(base.hazards(), merged.hazards());
  }

  @Test
  void mergeBuildsSuitTagFromSuitTierWhenProvided() {
    PlanetOverrideMerger.PlanetInfo base =
        new PlanetOverrideMerger.PlanetInfo(
            "Arrakis",
            5,
            "chex:suits/suit3",
            "chex:kerosene",
            "Desert world",
            Set.of());

    PlanetOverridesCore.Entry override =
        new PlanetOverridesCore.Entry(null, 4, null, null, null, null, null);

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(base, override);
    assertEquals("chex:suits/suit4", merged.requiredSuitTag());
  }
}
