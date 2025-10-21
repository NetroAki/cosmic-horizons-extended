package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PandoraKelpForestFeature extends Feature<NoneFeatureConfiguration> {

  private static final BlockState KELP = Blocks.KELP.defaultBlockState();
  private static final BlockState KELP_PLANT = Blocks.KELP_PLANT.defaultBlockState();
  // TODO: Fix when CHEXBlocks is implemented
  // private static final BlockState BIOLUME_KELP =
  // CHEXBlocks.BIOLUME_KELP.get().defaultBlockState();
  // private static final BlockState BIOLUME_KELP_PLANT =
  // CHEXBlocks.BIOLUME_KELP_PLANT.get().defaultBlockState();
  private static final BlockState BIOLUME_KELP = Blocks.KELP.defaultBlockState();
  private static final BlockState BIOLUME_KELP_PLANT = Blocks.KELP_PLANT.defaultBlockState();

  public PandoraKelpForestFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel world = context.level();
    BlockPos pos = context.origin();
    RandomSource random = context.random();

    if (!world.getFluidState(pos).isSource()) {
      return false;
    }

    int height = 8 + random.nextInt(12); // 8-20 blocks tall
    boolean isBiolume = random.nextFloat() < 0.3f; // 30% chance for biolume kelp

    BlockState kelp = isBiolume ? BIOLUME_KELP : KELP;
    BlockState kelpPlant = isBiolume ? BIOLUME_KELP_PLANT : KELP_PLANT;

    // Generate kelp stalk
    for (int y = 0; y < height; y++) {
      BlockPos currentPos = pos.above(y);
      if (world.getBlockState(currentPos).is(Blocks.WATER)) {
        if (y == 0) {
          setBlock(world, currentPos, kelp);
        } else {
          setBlock(world, currentPos, kelpPlant);
        }

        // Add occasional side growths
        if (y > 2 && random.nextFloat() < 0.2f) {
          addSideGrowth(world, currentPos, random, isBiolume);
        }
      } else {
        break;
      }
    }

    return true;
  }

  private void addSideGrowth(
      LevelAccessor world, BlockPos pos, RandomSource random, boolean isBiolume) {
    BlockState kelp = isBiolume ? BIOLUME_KELP : KELP;
    BlockState kelpPlant = isBiolume ? BIOLUME_KELP_PLANT : KELP_PLANT;

    // Choose a random direction for the side growth
    int dirX = random.nextBoolean() ? 1 : -1;
    if (random.nextBoolean()) {
      dirX = 0;
    }
    int dirZ = (dirX == 0 && random.nextBoolean()) ? (random.nextBoolean() ? 1 : -1) : 0;

    if (dirX == 0 && dirZ == 0) return;

    BlockPos sidePos = pos.offset(dirX, 0, dirZ);

    // Check if there's water at the side position
    if (world.getBlockState(sidePos).is(Blocks.WATER)) {
      int sideHeight = 1 + random.nextInt(4); // 1-4 blocks tall

      for (int y = 0; y < sideHeight; y++) {
        BlockPos currentPos = sidePos.above(y);
        if (world.getBlockState(currentPos).is(Blocks.WATER)) {
          if (y == 0) {
            setBlock(world, currentPos, kelp);
          } else {
            setBlock(world, currentPos, kelpPlant);
          }
        } else {
          break;
        }
      }
    }
  }
}
