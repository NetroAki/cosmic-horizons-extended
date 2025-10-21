package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SkybarkTreeFeature extends Feature<NoneFeatureConfiguration> {

  private static final BlockState TRUNK = CHEXBlocks.PANDORITE_POLISHED.get().defaultBlockState();
  private static final BlockState LEAVES = CHEXBlocks.BIOLUME_MOSS.get().defaultBlockState();

  public SkybarkTreeFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel world = context.level();
    BlockPos pos = context.origin();
    RandomSource random = context.random();

    if (!canPlaceOn(world, pos.below())) {
      return false;
    }

    // Generate trunk with varying height (12-20 blocks)
    int height = 12 + random.nextInt(9);
    int trunkHeight = height - 4 - random.nextInt(3);

    // Generate main trunk
    for (int y = 0; y < trunkHeight; y++) {
      setBlock(world, pos.above(y), TRUNK);

      // Add occasional branches
      if (y > 3 && y < trunkHeight - 2 && random.nextFloat() < 0.3f) {
        addBranch(world, pos.above(y), random, 2 + random.nextInt(3));
      }
    }

    // Generate canopy
    BlockPos canopyStart = pos.above(trunkHeight - 3);
    generateCanopy(world, canopyStart, random, height - trunkHeight + 3);

    return true;
  }

  private boolean canPlaceOn(WorldGenLevel world, BlockPos pos) {
    return world.getBlockState(pos).isSolidRender(world, pos);
  }

  private void addBranch(WorldGenLevel world, BlockPos start, RandomSource random, int length) {
    // Random horizontal direction
    int dirX = random.nextBoolean() ? 1 : -1;
    int dirZ = random.nextBoolean() ? 1 : -1;

    // Randomly choose x or z direction
    if (random.nextBoolean()) {
      dirX = 0;
    } else {
      dirZ = 0;
    }

    // Create branch
    for (int i = 1; i <= length; i++) {
      BlockPos branchPos = start.offset(dirX * i, 0, dirZ * i);
      if (world.isEmptyBlock(branchPos) || world.getBlockState(branchPos).is(LEAVES.getBlock())) {
        setBlock(world, branchPos, TRUNK);

        // Add leaves at the end of branches
        if (i == length) {
          addLeafCluster(world, branchPos, random, 2);
        }
      } else {
        break;
      }
    }
  }

  private void generateCanopy(
      WorldGenLevel world, BlockPos center, RandomSource random, int layers) {
    // Generate multiple layers of leaves
    for (int layer = 0; layer < layers; layer++) {
      int radius = (layers - layer) / 2 + 1;
      for (int x = -radius; x <= radius; x++) {
        for (int z = -radius; z <= radius; z++) {
          if (x * x + z * z <= radius * radius) {
            BlockPos leafPos = center.offset(x, layer, z);
            if (world.isEmptyBlock(leafPos) || world.getBlockState(leafPos).is(TRUNK.getBlock())) {
              setBlock(world, leafPos, LEAVES);
            }
          }
        }
      }
    }
  }

  private void addLeafCluster(
      WorldGenLevel world, BlockPos center, RandomSource random, int radius) {
    for (int x = -radius; x <= radius; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -radius; z <= radius; z++) {
          if (x * x + y * y + z * z <= radius * radius) {
            BlockPos pos = center.offset(x, y, z);
            if (world.isEmptyBlock(pos) || world.getBlockState(pos).is(TRUNK.getBlock())) {
              setBlock(world, pos, LEAVES);
            }
          }
        }
      }
    }
  }
}
