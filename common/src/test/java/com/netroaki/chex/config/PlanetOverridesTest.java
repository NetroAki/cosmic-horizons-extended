package com.netroaki.chex.config;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetDef;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PlanetOverridesTest {
    @TempDir
    static Path tempDir;
    
    private Path configFile;
    private PlanetOverridesCore overridesCore;
    
    @BeforeEach
    void setUp() throws Exception {
        configFile = tempDir.resolve("chex-planets.json5");
        overridesCore = new PlanetOverridesCore();
    }
    
    @Test
    void testLoadPlanetOverrides() throws Exception {
        // Create a test config file
        String configContent = """
        {
            "planets": {
                "cosmos:earth_moon": {
                    "name": "Luna",
                    "requiredRocketTier": 2,
                    "requiredSuitTag": "chex:suits/suit2",
                    "fuelType": "chex:kerosene",
                    "description": "Earth's only natural satellite, now with overrides!",
                    "hazards": ["vacuum", "radiation"],
                    "availableMinerals": ["iron", "silicon", "titanium"],
                    "biomeType": "lunar_highlands",
                    "gravity": 1,
                    "hasAtmosphere": false,
                    "requiresOxygen": true,
                    "isOrbit": false,
                    "temperature": 250.5,
                    "radiationLevel": 2,
                    "baseOxygen": 0.1
                }
            }
        }
        """;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile.toFile()))) {
            writer.write(configContent);
        }
        
        // Load the overrides
        Optional<Map<String, PlanetOverridesCore.Entry>> result = PlanetOverridesCore.load(configFile);
        assertTrue(result.isPresent(), "Should load overrides successfully");
        
        Map<String, PlanetOverridesCore.Entry> overrides = result.get();
        assertEquals(1, overrides.size(), "Should load one planet override");
        
        PlanetOverridesCore.Entry moonOverride = overrides.get("cosmos:earth_moon");
        assertNotNull(moonOverride, "Should find moon override");
        
        // Verify all fields are loaded correctly
        assertEquals("Luna", moonOverride.name);
        assertEquals(2, moonOverride.requiredRocketTier);
        assertEquals("chex:suits/suit2", moonOverride.requiredSuitTag);
        assertEquals("chex:kerosene", moonOverride.fuelType);
        assertEquals("Earth's only natural satellite, now with overrides!", moonOverride.description);
        assertTrue(moonOverride.hazards.containsAll(Set.of("vacuum", "radiation")));
        assertTrue(moonOverride.availableMinerals.containsAll(Set.of("iron", "silicon", "titanium")));
        assertEquals("lunar_highlands", moonOverride.biomeType);
        assertEquals(1, moonOverride.gravity);
        assertFalse(moonOverride.hasAtmosphere);
        assertTrue(moonOverride.requiresOxygen);
        assertFalse(moonOverride.isOrbit);
        assertEquals(250.5f, moonOverride.temperature);
        assertEquals(2, moonOverride.radiationLevel);
        assertEquals(0.1f, moonOverride.baseOxygen);
    }
    
    @Test
    void testApplyOverrides() {
        // Create a base planet definition
        PlanetDef basePlanet = new PlanetDef(
            ResourceLocation.fromNamespaceAndPath("cosmos", "earth_moon"),
            "Earth Moon",
            "The natural satellite of Earth",
            NoduleTiers.T1,
            "chex:suits/suit1",
            "minecraft:lava",
            1,
            false,
            true,
            Set.of("vacuum"),
            Set.of("iron", "silicon"),
            "lunar",
            false
        );
        
        // Create overrides
        PlanetOverridesCore.Entry override = new PlanetOverridesCore.Entry(
            2,                              // requiredRocketTier
            "chex:suits/suit2",            // requiredSuitTag
            "Luna",                         // name
            "chex:kerosene",                // fuelType
            "Earth's only natural satellite, now with overrides!", // description
            Set.of("vacuum", "radiation"),  // hazards
            Set.of("iron", "silicon", "titanium"), // availableMinerals
            "lunar_highlands",              // biomeType
            1,                              // gravity
            false,                          // hasAtmosphere
            true,                           // requiresOxygen
            false,                          // isOrbit
            250.5f,                         // temperature
            2,                              // radiationLevel
            0.1f                            // baseOxygen
        );
        
        // Create a map of overrides
        Map<ResourceLocation, PlanetOverridesCore.Entry> overrides = new HashMap<>();
        overrides.put(basePlanet.id(), override);
        
        // Apply overrides
        PlanetDef result = PlanetRegistry.applyOverrides(basePlanet, overrides);
        
        // Verify the result
        assertNotNull(result, "Result should not be null");
        assertEquals("Luna", result.name(), "Name should be overridden");
        assertEquals(NoduleTiers.T2, result.requiredRocketTier(), "Rocket tier should be overridden");
        assertEquals("chex:suits/suit2", result.requiredSuitTag(), "Suit tag should be overridden");
        assertEquals("chex:kerosene", result.fuelType(), "Fuel type should be overridden");
        assertEquals("Earth's only natural satellite, now with overrides!", result.description(), "Description should be overridden");
        assertTrue(result.hazards().containsAll(Set.of("vacuum", "radiation")), "Hazards should be overridden");
        assertTrue(result.availableMinerals().containsAll(Set.of("iron", "silicon", "titanium")), "Minerals should be overridden");
        assertEquals("lunar_highlands", result.biomeType(), "Biome type should be overridden");
        assertEquals(1, result.gravityLevel(), "Gravity should be overridden");
        assertFalse(result.hasAtmosphere(), "Has atmosphere should be overridden");
        assertTrue(result.requiresOxygen(), "Requires oxygen should be overridden");
        assertFalse(result.isOrbit(), "Is orbit should be overridden");
    }
    
    @Test
    void testMergeWithPartialOverrides() {
        // Create a base planet definition
        PlanetDef basePlanet = new PlanetDef(
            ResourceLocation.fromNamespaceAndPath("cosmos", "earth_moon"),
            "Earth Moon",
            "The natural satellite of Earth",
            NoduleTiers.T1,
            "chex:suits/suit1",
            "minecraft:lava",
            1,
            false,
            true,
            Set.of("vacuum"),
            Set.of("iron", "silicon"),
            "lunar",
            false
        );
        
        // Create partial overrides (only some fields)
        Map<String, Object> overrideData = new HashMap<>();
        overrideData.put("name", "Luna");
        overrideData.put("requiredRocketTier", 2);
        overrideData.put("hazards", Arrays.asList("vacuum", "radiation"));
        
        // Convert to JSON and back to simulate loading from file
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(overrideData);
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        
        // Create overrides map
        Map<ResourceLocation, PlanetOverridesCore.Entry> overrides = new HashMap<>();
        overrides.put(
            basePlanet.id(),
            new PlanetOverridesCore.Entry(
                obj.has("requiredRocketTier") ? obj.get("requiredRocketTier").getAsInt() : 0,
                obj.has("requiredSuitTag") ? obj.get("requiredSuitTag").getAsString() : "",
                obj.has("name") ? obj.get("name").getAsString() : "",
                obj.has("fuelType") ? obj.get("fuelType").getAsString() : "",
                obj.has("description") ? obj.get("description").getAsString() : "",
                obj.has("hazards") ? parseStringSet(obj.getAsJsonArray("hazards")) : null,
                obj.has("availableMinerals") ? parseStringSet(obj.getAsJsonArray("availableMinerals")) : null,
                obj.has("biomeType") ? obj.get("biomeType").getAsString() : "",
                obj.has("gravity") ? obj.get("gravity").getAsInt() : null,
                obj.has("hasAtmosphere") ? obj.get("hasAtmosphere").getAsBoolean() : null,
                obj.has("requiresOxygen") ? obj.get("requiresOxygen").getAsBoolean() : null,
                obj.has("isOrbit") ? obj.get("isOrbit").getAsBoolean() : null,
                obj.has("temperature") ? obj.get("temperature").getAsFloat() : null,
                obj.has("radiationLevel") ? obj.get("radiationLevel").getAsInt() : null,
                obj.has("baseOxygen") ? obj.get("baseOxygen").getAsFloat() : null
            )
        );
        
        // Apply overrides
        PlanetDef result = PlanetRegistry.applyOverrides(basePlanet, overrides);
        
        // Verify the result
        assertNotNull(result, "Result should not be null");
        assertEquals("Luna", result.name(), "Name should be overridden");
        assertEquals(NoduleTiers.T2, result.requiredRocketTier(), "Rocket tier should be overridden");
        assertEquals("chex:suits/suit1", result.requiredSuitTag(), "Suit tag should remain unchanged");
        assertEquals("minecraft:lava", result.fuelType(), "Fuel type should remain unchanged");
        assertEquals("The natural satellite of Earth", result.description(), "Description should remain unchanged");
        assertTrue(result.hazards().containsAll(Set.of("vacuum", "radiation")), "Hazards should be overridden");
        assertEquals(Set.of("iron", "silicon"), result.availableMinerals(), "Minerals should remain unchanged");
        assertEquals("lunar", result.biomeType(), "Biome type should remain unchanged");
        assertEquals(1, result.gravityLevel(), "Gravity should remain unchanged");
        assertFalse(result.hasAtmosphere(), "Has atmosphere should remain unchanged");
        assertTrue(result.requiresOxygen(), "Requires oxygen should remain unchanged");
        assertFalse(result.isOrbit(), "Is orbit should remain unchanged");
    }
    
    // Helper method to parse JSON array to Set<String>
    private static Set<String> parseStringSet(com.google.gson.JsonArray jsonArray) {
        Set<String> result = new HashSet<>();
        for (com.google.gson.JsonElement element : jsonArray) {
            result.add(element.getAsString());
        }
        return result;
    }
}
