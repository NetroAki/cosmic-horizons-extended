package com.netroaki.chex.validation;

import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetDef;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlanetValidationTest {
    private static final ResourceLocation TEST_ID = new ResourceLocation("test", "test_planet");

    @Test
    void testValidPlanetDefinition() {
        // Test a valid planet definition
        assertDoesNotThrow(() -> new PlanetDef(
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
        ));
    }

    @ParameterizedTest
    @ValueSource(floats = {-1.0f, 1001.0f})
    void testInvalidTemperature(float temperature) {
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, temperature, 0, 0.0f)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 11})
    void testInvalidRadiationLevel(int radiationLevel) {
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 0.0f, radiationLevel, 0.0f)
        );
    }

    @ParameterizedTest
    @ValueSource(floats = {-0.1f, 1.1f})
    void testInvalidBaseOxygen(float baseOxygen) {
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 0.0f, 0, baseOxygen)
        );
    }

    @Test
    void testInvalidHazard() {
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of("invalid_hazard"), Set.of(), "", false, 0.0f, 0, 0.0f)
        );
    }

    @Test
    void testInvalidMineralFormat() {
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of("invalid-format"), "", false, 0.0f, 0, 0.0f)
        );
    }

    @Test
    void testNullId() {
        assertThrows(NullPointerException.class, () ->
            new PlanetDef(null, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 0.0f, 0, 0.0f)
        );
    }

    @Test
    void testEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->
            new PlanetDef(TEST_ID, "", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), Set.of(), "", false, 0.0f, 0, 0.0f)
        );
    }

    @Test
    void testNullHazards() {
        assertDoesNotThrow(() ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, null, Set.of(), "", false, 0.0f, 0, 0.0f)
        );
    }

    @Test
    void testNullMinerals() {
        assertDoesNotThrow(() ->
            new PlanetDef(TEST_ID, "Test", "", NoduleTiers.TIER_1, "", "", 5, 
                true, true, Set.of(), null, "", false, 0.0f, 0, 0.0f)
        );
    }
}
