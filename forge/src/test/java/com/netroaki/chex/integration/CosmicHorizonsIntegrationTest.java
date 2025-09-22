package com.netroaki.chex.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetDef;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class CosmicHorizonsIntegrationTest {

    @Mock
    private CosmicHorizonsIntegration integration;

    @BeforeEach
    void setUp() {
        // Setup mock behavior for protected/private methods if needed
    }

    @Test
    void testCreatePlanetDefForDesertBiome() {
        ResourceLocation dimensionId = new ResourceLocation("test", "desert_planet");
        
        // Mock the static methods
        try (var mocked = mockStatic(CosmicHorizonsIntegration.class)) {
            // Setup mocks for helper methods
            when(CosmicHorizonsIntegration.getPlanetNameFromDimensionId(dimensionId)).thenReturn("Desert Planet");
            when(CosmicHorizonsIntegration.getPlanetDescriptionFromDimensionId(dimensionId)).thenReturn("A hot, arid world");
            when(CosmicHorizonsIntegration.getRequiredTierFromDimensionId(dimensionId)).thenReturn(NoduleTiers.TIER_2);
            when(CosmicHorizonsIntegration.getSuitTagFromDimensionId(dimensionId)).thenReturn("test:suit2");
            when(CosmicHorizonsIntegration.getMineralsFromDimensionId(dimensionId)).thenReturn(Set.of("minecraft:sandstone"));
            when(CosmicHorizonsIntegration.getBiomeTypeFromDimensionId(dimensionId)).thenReturn("desert");
            when(CosmicHorizonsIntegration.getDefaultHazardsForBiome("desert")).thenReturn(Set.of("heat"));
            when(CosmicHorizonsIntegration.determineDefaultFuel("desert")).thenReturn("minecraft:coal");
            when(CosmicHorizonsIntegration.calculateGravityLevel("desert")).thenReturn(3);
            
            // Call the actual method
            PlanetDef planet = CosmicHorizonsIntegration.createPlanetDefForDiscoveredPlanet(dimensionId);
            
            // Verify the results
            assertNotNull(planet);
            assertEquals("Desert Planet", planet.name());
            assertEquals(NoduleTiers.TIER_2, planet.requiredRocketTier());
            assertTrue(planet.hazards().contains("heat"));
            
            // Verify temperature is within expected range for desert (320-400K)
            assertTrue(planet.temperature() >= 320 && planet.temperature() <= 400);
            
            // Verify radiation level is within expected range for desert (4-7)
            assertTrue(planet.radiationLevel() >= 4 && planet.radiationLevel() <= 7);
            
            // Verify base oxygen is within expected range for desert (0.1-0.2)
            assertTrue(planet.baseOxygen() >= 0.1f && planet.baseOxygen() <= 0.2f);
        }
    }

    @Test
    void testCalculateTemperatureByBiome() {
        // Test temperature ranges for different biomes
        testTemperatureRange("volcanic", 500, 700);
        testTemperatureRange("desert", 320, 400);
        testTemperatureRange("jungle", 300, 340);
        testTemperatureRange("temperate", 280, 320);
        testTemperatureRange("snow", 200, 250);
        testTemperatureRange("airless", 100, 200);
    }
    
    private void testTemperatureRange(String biomeType, float min, float max) {
        float temp = CosmicHorizonsIntegration.calculateTemperature(biomeType);
        assertTrue(temp >= min && temp <= max, 
            String.format("Temperature for %s should be between %.1f and %.1f, but was %.1f", 
                biomeType, min, max, temp));
    }
    
    @Test
    void testCalculateRadiationByBiome() {
        // Test radiation levels for different biomes
        testRadiationRange("volcanic", 7, 10);
        testRadiationRange("desert", 4, 7);
        testRadiationRange("temperate", 1, 2);
        testRadiationRange("snow", 1, 1);
        testRadiationRange("airless", 8, 10);
    }
    
    private void testRadiationRange(String biomeType, int min, int max) {
        int level = CosmicHorizonsIntegration.calculateRadiationLevel(biomeType);
        assertTrue(level >= min && level <= max, 
            String.format("Radiation level for %s should be between %d and %d, but was %d", 
                biomeType, min, max, level));
    }
    
    @Test
    void testCalculateOxygenByBiome() {
        // Test oxygen levels for different biomes
        testOxygenRange("jungle", 0.25f, 0.4f);
        testOxygenRange("temperate", 0.2f, 0.3f);
        testOxygenRange("desert", 0.1f, 0.2f);
        testOxygenRange("volcanic", 0.05f, 0.15f);
        testOxygenRange("airless", 0.0f, 0.0f);
    }
    
    private void testOxygenRange(String biomeType, float min, float max) {
        float oxygen = CosmicHorizonsIntegration.calculateBaseOxygen(biomeType);
        assertTrue(oxygen >= min && oxygen <= max, 
            String.format("Oxygen level for %s should be between %.2f and %.2f, but was %.2f", 
                biomeType, min, max, oxygen));
    }
}
