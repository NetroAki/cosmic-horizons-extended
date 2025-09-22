package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PandoraWorldGenTest {
    private static final Logger LOGGER = LogManager.getLogger();

    // Expected feature IDs
    private static final Set<String> EXPECTED_FEATURES = Set.of(
        "chex:fungal_tower",
        "chex:skybark_tree",
        "chex:pandora_kelp",
        "chex:magma_spire",
        "chex:cloudstone_island"
    );

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(Registries.CONFIGURED_FEATURE, helper -> {
            LOGGER.info("=== PANDORA WORLD GENERATION TEST STARTED ===");
            LOGGER.info("Verifying configured features...");
            
            Registry<ConfiguredFeature<?, ?>> featureRegistry = event.getRegistry(Registries.CONFIGURED_FEATURE);
            if (featureRegistry == null) {
                LOGGER.error("Failed to get feature registry!");
                return;
            }

            // Check if all expected features are registered
            Set<String> missingFeatures = EXPECTED_FEATURES.stream()
                .filter(id -> !featureRegistry.containsKey(new ResourceLocation(id)))
                .collect(Collectors.toSet());

            if (!missingFeatures.isEmpty()) {
                LOGGER.error("Missing configured features: {}", String.join(", ", missingFeatures));
            } else {
                LOGGER.info("All expected configured features are registered!");
            }
        });

        event.register(Registries.PLACED_FEATURE, helper -> {
            Registry<PlacedFeature> placedFeatureRegistry = event.getRegistry(Registries.PLACED_FEATURE);
            if (placedFeatureRegistry == null) {
                LOGGER.error("Failed to get placed feature registry!");
                return;
            }

            LOGGER.info("Verifying placed features...");
            Set<String> placedFeatureIds = placedFeatureRegistry.entrySet().stream()
                .map(entry -> entry.getKey().location().toString())
                .filter(id -> id.startsWith("chex:"))
                .collect(Collectors.toSet());

            LOGGER.info("Found placed features: {}", String.join(", ", placedFeatureIds));
        });

        event.register(Registries.BIOME, helper -> {
            Registry<Biome> biomeRegistry = event.getRegistry(Registries.BIOME);
            if (biomeRegistry == null) {
                LOGGER.error("Failed to get biome registry!");
                return;
            }

            LOGGER.info("Verifying Pandora biomes...");
            List<String> pandoraBiomes = biomeRegistry.entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(CHEX.MOD_ID))
                .map(entry -> entry.getKey().location().toString())
                .toList();

            LOGGER.info("Found Pandora biomes: {}", String.join(", ", pandoraBiomes));
            
            // Verify biome feature steps
            for (var entry : biomeRegistry.entrySet()) {
                ResourceLocation biomeId = entry.getKey().location();
                if (!biomeId.getNamespace().equals(CHEX.MOD_ID)) continue;
                
                Biome biome = entry.getValue();
                LOGGER.info("Checking features for biome: {}", biomeId);
                
                for (GenerationStep.Decoration step : GenerationStep.Decoration.values()) {
                    List<Holder<PlacedFeature>> features = biome.getGenerationSettings().features().get(step.ordinal());
                    if (features != null && !features.isEmpty()) {
                        LOGGER.info("  {}: {} features", step, features.size());
                        for (Holder<PlacedFeature> feature : features) {
                            feature.unwrapKey().ifPresent(key -> 
                                LOGGER.info("    - {}", key.location())
                            );
                        }
                    }
                }
            }
            
            LOGGER.info("=== PANDORA WORLD GENERATION TEST COMPLETED ===");
        });
    }
}
