package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.CHEXBlocks;
import com.netroaki.chex.worldgen.features.PandoraFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public final class PandoraWorldGen {
    private static final ResourceKey<Biome> PANDORA_PLAINS = registerBiome("pandora_plains");
    private static final ResourceKey<Biome> PANDORA_FOREST = registerBiome("pandora_forest");
    private static final ResourceKey<Biome> PANDORA_MOUNTAINS = registerBiome("pandora_mountains");
    private static final ResourceKey<Biome> PANDORA_OCEAN = registerBiome("pandora_ocean");
    private static final ResourceKey<Biome> PANDORA_HIGHLANDS = registerBiome("pandora_highlands");

    private PandoraWorldGen() {}

    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        
        context.register(PANDORA_PLAINS, createPandoraPlains(placedFeatures));
        context.register(PANDORA_FOREST, createPandoraForest(placedFeatures));
        context.register(PANDORA_MOUNTAINS, createPandoraMountains(placedFeatures));
        context.register(PANDORA_OCEAN, createPandoraOcean(placedFeatures));
        context.register(PANDORA_HIGHLANDS, createPandoraHighlands(placedFeatures));
    }

    private static Biome createPandoraPlains(HolderGetter<PlacedFeature> placedFeatures) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, null);
        addPandoraPlainsFeatures(generation);
        
        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.8f)
            .downfall(0.4f)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .skyColor(0x7BA4FF)
                    .fogColor(0xC0D8FF)
                    .waterColor(0x3F76E4)
                    .waterFogColor(0x1A4F9C)
                    .build()
            )
            .mobSpawnSettings(new MobSpawnSettings.Builder().build())
            .generationSettings(generation.build())
            .build();
    }

    private static void addPandoraPlainsFeatures(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, 
            PandoraFeatures.PANDORA_FUNGI);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, 
            PandoraFeatures.PANDORA_TREES);
    }

    private static Biome createPandoraForest(HolderGetter<PlacedFeature> placedFeatures) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, null);
        addPandoraForestFeatures(generation);
        
        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.7f)
            .downfall(0.8f)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .skyColor(0x7BA4FF)
                    .fogColor(0xC0D8FF)
                    .waterColor(0x3F76E4)
                    .waterFogColor(0x1A4F9C)
                    .build()
            )
            .mobSpawnSettings(new MobSpawnSettings.Builder().build())
            .generationSettings(generation.build())
            .build();
    }

    private static void addPandoraForestFeatures(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, 
            PandoraFeatures.PANDORA_FUNGI);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, 
            PandoraFeatures.PANDORA_TREES);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, 
            PandoraFeatures.PANDORA_TREES); // Double density for forest
    }

    private static Biome createPandoraMountains(HolderGetter<PlacedFeature> placedFeatures) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, null);
        addPandoraMountainsFeatures(generation);
        
        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.5f)
            .downfall(0.3f)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .skyColor(0x7BA4FF)
                    .fogColor(0xC0D8FF)
                    .waterColor(0x3F76E4)
                    .waterFogColor(0x1A4F9C)
                    .build()
            )
            .mobSpawnSettings(new MobSpawnSettings.Builder().build())
            .generationSettings(generation.build())
            .build();
    }

    private static void addPandoraMountainsFeatures(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, 
            PandoraFeatures.PANDORA_MAGMA_FIELDS);
        builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, 
            PandoraFeatures.PANDORA_FLOATING_ISLANDS);
    }

    private static Biome createPandoraOcean(HolderGetter<PlacedFeature> placedFeatures) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, null);
        addPandoraOceanFeatures(generation);
        
        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.7f)
            .downfall(0.9f)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .skyColor(0x7BA4FF)
                    .fogColor(0xC0D8FF)
                    .waterColor(0x3F76E4)
                    .waterFogColor(0x1A4F9C)
                    .build()
            )
            .mobSpawnSettings(new MobSpawnSettings.Builder().build())
            .generationSettings(generation.build())
            .build();
    }

    private static void addPandoraOceanFeatures(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, 
            PandoraFeatures.PANDORA_UNDERWATER_KELP);
    }

    private static Biome createPandoraHighlands(HolderGetter<PlacedFeature> placedFeatures) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, null);
        addPandoraHighlandsFeatures(generation);
        
        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.4f)
            .downfall(0.2f)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .skyColor(0x7BA4FF)
                    .fogColor(0xC0D8FF)
                    .waterColor(0x3F76E4)
                    .waterFogColor(0x1A4F9C)
                    .build()
            )
            .mobSpawnSettings(new MobSpawnSettings.Builder().build())
            .generationSettings(generation.build())
            .build();
    }

    private static void addPandoraHighlandsFeatures(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, 
            PandoraFeatures.PANDORA_MAGMA_FIELDS);
        builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, 
            PandoraFeatures.PANDORA_FLOATING_ISLANDS);
    }

    private static ResourceKey<Biome> registerBiome(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(CHEX.MOD_ID, name));
    }
}
