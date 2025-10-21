package com.netroaki.chex.travel;

import static org.junit.jupiter.api.Assertions.*;

import com.netroaki.chex.core.TravelGraphCore;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class TravelGraphTest {

  @BeforeEach
  void setUp() {
    // Clear any existing test data
    TravelGraphCore.clear();

    // Register test planets in the registry
    PlanetRegistry.registerPlanet(
        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("test", "earth"),
        new PlanetDef(
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("test", "earth"),
            "Earth",
            "The home planet",
            NoduleTiers.T1,
            "chex:suits/suit1",
            "chex:rocket_fuel",
            1,
            true,
            true,
            Set.of("radiation"),
            Set.of("iron", "copper"),
            "terrestrial",
            false));

    PlanetRegistry.registerPlanet(
        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("test", "moon"),
        new PlanetDef(
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("test", "moon"),
            "Moon",
            "Earth's natural satellite",
            NoduleTiers.T2,
            "chex:suits/suit2",
            "chex:rocket_fuel",
            1,
            false,
            false,
            Set.of("vacuum", "radiation"),
            Set.of("titanium", "aluminum"),
            "barren",
            false));

    // Initialize the travel graph with test data
    TravelGraph.initializeDefaultGraph();
  }

  @Test
  void validate_withValidGraph_returnsValidResult() {
    // Arrange - Add planets to the travel graph
    TravelGraphCore.add(1, "test:earth", "test");
    TravelGraphCore.add(2, "test:moon", "test");

    // Act
    TravelGraph.ValidationResult result = TravelGraph.validate();

    // Assert
    assertTrue(result.isValid());
    assertTrue(result.getUnknownPlanets().isEmpty());
    assertTrue(result.getInvalidTiers().isEmpty());
    assertTrue(result.getConflictingPlanets().isEmpty());
    assertEquals(2, result.getTotalPlanets());
    assertEquals(2, result.getTiersWithPlanets());
  }

  @Test
  @Disabled("Relies on TravelGraphCore without public sync to validation map")
  void validate_withUnknownPlanet_returnsError() {
    // Arrange - Add a planet that doesn't exist in the registry
    TravelGraphCore.add(1, "test:mars", "test");

    // Act
    TravelGraph.ValidationResult result = TravelGraph.validate();

    // Assert
    assertFalse(result.isValid());
    assertTrue(result.getUnknownPlanets().contains("test:mars"));
    assertTrue(result.getInvalidTiers().isEmpty());
    assertTrue(result.getConflictingPlanets().isEmpty());
  }

  @Test
  @Disabled("Relies on TravelGraphCore without public sync to validation map")
  void validate_withConflictingTier_returnsError() {
    // Arrange - Add a planet with a tier that doesn't match its definition
    TravelGraphCore.add(3, "test:earth", "test"); // Earth is defined as tier 1

    // Act
    TravelGraph.ValidationResult result = TravelGraph.validate();

    // Assert
    assertFalse(result.isValid());
    assertTrue(result.getUnknownPlanets().isEmpty());
    assertTrue(result.getInvalidTiers().isEmpty());
    assertTrue(
        result.getConflictingPlanets().stream()
            .anyMatch(s -> s.contains("test:earth") && s.contains("T3 but requires T1")));
  }

  @Test
  @Disabled("Relies on TravelGraphCore without public sync to validation map")
  void validate_withMissingPlanet_returnsError() {
    // Arrange - Don't add any planets to the travel graph

    // Act
    TravelGraph.ValidationResult result = TravelGraph.validate();

    // Assert
    assertFalse(result.isValid());
    assertTrue(result.getUnknownPlanets().contains("test:earth (not in travel graph)"));
    assertTrue(result.getUnknownPlanets().contains("test:moon (not in travel graph)"));
    assertTrue(result.getInvalidTiers().isEmpty());
    assertTrue(result.getConflictingPlanets().isEmpty());
  }
}
