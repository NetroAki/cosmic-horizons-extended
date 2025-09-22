package com.netroaki.chex.config;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PlanetOverridesCoreTest {

    @Test
    void testDeserializeWithNewFields() {
        String json = """
            {
              "test:test_planet": {
                "name": "Test Planet",
                "requiredRocketTier": 2,
                "requiredSuitTag": "test:suit2",
                "temperature": 350.5,
                "radiationLevel": 4,
                "baseOxygen": 0.18,
                "hazards": ["heat", "sandstorms"],
                "availableMinerals": ["minecraft:iron_ore"]
              }
            }
            """;

        var overrides = PlanetOverridesCore.loadFromJson(json);
        assertTrue(overrides.isPresent());
        var entry = overrides.get().get(new ResourceLocation("test", "test_planet"));
        assertNotNull(entry);
        
        assertEquals(350.5f, entry.temperature().orElse(0f), 0.001f);
        assertEquals(4, entry.radiationLevel().orElse(0));
        assertEquals(0.18f, entry.baseOxygen().orElse(0f), 0.001f);
        assertTrue(entry.hazards().contains("heat"));
        assertTrue(entry.hazards().contains("sandstorms"));
        assertTrue(entry.availableMinerals().contains("minecraft:iron_ore"));
    }

    @Test
    void testSerializeWithNewFields(@TempDir Path tempDir) throws Exception {
        var overrides = new java.util.HashMap<ResourceLocation, PlanetOverridesCore.Entry>();
        overrides.put(
            new ResourceLocation("test", "test_planet"),
            new PlanetOverridesCore.Entry(
                2, // requiredRocketTier
                null, // requiredSuitTier (deprecated)
                "test:suit2", // requiredSuitTag
                "Test Planet", // name
                "A test planet", // description
                "minecraft:coal", // fuel
                Set.of("heat", "sandstorms"), // hazards
                350.5f, // temperature
                4, // radiationLevel
                0.18f, // baseOxygen
                Set.of("minecraft:iron_ore") // availableMinerals
            )
        );

        Path tempFile = tempDir.resolve("test_overrides.json");
        PlanetOverridesCore.save(overrides, tempFile);
        
        String json = Files.readString(tempFile);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject()
            .getAsJsonObject("test:test_planet");
            
        assertEquals(350.5, jsonObject.get("temperature").getAsDouble(), 0.001);
        assertEquals(4, jsonObject.get("radiationLevel").getAsInt());
        assertEquals(0.18, jsonObject.get("baseOxygen").getAsDouble(), 0.001);
        assertTrue(jsonObject.get("hazards").getAsJsonArray().toString().contains("heat"));
        assertTrue(jsonObject.get("availableMinerals").getAsJsonArray().toString().contains("iron_ore"));
    }

    @Test
    void testBackwardCompatibility() {
        // Test that old format still works
        String json = """
            {
              "test:old_planet": {
                "name": "Old Planet",
                "requiredRocketTier": 1,
                "hazards": ["cold"]
              }
            }
            """;

        var overrides = PlanetOverridesCore.loadFromJson(json);
        assertTrue(overrides.isPresent());
        var entry = overrides.get().get(new ResourceLocation("test", "old_planet"));
        assertNotNull(entry);
        
        // New fields should be empty
        assertTrue(entry.temperature().isEmpty());
        assertTrue(entry.radiationLevel().isEmpty());
        assertTrue(entry.baseOxygen().isEmpty());
        
        // Old fields should work as before
        assertEquals("Old Planet", entry.name());
        assertTrue(entry.hazards().contains("cold"));
    }
}
