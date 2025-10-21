package com.netroaki.chex.world.library;

import com.netroaki.chex.CHEX;
import java.util.OptionalLong;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.blending.BlendedNoise;

public class LibraryDimensionType {
  public static final ResourceKey<Level> LIBRARY_DIMENSION =
      ResourceKey.create(
          Registries.DIMENSION, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));

  public static final ResourceKey<DimensionType> LIBRARY_DIMENSION_TYPE =
      ResourceKey.create(
          Registries.DIMENSION_TYPE, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));

  public static final ResourceKey<LevelStem> LIBRARY_LEVEL_STEM =
      ResourceKey.create(
          Registries.LEVEL_STEM, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));

  public static final ResourceKey<NoiseGeneratorSettings> LIBRARY_NOISE_SETTINGS =
      ResourceKey.create(
          Registries.NOISE_SETTINGS, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));

  public static final ResourceKey<WorldPreset> LIBRARY_WORLD_PRESET =
      ResourceKey.create(
          Registries.WORLD_PRESET, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));

  public static DimensionType createDimensionType() {
    return new DimensionType(
        OptionalLong.empty(), // fixedTime
        true, // hasSkylight
        false, // hasCeiling
        false, // ultraWarm
        false, // natural
        1.0, // coordinateScale
        false, // bedWorks
        false, // respawnAnchorWorks
        -64, // minY
        320, // height
        256, // logicalHeight
        BlockTags.INFINIBURN_OVERWORLD, // infiniburn
        BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
        0.1f, // ambientLight
        new DimensionType.MonsterSettings(
            false, // piglinSafe
            true, // hasRaids
            false, // bedWorks
            false // respawnAnchorWorks
            ));
  }

  public static NoiseGeneratorSettings createNoiseSettings() {
    return new NoiseGeneratorSettings(
        NoiseGeneratorSettings.Preset.OVERWORLD.value().noiseSettings(),
        Blocks.STONE.defaultBlockState(),
        Blocks.WATER.defaultBlockState(),
        BlendedNoise.create(
            new NormalNoise.NoiseParameters(-8, 1.0, 1.0, 1.0, 1.0),
            new NormalNoise.NoiseParameters(-3, 1.0, 1.0, 1.0, 1.0),
            1,
            2,
            1.0,
            1.0,
            1.0),
        NoiseGeneratorSettings.Preset.OVERWORLD.value().surfaceRule(),
        List.of(), // spawnTarget
        0, // seaLevel
        false, // disableMobGeneration
        false, // aquifersEnabled
        false, // oreVeinsEnabled
        false // useLegacyRandomSource
        );
  }
}
