package com.netroaki.chex.validation;

import com.netroaki.chex.core.TravelGraphCore;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TravelGraphValidationTest {
    
    private static final ResourceLocation EARTH = new ResourceLocation("test", "earth");
    private static final ResourceLocation MOON = new ResourceLocation("test", "moon");
    private static final ResourceLocation MARS = new ResourceLocation("test", "mars");
    private static final ResourceLocation PHOBOS = new ResourceLocation("test", "phobos");
    private static final ResourceLocation UNKNOWN_PLANET = new ResourceLocation("test", "unknown");

    @BeforeEach
    void setUp() {
        // Clear any existing data
        PlanetRegistry.clear();
        TravelGraphCore.clear();
        
        // Register test planets
        registerTestPlanet(EARTH, "Earth", NoduleTiers.TIER_1, Set.of(), "earth");
        registerTestPlanet(MOON, "Moon", NoduleTiers.TIER_2, Set.of(EARTH.toString()), "moon");
        registerTestPlanet(MARS, "Mars", NoduleTiers.TIER_3, Set.of(EARTH.toString()), "mars");
        registerTestPlanet(PHOBOS, "Phobos", NoduleTiers.TIER_4, Set.of(MARS.toString()), "phobos");
        
        // Set up travel graph
        TravelGraphCore.add(1, EARTH.toString(), "test");
        TravelGraphCore.add(2, MOON.toString(), "test");
        TravelGraphCore.add(3, MARS.toString(), "test");
        TravelGraphCore.add(4, PHOBOS.toString(), "test");
        
        // Add an unknown planet to the travel graph
        TravelGraphCore.add(5, UNKNOWN_PLANET.toString(), "test");
    }
    
    @AfterEach
    void tearDown() {
        // Clean up after each test
        PlanetRegistry.clear();
        TravelGraphCore.clear();
    }
    
    private void registerTestPlanet(ResourceLocation id, String name, NoduleTiers tier, 
                                  Set<String> requiredPlanets, String biome) {
        PlanetDef planet = new PlanetDef(
            id,
            name,
            "A test planet",
            tier,
            "test:suit" + tier.getTier(),
            "minecraft:coal",
            5, // gravity
            true, // hasAtmosphere
            true, // requiresOxygen
            Set.of(), // hazards
            Set.of("minecraft:iron_ore"), // minerals
            biome,
            false, // isOrbit
            300.0f, // temperature
            3,      // radiationLevel
            0.21f,  // baseOxygen
            requiredPlanets // required planets
        );
        PlanetRegistry.registerPlanet(id, planet);
    }
    
    @Test
    void testValidate_WithValidGraph() {
        // Remove the unknown planet to make the graph valid
        TravelGraphCore.clear();
        TravelGraphCore.add(1, EARTH.toString(), "test");
        TravelGraphCore.add(2, MOON.toString(), "test");
        TravelGraphCore.add(3, MARS.toString(), "test");
        TravelGraphCore.add(4, PHOBOS.toString(), "test");
        
        List<String> issues = TravelGraphCore.validate();
        
        // Should only have the success message
        assertEquals(1, issues.size());
        assertTrue(issues.get(0).contains("✅ Travel graph validation passed with no issues!"));
    }
    
    @Test
    void testValidate_WithUnknownPlanet() {
        List<String> issues = TravelGraphCore.validate();
        
        // Should find the unknown planet
        assertTrue(issues.stream()
            .anyMatch(issue -> issue.contains("unknown planet IDs in travel graph") && 
                             issue.contains(UNKNOWN_PLANET.toString())));
    }
    
    @Test
    void testValidate_WithMissingPlanet() {
        // Add a planet to the registry but not to the travel graph
        ResourceLocation newPlanet = new ResourceLocation("test", "new_planet");
        registerTestPlanet(newPlanet, "New Planet", NoduleTiers.TIER_3, Set.of(EARTH.toString()), "plains");
        
        List<String> issues = TravelGraphCore.validate();
        
        // Should find the missing planet
        assertTrue(issues.stream()
            .anyMatch(issue -> issue.contains("planets missing from travel graph") && 
                             issue.contains(newPlanet.toString())));
    }
    
    @Test
    void testValidate_WithTierMismatch() {
        // Change a planet's tier in the registry but not in the travel graph
        PlanetDef mars = PlanetRegistry.getPlanet(MARS).orElseThrow();
        PlanetDef updatedMars = new PlanetDef(
            mars.id(),
            mars.name(),
            mars.description(),
            NoduleTiers.TIER_5, // Changed from TIER_3
            mars.suitTag(),
            mars.fuelType(),
            mars.gravity(),
            mars.hasAtmosphere(),
            mars.requiresOxygen(),
            mars.hazards(),
            mars.minerals(),
            mars.biomeType(),
            mars.isOrbit(),
            mars.temperature(),
            mars.radiationLevel(),
            mars.baseOxygen(),
            mars.requiredPlanets()
        );
        PlanetRegistry.registerPlanet(MARS, updatedMars);
        
        List<String> issues = TravelGraphCore.validate();
        
        // Should find the tier mismatch
        assertTrue(issues.stream()
            .anyMatch(issue -> issue.contains("tier mismatches") && 
                             issue.contains(MARS.toString())));
    }
    
    @Test
    void testValidate_WithUnreachablePlanet() {
        // Make Phobos require a higher tier planet than it's in
        PlanetDef phobos = PlanetRegistry.getPlanet(PHOBOS).orElseThrow();
        PlanetDef updatedPhobos = new PlanetDef(
            phobos.id(),
            phobos.name(),
            phobos.description(),
            phobos.requiredRocketTier(),
            phobos.suitTag(),
            phobos.fuelType(),
            phobos.gravity(),
            phobos.hasAtmosphere(),
            phobos.requiresOxygen(),
            phobos.hazards(),
            phobos.minerals(),
            phobos.biomeType(),
            phobos.isOrbit(),
            phobos.temperature(),
            phobos.radiationLevel(),
            phobos.baseOxygen(),
            Set.of(EARTH.toString(), UNKNOWN_PLANET.toString()) // Add unknown planet as requirement
        );
        PlanetRegistry.registerPlanet(PHOBOS, updatedPhobos);
        
        List<String> issues = TravelGraphCore.validate();
        
        // Should find the unreachable planet
        assertTrue(issues.stream()
            .anyMatch(issue -> issue.contains("unreachable planets") && 
                             issue.contains(PHOBOS.toString()) &&
                             issue.contains(UNKNOWN_PLANET.toString())));
    }
    
    @Test
    void testValidate_WithEmptyTiers() {
        // Clear some tiers to create gaps
        TravelGraphCore.clear();
        TravelGraphCore.add(1, EARTH.toString(), "test");
        TravelGraphCore.add(4, PHOBOS.toString(), "test"); // Skip tiers 2 and 3
        
        List<String> issues = TravelGraphCore.validate();
        
        // Should find the empty tiers
        assertTrue(issues.stream()
            .anyMatch(issue -> issue.contains("empty tiers") && 
                             (issue.contains("2") || issue.contains("3"))));
    }
}
