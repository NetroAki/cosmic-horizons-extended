package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class MagmaSpireFeature extends Feature<NoneFeatureConfiguration> {

  private static final BlockState MAGMA = Blocks.MAGMA_BLOCK.defaultBlockState();
  private static final BlockState OBSIDIAN = Blocks.OBSIDIAN.defaultBlockState();
  private static final BlockState LAVA = Blocks.LAVA.defaultBlockState();
  private static final BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();

  public MagmaSpireFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel world = context.level();
    BlockPos pos = context.origin();
    RandomSource random = context.random();

    // Check if we can place on the block below
    if (!world.getBlockState(pos.below()).isSolid()) {
      return false;
    }

    // Generate main spire
    int height = 6 + random.nextInt(5); // 6-10 blocks tall
    int baseWidth = 1 + random.nextInt(2); // 1-2 blocks wide at base

    // Generate the main column
    for (int y = 0; y < height; y++) {
      // Taper the spire as it gets higher
      int width = (int) (baseWidth * (1.0f - (y / (float) height))) + 1;

      // Create a cross pattern for the spire
      for (int x = -width; x <= width; x++) {
        for (int z = -width; z <= width; z++) {
          if (Math.abs(x) + Math.abs(z) <= width + 1) {
            BlockPos currentPos = pos.offset(x, y, z);

            // Only place if air or replaceable
            if (world.isEmptyBlock(currentPos) || world.getBlockState(currentPos).canBeReplaced()) {
              // Add some variation to the blocks
              BlockState blockToPlace = MAGMA;
              if (random.nextFloat() < 0.1f) {
                blockToPlace = OBSIDIAN;
              } else if (random.nextFloat() < 0.05f) {
                blockToPlace = NETHERRACK;
              }

              setBlock(world, currentPos, blockToPlace);

              // Add some lava on top of some blocks
              if (y > 0 && random.nextFloat() < 0.3f) {
                BlockPos above = currentPos.above();
                if (world.isEmptyBlock(above)) {
                  setBlock(world, above, LAVA);
                }
              }
            }
          }
        }
      }
    }

    // Add some lava pools around the base
    if (random.nextFloat() < 0.7f) {
      int poolRadius = 2 + random.nextInt(2);
      for (int x = -poolRadius; x <= poolRadius; x++) {
        for (int z = -poolRadius; z <= poolRadius; z++) {
          if (x * x + z * z <= poolRadius * poolRadius) {
            BlockPos poolPos = pos.offset(x, -1, z);
            if (world.isEmptyBlock(poolPos) || world.getBlockState(poolPos).canBeReplaced()) {
              setBlock(world, poolPos, LAVA);
              // Ensure blocks below can support the lava
              BlockPos below = poolPos.below();
              if (world.isEmptyBlock(below) || world.getFluidState(below).is(Fluids.LAVA)) {
                setBlock(world, below, MAGMA);
              }
            }
          }
        }
      }
    }

    return true;
  }
}
