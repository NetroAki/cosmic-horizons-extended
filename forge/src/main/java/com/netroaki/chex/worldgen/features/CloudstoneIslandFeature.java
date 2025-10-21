package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CloudstoneIslandFeature extends Feature<NoneFeatureConfiguration> {

  private static final BlockState CLOUDSTONE =
      CHEXBlocks.PANDORITE_POLISHED.get().defaultBlockState();
  private static final BlockState MOSSY_CLOUDSTONE =
      CHEXBlocks.PANDORITE_MOSSY.get().defaultBlockState();
  private static final BlockState BIOLUME_MOSS = CHEXBlocks.BIOLUME_MOSS.get().defaultBlockState();
  private static final BlockState AIR = Blocks.AIR.defaultBlockState();

  public CloudstoneIslandFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel world = context.level();
    BlockPos pos = context.origin();
    RandomSource random = context.random();

    // Only place if there's air below (floating island)
    if (!world.isEmptyBlock(pos.below())) {
      return false;
    }

    // Island size and shape parameters
    int radius = 3 + random.nextInt(5); // 3-7 blocks radius
    int height = 1 + random.nextInt(3); // 1-3 blocks thick

    // Generate the main island disc
    for (int x = -radius; x <= radius; x++) {
      for (int z = -radius; z <= radius; z++) {
        // Create a circular shape
        float distance = x * x + z * z;
        if (distance <= radius * radius) {
          // Calculate thickness based on distance from center (thinner at edges)
          float thicknessFactor = 1.0f - (distance / (radius * radius));
          int localHeight = (int) (height * (0.5f + 0.5f * thicknessFactor));

          // Place the island blocks
          for (int y = 0; y < localHeight; y++) {
            BlockPos currentPos = pos.offset(x, -y, z);

            // Only replace air or replaceable blocks
            if (world.isEmptyBlock(currentPos) || world.getBlockState(currentPos).canBeReplaced()) {
              // Use mossy cloudstone for the bottom layer
              BlockState blockToPlace =
                  (y == localHeight - 1)
                      ? (random.nextFloat() < 0.7f ? MOSSY_CLOUDSTONE : CLOUDSTONE)
                      : CLOUDSTONE;

              setBlock(world, currentPos, blockToPlace);

              // Add some biolume moss on top
              if (y == 0 && random.nextFloat() < 0.3f) {
                BlockPos above = currentPos.above();
                if (world.isEmptyBlock(above)) {
                  setBlock(world, above, BIOLUME_MOSS);
                }
              }
            }
          }

          // Add some hanging particles underneath
          if (random.nextFloat() < 0.1f) {
            BlockPos below = pos.offset(x, -localHeight, z);
            if (world.isEmptyBlock(below)) {
              // Add some hanging vines or particles here if desired
              // For now, we'll just leave it as air
            }
          }
        }
      }
    }

    // Add a small chance for a tree or crystal on larger islands
    if (radius > 5 && random.nextFloat() < 0.3f) {
      BlockPos surfacePos = pos.above();
      if (world.isEmptyBlock(surfacePos)) {
        // Place a small feature on top
        if (random.nextBoolean()) {
          // Small crystal formation
          int crystalHeight = 2 + random.nextInt(3);
          for (int y = 0; y < crystalHeight; y++) {
            setBlock(world, surfacePos.above(y), CLOUDSTONE);
          }
        } else {
          // Small bush
          setBlock(world, surfacePos, BIOLUME_MOSS);
          for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
              if ((x != 0 || z != 0) && random.nextFloat() < 0.7f) {
                BlockPos bushPos = surfacePos.offset(x, 0, z);
                if (world.isEmptyBlock(bushPos)) {
                  setBlock(world, bushPos, BIOLUME_MOSS);
                }
              }
            }
          }
        }
      }
    }

    return true;
  }
}
