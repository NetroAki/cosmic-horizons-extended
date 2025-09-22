package com.netroaki.chex.world.biome;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.entity.KeplerEntities;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;

public class KeplerBiomes {
    // Biome Keys
    public static final ResourceKey<Biome> KEPLER_FOREST = registerKey("kepler_forest");
    public static final ResourceKey<Biome> KEPLER_RIVER = registerKey("kepler_river");
    public static final ResourceKey<Biome> KEPLER_PLAINS = registerKey("kepler_plains");
    
    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);
        
        context.register(KEPLER_FOREST, keplerForest(placedFeatures, carvers));
        context.register(KEPLER_RIVER, keplerRiver(placedFeatures, carvers));
        context.register(KEPLER_PLAINS, keplerPlains(placedFeatures, carvers));
    }
    
    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
        
        // Add ores
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, KeplerBiomeFeatures.PLACED_ORE_KEPSILON);
    }
    
    public static Biome keplerForest(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        addKeplerCreatures(spawnBuilder);
        
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        globalOverworldGeneration(biomeBuilder);
        
        // Add biome-specific features
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, 
            KeplerBiomeFeatures.PLACED_KEPLER_FOREST_TREES);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
            VegetationPlacements.FLOWER_FOREST_FLOWERS);
            
        return biome(
            Biome.Precipitation.RAIN,
            0.7f, // Temperature
            0.8f,  // Downfall
            0x3f7e3a, // Grass color
            0x50b83c, // Foliage color
            0x4ec9ff, // Water color
            0x3f76e4, // Water fog color
            0xc0d8ff, // Sky color
            0xc0d8ff, // Fog color
            spawnBuilder,
            biomeBuilder,
            Musics.GAME
        );
    }
    
    public static Biome keplerRiver(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        addKeplerCreatures(spawnBuilder);
        spawnBuilder.addSpawn(MobCategory.WATER_CREATURE, 
            new MobSpawnSettings.SpawnerData(EntityType.SALMON, 5, 1, 5));
        
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        globalOverworldGeneration(biomeBuilder);
        
        // Add river-specific features
        BiomeDefaultFeatures.addDefaultGrass(biomeBuilder);
        
        return biome(
            Biome.Precipitation.RAIN,
            0.5f, // Temperature
            0.9f,  // Downfall
            0x3f7e3a, // Grass color
            0x50b83c, // Foliage color
            0x3f76e4, // Water color
            0x3f76e4, // Water fog color
            0x78a7ff, // Sky color
            0x78a7ff, // Fog color
            spawnBuilder,
            biomeBuilder,
            null
        );
    }
    
    public static Biome keplerPlains(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        addKeplerCreatures(spawnBuilder);
        
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        globalOverworldGeneration(biomeBuilder);
        
        // Add plains-specific features
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
            KeplerBiomeFeatures.PATCH_KEPSILON_GRASS);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
            KeplerBiomeFeatures.PATCH_KEPSILON_FLOWERS);
            
        return biome(
            Biome.Precipitation.RAIN,
            0.8f, // Temperature
            0.4f,  // Downfall
            0x7cc13f, // Grass color
            0x7cc13f, // Foliage color
            0x3f76e4, // Water color
            0x3f76e4, // Water fog color
            0x78a7ff, // Sky color
            0x78a7ff, // Fog color
            spawnBuilder,
            biomeBuilder,
            null
        );
    }
    
    private static void addKeplerCreatures(MobSpawnSettings.Builder builder) {
        // Add spawns for our custom creatures
        builder.addSpawn(MobCategory.CREATURE,
            new MobSpawnSettings.SpawnerData(KeplerEntities.RIVER_GRAZER.get(), 10, 2, 4));
        builder.addSpawn(MobCategory.AMBIENT,
            new MobSpawnSettings.SpawnerData(KeplerEntities.MEADOW_FLUTTERWING.get(), 15, 1, 3));
        builder.addSpawn(MobCategory.MONSTER,
            new MobSpawnSettings.SpawnerData(KeplerEntities.SCRUB_STALKER.get(), 5, 1, 2));
    }
    
    private static ResourceKey<Biome> registerKey(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(CHEX.MOD_ID, name));
    }
    
    private static Biome biome(
        Biome.Precipitation precipitation,
        float temperature, float downfall,
        int grassColor, int foliageColor,
        int waterColor, int waterFogColor,
        int skyColor, int fogColor,
        MobSpawnSettings.Builder spawnBuilder,
        BiomeGenerationSettings.Builder biomeBuilder,
        @Nullable Music backgroundMusic
    ) {
        return new Biome.BiomeBuilder()
            .precipitation(precipitation)
            .temperature(temperature)
            .downfall(downfall)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .grassColorOverride(grassColor)
                    .foliageColorOverride(foliageColor)
                    .waterColor(waterColor)
                    .waterFogColor(waterFogColor)
                    .skyColor(skyColor)
                    .fogColor(fogColor)
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    .backgroundMusic(backgroundMusic)
                    .build()
            )
            .mobSpawnSettings(spawnBuilder.build())
            .generationSettings(biomeBuilder.build())
            .build();
    }
}
