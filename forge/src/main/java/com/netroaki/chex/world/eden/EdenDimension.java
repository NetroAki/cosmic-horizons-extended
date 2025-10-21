package com.netroaki.chex.world.eden;

import java.util.OptionalLong;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class EdenDimension {
  public static final ResourceKey<Level> EDEN_DIMENSION =
      ResourceKey.create(
          Registries.DIMENSION, new ResourceLocation("cosmic_horizons_extended", "eden_garden"));

  public static final ResourceKey<LevelStem> EDEN_LEVEL_STEM =
      ResourceKey.create(
          Registries.LEVEL_STEM, new ResourceLocation("cosmic_horizons_extended", "eden_garden"));

  public static final ResourceKey<DimensionType> EDEN_TYPE =
      ResourceKey.create(
          Registries.DIMENSION_TYPE,
          new ResourceLocation("cosmic_horizons_extended", "eden_garden"));

  public static DimensionType createDimensionType() {
    return new DimensionType(
        OptionalLong.empty(),
        true,
        false,
        false,
        true,
        1.0,
        true,
        false,
        -64,
        384,
        384,
        DimensionType.OVERWORLD_INFOBAR,
        DimensionType.OVERWORLD_EFFECTS,
        0.1f,
        new DimensionType.MonsterSettings(false, true, Integer.MIN_VALUE, 0));
  }

  public static NoiseBasedChunkGenerator createChunkGenerator(RegistryAccess registryAccess) {
    return new NoiseBasedChunkGenerator(
        new StructureSetSource(
            registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), Optional.empty()),
        registryAccess.registryOrThrow(Registry.NOISE_REGISTRY),
        ((Holder<NoiseGeneratorSettings>)
                registryAccess
                    .registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY)
                    .getHolderOrThrow(NoiseGeneratorSettings.OVERWORLD))
            .value());
  }

  public static void register() {
    // Registration handled by Forge's registry system
  }
}
