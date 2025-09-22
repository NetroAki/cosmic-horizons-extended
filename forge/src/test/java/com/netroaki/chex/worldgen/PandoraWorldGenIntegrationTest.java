package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class PandoraWorldGenIntegrationTest {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<String> VERIFIED_FEATURES = new HashSet<>();
    private static final Set<String> MISSING_FEATURES = new HashSet<>();
    private static final Set<String> FOUND_BIOMES = new HashSet<>();
    private static boolean testStarted = false;

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().location().getNamespace().equals(CHEX.MOD_ID)) return;
        
        if (!testStarted) {
            testStarted = true;
            LOGGER.info("=== PANDORA WORLD GENERATION INTEGRATION TEST STARTED ===");
            LOGGER.info("Testing world generation in dimension: {}", level.dimension().location());
        }

        ChunkAccess chunk = event.getChunk();
        BlockPos chunkCenter = new BlockPos(
            chunk.getPos().getMiddleBlockX(),
            64,
            chunk.getPos().getMiddleBlockZ()
        );

        // Check biome at chunk center
        Holder<Biome> biomeHolder = level.getBiome(chunkCenter);
        biomeHolder.unwrapKey().ifPresent(biomeKey -> {
            String biomeId = biomeKey.location().toString();
            if (biomeId.startsWith(CHEX.MOD_ID + ":") && FOUND_BIOMES.add(biomeId)) {
                LOGGER.info("Found Pandora biome: {}", biomeId);
                
                // Check features for this biome
                Biome biome = biomeHolder.value();
                for (GenerationStep.Decoration step : GenerationStep.Decoration.values()) {
                    biome.getGenerationSettings().features().get(step.ordinal()).forEach(feature -> {
                        feature.unwrapKey().ifPresent(key -> {
                            String featureId = key.location().toString();
                            if (featureId.startsWith(CHEX.MOD_ID + ":")) {
                                LOGGER.info("  Found feature in {}: {}", step, featureId);
                                VERIFIED_FEATURES.add(featureId);
                            }
                        });
                    });
                }
            }
        });
    }

    @Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class TestCompletionHandler {
        private static final Set<String> EXPECTED_FEATURES = Set.of(
            "chex:fungal_tower",
            "chex:skybark_tree",
            "chex:pandora_kelp",
            "chex:magma_spire",
            "chex:cloudstone_island"
        );

        @SubscribeEvent
        public static void onServerStopping(net.minecraftforge.event.server.ServerStoppingEvent event) {
            LOGGER.info("=== PANDORA WORLD GENERATION TEST SUMMARY ===");
            LOGGER.info("Found Pandora biomes: {}", FOUND_BIOMES.size());
            FOUND_BIOMES.forEach(biome -> LOGGER.info("  - {}", biome));
            
            LOGGER.info("\nVerified features: {}/{}", VERIFIED_FEATURES.size(), EXPECTED_FEATURES.size());
            VERIFIED_FEATURES.forEach(feature -> LOGGER.info("  ✓ {}", feature));
            
            Set<String> missing = new HashSet<>(EXPECTED_FEATURES);
            missing.removeAll(VERIFIED_FEATURES);
            
            if (!missing.isEmpty()) {
                LOGGER.warn("\nMissing features: {}", missing.size());
                missing.forEach(feature -> LOGGER.warn("  ✗ {}", feature));
            }
            
            LOGGER.info("\nTest completed. All features verified: {}", 
                missing.isEmpty() ? "✓ SUCCESS" : "✗ FAILED");
        }
    }
}
