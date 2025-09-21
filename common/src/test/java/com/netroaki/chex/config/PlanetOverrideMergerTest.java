package com.netroaki.chex.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanetOverrideMergerTest {

  @Test
  void mergeAppliesOverridesAndMergesHazards() {
    Set<String> baseHazards = new LinkedHashSet<>();
    baseHazards.add("vacuum");
    baseHazards.add("cryogenic");

    PlanetOverrideMerger.PlanetInfo baseInfo =
        new PlanetOverrideMerger.PlanetInfo(
            "Mars", 2, "chex:suits/suit2", "chex:rp1", "Base description", baseHazards);

    Set<String> overrideHazards = new LinkedHashSet<>();
    overrideHazards.add("Radiation");
    overrideHazards.add("cryogenic");

    PlanetOverridesCore.Entry overrideEntry =
        new PlanetOverridesCore.Entry(
            4, 3, null, "Mars Prime", "Refined description", "chex:lox", overrideHazards);

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(baseInfo, overrideEntry);

    assertEquals("Mars Prime", merged.name());
    assertEquals(4, merged.requiredRocketTier());
    assertEquals("chex:suits/suit3", merged.requiredSuitTag());
    assertEquals("chex:lox", merged.fuel());
    assertEquals("Refined description", merged.description());
    assertIterableEquals(
        List.of("vacuum", "cryogenic", "radiation"), new ArrayList<>(merged.hazards()));
  }

  @Test
  void mergeIgnoresMissingOverrideValues() {
    Set<String> baseHazards = new LinkedHashSet<>();
    baseHazards.add("vacuum");

    PlanetOverrideMerger.PlanetInfo baseInfo =
        new PlanetOverrideMerger.PlanetInfo(
            "Europa", 1, "chex:suits/suit1", "chex:kerosene", "Base description", baseHazards);

    PlanetOverridesCore.Entry overrideEntry =
        new PlanetOverridesCore.Entry(null, null, null, null, null, null, null);

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(baseInfo, overrideEntry);

    assertEquals(baseInfo.name(), merged.name());
    assertEquals(baseInfo.requiredRocketTier(), merged.requiredRocketTier());
    assertEquals(baseInfo.requiredSuitTag(), merged.requiredSuitTag());
    assertEquals(baseInfo.fuel(), merged.fuel());
    assertEquals(baseInfo.description(), merged.description());
    assertEquals(baseInfo.hazards(), merged.hazards());
  }

  @Test
  void mergeTreatsEmptyHazardArrayAsNoChange() {
    Set<String> baseHazards = new LinkedHashSet<>();
    baseHazards.add("vacuum");
    baseHazards.add("radiation");

    PlanetOverrideMerger.PlanetInfo baseInfo =
        new PlanetOverrideMerger.PlanetInfo(
            "Callisto", 2, "chex:suits/suit2", "chex:rp1", "Base description", baseHazards);

    PlanetOverridesCore.Entry overrideEntry =
        new PlanetOverridesCore.Entry(null, null, null, "  ", null, "   ", Set.of());

    PlanetOverrideMerger.PlanetInfo merged = PlanetOverrideMerger.merge(baseInfo, overrideEntry);

    assertEquals(baseInfo.hazards(), merged.hazards());
    assertEquals(baseInfo.fuel(), merged.fuel());
    assertEquals(baseInfo.requiredSuitTag(), merged.requiredSuitTag());
    assertEquals(baseInfo.name(), merged.name());
  }
}
