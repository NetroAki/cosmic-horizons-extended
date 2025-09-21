package com.netroaki.chex.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanetOverrideMergerTest {

  @Test
  void mergesHazardsWithoutDroppingBaseEntries() {
    PlanetOverrideMerger.PlanetInfo baseInfo =
        new PlanetOverrideMerger.PlanetInfo(
            "Pandora",
            6,
            "chex:suits/suit3",
            "chex:rp1",
            "Base description",
            new LinkedHashSet<>(List.of("vacuum", "radiation")));

    PlanetOverridesCore.Entry override =
        new PlanetOverridesCore.Entry(
            null,
            null,
            null,
            null,
            "Override description",
            null,
            new LinkedHashSet<>(List.of("acid", "radiation")));

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(baseInfo, override);

    assertEquals("Override description", merged.description());
    assertEquals(6, merged.requiredRocketTier());
    assertEquals("chex:suits/suit3", merged.requiredSuitTag());
    assertEquals("chex:rp1", merged.fuel());
    assertEquals(3, merged.hazards().size());
    assertTrue(merged.hazards().containsAll(Set.of("vacuum", "radiation", "acid")));
  }

  @Test
  void overrideFieldsFallbackWhenMissing() {
    PlanetOverrideMerger.PlanetInfo baseInfo =
        new PlanetOverrideMerger.PlanetInfo(
            "Arrakis", 5, "chex:suits/suit3", "chex:kerosene", "Desert world", Set.of("sandstorm"));

    PlanetOverridesCore.Entry override =
        new PlanetOverridesCore.Entry(7, 4, null, "Arrakis Prime", null, "chex:lh2", null);

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(baseInfo, override);

    assertEquals("Arrakis Prime", merged.name());
    assertEquals(7, merged.requiredRocketTier());
    assertEquals("chex:suits/suit4", merged.requiredSuitTag());
    assertEquals("chex:lh2", merged.fuel());
    assertEquals("Desert world", merged.description());
    assertEquals(Set.of("sandstorm"), merged.hazards());
  }
}
