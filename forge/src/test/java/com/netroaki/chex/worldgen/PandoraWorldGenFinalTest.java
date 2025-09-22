package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.worldgen.features.PandoraFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class PandoraWorldGenFinalTest {
    private static final Logger LOGGER = LogManager.getLogger("PandoraWorldGenTest");
    private static final Set<String> VERIFIED_FEATURES = new HashSet<>();
    private static final Set<String> FOUND_BIOMES = new HashSet<>();

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        LOGGER.info("=== PANDORA WORLD GENERATION FINAL TEST STARTED ===");
        LOGGER.info("Server started, checking Pandora dimension...");
        
        MinecraftServer server = event.getServer();
        ServerLevel pandoraLevel = server.getLevel(CHEX.PANDORA_LEVEL);
        
        if (pandoraLevel == null) {
            LOGGER.error("Pandora dimension not found! Make sure it's registered correctly.");
            return;
        }
        
        LOGGER.info("Pandora dimension found, checking features...");
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().location().equals(CHEX.PANDORA_LEVEL.location())) return;
        
        ChunkAccess chunk = event.getChunk();
        
        // Check biomes in this chunk
        chunk.getBiomes().getUsedPositions().forEach(pos -> {
            Holder<Biome> biomeHolder = level.getBiome(pos);
            biomeHolder.unwrapKey().ifPresent(biomeKey -> {
                String biomeId = biomeKey.location().toString();
                if (biomeId.startsWith(CHEX.MOD_ID + ":") && FOUND_BIOMES.add(biomeId)) {
                    LOGGER.info("Found Pandora biome: {}", biomeId);
                    
                    // Log features for this biome
                    Biome biome = biomeHolder.value();
                    for (GenerationStep.Decoration step : GenerationStep.Decoration.values()) {
                        biome.getGenerationSettings().features().get(step.ordinal()).forEach(feature -> {
                            feature.unwrapKey().ifPresent(key -> {
                                String featureId = key.location().toString();
                                if (featureId.startsWith(CHEX.MOD_ID + ":") && VERIFIED_FEATURES.add(featureId)) {
                                    LOGGER.info("  Feature in {}: {}", step, featureId);
                                }
                            });
                        });
                    }
                }
            });
        });
        
        // Check if we've found all expected features
        checkFeatureCompleteness();
    }
    
    private static void checkFeatureCompleteness() {
        String[] expectedFeatures = {
            "chex:fungal_tower",
            "chex:skybark_tree",
            "chex:pandora_kelp",
            "chex:magma_spire",
            "chex:cloudstone_island"
        };
        
        boolean allFound = true;
        for (String feature : expectedFeatures) {
            if (!VERIFIED_FEATURES.contains(feature)) {
                LOGGER.warn("Missing feature: {}", feature);
                allFound = false;
            }
        }
        
        if (allFound) {
            LOGGER.info("=== ALL PANDORA FEATURES VERIFIED ===");
            LOGGER.info("Found {} biomes and {} features", 
                FOUND_BIOMES.size(), VERIFIED_FEATURES.size());
        }
    }
    
    public static void bootstrapTest(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // Verify all configured features are registered
        verifyFeature(context, PandoraFeatures.FUNGAL_TOWER, "Fungal Tower");
        verifyFeature(context, PandoraFeatures.SKYBARK_TREE, "Skybark Tree");
        verifyFeature(context, PandoraFeatures.PANDORA_KELP, "Pandora Kelp");
        verifyFeature(context, PandoraFeatures.MAGMA_SPIRE, "Magma Spire");
        verifyFeature(context, PandoraFeatures.CLOUDSTONE_ISLAND, "Cloudstone Island");
    }
    
    private static void verifyFeature(BootstapContext<ConfiguredFeature<?, ?>> context, 
                                    ResourceKey<ConfiguredFeature<?, ?>> key, String name) {
        try {
            ConfiguredFeature<?, ?> feature = context.lookup(Registries.CONFIGURED_FEATURE)
                .getOrThrow(key);
            LOGGER.info("Verified configured feature: {}", name);
        } catch (Exception e) {
            LOGGER.error("Failed to verify feature {}: {}", name, e.getMessage());
        }
    }
}
