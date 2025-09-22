package com.netroaki.chex.registry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;

class PlanetDefinitionSystemTest {
    private static final ResourceLocation TEST_ID = new ResourceLocation("test", "test_planet");
    
    @Test
    void testPlanetDefinitionLoading() {
        // Test loading with all fields
        PlanetDef planet = new PlanetDef(
            TEST_ID,
            "Test Planet",
            "A test planet",
            NoduleTiers.TIER_1,
            "test:suit1",
            "minecraft:coal",
            5, // gravity
            true, // hasAtmosphere
            true, // requiresOxygen
            Set.of("vacuum", "radiation"),
            Set.of("minecraft:iron_ore"),
            "desert",
            false, // isOrbit
            300.0f, // temperature
            3,      // radiationLevel
            0.21f   // baseOxygen
        );
        
        // Verify all fields are set correctly
        assertEquals("Test Planet", planet.name());
        assertEquals("A test planet", planet.description());
        assertEquals(NoduleTiers.TIER_1, planet.tier());
        assertEquals("test:suit1", planet.requiredSuitTag());
        assertEquals("minecraft:coal", planet.fuelItem());
        assertEquals(5, planet.gravityLevel());
        assertTrue(planet.hasAtmosphere());
        assertTrue(planet.requiresOxygen());
        assertTrue(planet.hazards().contains("vacuum"));
        assertTrue(planet.hazards().contains("radiation"));
        assertTrue(planet.availableMinerals().contains("minecraft:iron_ore"));
        assertEquals("desert", planet.biomeType());
        assertFalse(planet.isOrbit());
        assertEquals(300.0f, planet.temperature(), 0.001f);
        assertEquals(3, planet.radiationLevel());
        assertEquals(0.21f, planet.baseOxygen(), 0.001f);
    }
    
    @Test
    void testValidationRules() {
        // Test temperature validation
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, -1.0f, 0, 0.0f)
        );
        
        // Test radiation validation
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 0.0f, -1, 0.0f)
        );
        
        // Test oxygen validation
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 0.0f, 0, -0.1f)
        );
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 0.0f, 0, 1.1f)
        );
    }
    
    @Test
    void testHazardValidation() {
        // Test that hazards are properly validated against the allowed set
        assertDoesNotThrow(() ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of("vacuum", "radiation"), Set.of(), "", false, 0.0f, 0, 0.0f)
        );
        
        // Test that invalid hazards are rejected
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of("invalid_hazard"), Set.of(), "", false, 0.0f, 0, 0.0f)
        );
    }
    
    @Test
    void testMineralValidation() {
        // Test that valid mineral formats are accepted
        assertDoesNotThrow(() ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of("minecraft:iron_ore", "mod:ore_tin"), "", false, 0.0f, 0, 0.0f)
        );
        
        // Test that invalid mineral formats are rejected
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of("invalid-format"), "", false, 0.0f, 0, 0.0f)
        );
    }
}
