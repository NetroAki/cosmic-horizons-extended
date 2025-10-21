package com.netroaki.chex.world.library;

import com.netroaki.chex.CHEX;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class LibraryDimensionProvider {
  public static final ResourceKey<NoiseGeneratorSettings> LIBRARY_NOISE_SETTINGS =
      ResourceKey.create(
          Registries.NOISE_SETTINGS, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));

  public static LevelStem createDimension(
      Registry<DimensionType> dimensionTypeRegistry,
      Registry<Biome> biomeRegistry,
      Registry<StructureSet> structureSetRegistry,
      Registry<NoiseGeneratorSettings> noiseSettingsRegistry,
      long seed) {
    return new LevelStem(
        dimensionTypeRegistry.getOrCreateHolderOrThrow(LibraryDimension.LIBRARY_DIM_TYPE),
        createChunkGenerator(biomeRegistry, structureSetRegistry, noiseSettingsRegistry, seed));
  }

  private static ChunkGenerator createChunkGenerator(
      Registry<Biome> biomeRegistry,
      Registry<StructureSet> structureSetRegistry,
      Registry<NoiseGeneratorSettings> noiseSettingsRegistry,
      long seed) {
    return new NoiseBasedChunkGenerator(
        new FixedBiomeSource(biomeRegistry.getOrCreateHolderOrThrow(Biomes.PLAINS)),
        seed,
        () -> noiseSettingsRegistry.getOrCreateHolderOrThrow(LIBRARY_NOISE_SETTINGS));
  }

  public static void bootstrapNoiseSettings(BootstapContext<NoiseGeneratorSettings> context) {
    HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
    HolderGetter<NormalNoise.NoiseParameters> noiseParams = context.lookup(Registries.NOISE);
    NoiseGeneratorSettings settings =
        new NoiseGeneratorSettings(
            new StructureSettings(Optional.empty(), Map.of()),
            new NoiseSettings(
                -64, // minY
                384, // height
                1, // noiseSizeHorizontal
                1 // noiseSizeVertical
                ),
            Blocks.STONE.defaultBlockState(),
            Blocks.WATER.defaultBlockState(),
            noiseParams.getOrThrow(Noises.OCEAN_TEMPERATURE),
            noiseParams.getOrThrow(Noises.OCEAN_RARITY),
            noiseParams.getOrThrow(Noises.OCEAN_LEVEL),
            noiseParams.getOrThrow(Noises.OCEAN_LEVEL_WITH_DEEP_OCEAN),
            noiseParams.getOrThrow(Noises.OCEAN_LEVEL_WITH_SURFACE),
            0.0F,
            NoiseRouterData.overworld(noiseParams, densityFunctions, false, false),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                    SurfaceRules.isBiome(Biomes.PLAINS),
                    SurfaceRules.sequence(
                        SurfaceRules.ifTrue(
                            SurfaceRules.ON_FLOOR,
                            SurfaceRules.state(Blocks.OAK_PLANKS.defaultBlockState())),
                        SurfaceRules.ifTrue(
                            SurfaceRules.UNDER_FLOOR,
                            SurfaceRules.state(Blocks.BOOKSHELF.defaultBlockState())))),
                SurfaceRules.ifTrue(
                    SurfaceRules.verticalGradient(
                        "bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)),
                    SurfaceRules.state(Blocks.BEDROCK.defaultBlockState()))),
            List.of(),
            64,
            false,
            true,
            true,
            false);

    context.register(LIBRARY_NOISE_SETTINGS, settings);
  }
}
