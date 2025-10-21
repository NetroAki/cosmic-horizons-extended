package com.netroaki.chex.world.eden.features.flora;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EdenFloraFeatures {
  public static class CelestialBloomFeature extends Feature<NoneFeatureConfiguration> {
    public CelestialBloomFeature(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin();
      RandomSource random = context.random();

      // Only place on grass blocks
      if (!level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)) {
        return false;
      }

      // Don't replace solid blocks
      if (!level.getBlockState(pos).isAir()) {
        return false;
      }

      // Random age for the plant
      BlockState plantState =
          EdenFloraBlocks.CELESTIAL_BLOOM
              .get()
              .defaultBlockState()
              .setValue(EdenPlantBlock.AGE, random.nextInt(4));

      if (plantState.canSurvive(level, pos)) {
        level.setBlock(pos, plantState, 2);
        return true;
      }

      return false;
    }
  }

  // Add more feature classes for other plant types here
}
