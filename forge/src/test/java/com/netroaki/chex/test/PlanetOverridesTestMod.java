package com.netroaki.chex.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.PlanetOverridesCore;
import com.netroaki.chex.registry.NoduleTiers;
import com.netroaki.chex.registry.PlanetDef;
import com.netroaki.chex.registry.PlanetRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mod("chex_test")
public class PlanetOverridesTestMod {
    public PlanetOverridesTestMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        CHEX.LOGGER.info("CHEX Test Mod setup");
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        CHEX.LOGGER.info("=== Starting CHEX Planet Overrides Test ===");
        
        try {
            // Create a test config directory if it doesn't exist
            Path configDir = FMLPaths.CONFIGDIR.get().resolve("chex");
            Files.createDirectories(configDir);
            
            // Create a test planets config file
            Path configFile = configDir.resolve("chex-planets.json5");
            String testConfig = """
            {
                "planets": {
                    "cosmos:earth_moon": {
                        "name": "Luna (Test Override)",
                        "requiredRocketTier": 2,
                        "requiredSuitTag": "chex:suits/suit2",
                        "fuelType": "chex:kerosene",
                        "description": "Test override for Earth's Moon",
                        "hazards": ["vacuum", "radiation"],
                        "availableMinerals": ["iron", "silicon", "titanium"],
                        "biomeType": "lunar_highlands"
                    }
                }
            }
            """;
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile.toFile()))) {
                writer.write(testConfig);
            }
            
            CHEX.LOGGER.info("Test config written to: {}", configFile);
            
            // Load the test config
            Map<ResourceLocation, PlanetOverridesCore.Entry> overrides = PlanetRegistry.loadPlanetOverrides();
            CHEX.LOGGER.info("Loaded {} planet overrides", overrides.size());
            
            // Create a test planet
            PlanetDef moon = new PlanetDef(
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
            
            // Apply overrides
            PlanetDef overriddenMoon = PlanetRegistry.applyOverrides(moon, overrides);
            
            // Log the results
            CHEX.LOGGER.info("=== Test Results ===");
            CHEX.LOGGER.info("Original name: {}", moon.name());
            CHEX.LOGGER.info("Overridden name: {}", overriddenMoon.name());
            CHEX.LOGGER.info("Original tier: {}", moon.requiredRocketTier());
            CHEX.LOGGER.info("Overridden tier: {}", overriddenMoon.requiredRocketTier());
            CHEX.LOGGER.info("Original fuel: {}", moon.fuelType());
            CHEX.LOGGER.info("Overridden fuel: {}", overriddenMoon.fuelType());
            CHEX.LOGGER.info("Original hazards: {}", moon.hazards());
            CHEX.LOGGER.info("Overridden hazards: {}", overriddenMoon.hazards());
            
        } catch (Exception e) {
            CHEX.LOGGER.error("Error in test mod:", e);
        }
    }
}
