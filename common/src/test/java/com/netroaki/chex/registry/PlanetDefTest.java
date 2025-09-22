package com.netroaki.chex.registry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import net.minecraft.resources.ResourceLocation;

class PlanetDefTest {
    private static final ResourceLocation TEST_ID = new ResourceLocation("test", "test_planet");
    
    @Test
    void testValidPlanetDef() {
        // Test valid construction
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
        
        assertEquals("Test Planet", planet.name());
        assertEquals(5, planet.gravityLevel());
        assertEquals(300.0f, planet.temperature(), 0.001f);
        assertEquals(3, planet.radiationLevel());
        assertEquals(0.21f, planet.baseOxygen(), 0.001f);
    }
    
    @Test
    void testTemperatureValidation() {
        // Test temperature validation
        assertThrows(IllegalArgumentException.class, () -> 
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, -1.0f, 0, 0.0f)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 1001.0f, 0, 0.0f)
        );
    }
    
    @Test
    void testRadiationValidation() {
        // Test radiation level validation
        assertThrows(IllegalArgumentException.class, () -> 
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 300.0f, -1, 0.0f)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 300.0f, 11, 0.0f)
        );
    }
    
    @Test
    void testOxygenValidation() {
        // Test base oxygen validation
        assertThrows(IllegalArgumentException.class, () -> 
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 300.0f, 5, -0.1f)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 300.0f, 5, 1.1f)
        );
    }
    
    @Test
    void testEnvironmentalChecks() {
        PlanetDef habitable = new PlanetDef(TEST_ID, "Habitable", "", NoduleTiers.TIER_1, 
            "", "", 5, true, true, Set.of(), Set.of(), "temperate", false, 
            300.0f, 2, 0.21f);
            
        PlanetDef hostile = new PlanetDef(TEST_ID, "Hostile", "", NoduleTiers.TIER_1, 
            "", "", 5, true, true, Set.of("toxic"), Set.of(), "volcanic", false, 
            500.0f, 8, 0.05f);
        
        assertTrue(habitable.isHabitable());
        assertFalse(hostile.isHabitable());
        assertTrue(hostile.hasHazardousConditions());
        assertEquals("Moderate", habitable.getRadiationLevelDescription());
        assertEquals("Extreme", hostile.getRadiationLevelDescription());
    }
}
