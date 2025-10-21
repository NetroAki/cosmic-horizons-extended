package com.netroaki.chex.world.feature;

import com.mojang.serialization.Codec;
import com.netroaki.chex.registry.CHEXBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class BioluminescentTreeFeature extends Feature<TreeConfiguration> {
  public BioluminescentTreeFeature(Codec<TreeConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
    WorldGenLevel level = context.level();
    BlockPos pos = context.origin();
    RandomSource random = context.random();
    TreeConfiguration config = context.config();

    // Check if we can place the tree here
    if (!canPlaceTree(level, pos)) {
      return false;
    }

    // Generate trunk
    int height = 5 + random.nextInt(3);
    for (int i = 0; i < height; i++) {
      placeLog(level, pos.above(i), config);
    }

    // Generate leaves in layers
    BlockPos top = pos.above(height - 1);
    generateLeafLayer(level, top, 2, random, config);
    generateLeafLayer(level, top.below(), 3, random, config);
    generateLeafLayer(level, top.below(2), 2, random, config);

    // Add some hanging vines or other decorations
    addVines(level, top, random);

    return true;
  }

  private boolean canPlaceTree(WorldGenLevel level, BlockPos pos) {
    // Check if the tree can be placed here
    return level.getBlockState(pos.below()).is(CHEXBlocks.PANDORA_GRASS_BLOCK.get())
        && level.getBlockState(pos).isAir()
        && level.getBlockState(pos.above()).isAir();
  }

  private void placeLog(WorldGenLevel level, BlockPos pos, TreeConfiguration config) {
    if (level.getBlockState(pos).isAir()
        || level.getBlockState(pos).is(CHEXBlocks.BIOLUMINESCENT_LEAVES.get())) {
      setBlock(level, pos, config.trunkProvider.getState(random, pos));
    }
  }

  private void generateLeafLayer(
      WorldGenLevel level,
      BlockPos center,
      int radius,
      RandomSource random,
      TreeConfiguration config) {
    BlockPos.betweenClosedStream(
            center.offset(-radius, 0, -radius), center.offset(radius, 0, radius))
        .filter(
            pos -> {
              double distance = Math.sqrt(center.distSqr(pos));
              if (distance > radius) return false;
              // Randomize leaf placement for a more natural look
              if (distance > radius - 1 && random.nextFloat() > 0.7f) return false;
              return level.getBlockState(pos).isAir();
            })
        .forEach(
            pos -> {
              // Chance to place a leaf block
              if (random.nextFloat() > 0.1f) {
                setBlock(level, pos, config.leavesProvider.getState(random, pos));

                // Chance to place hanging vines
                if (random.nextFloat() < 0.2f) {
                  int vineLength = 1 + random.nextInt(2);
                  for (int i = 1; i <= vineLength; i++) {
                    BlockPos vinePos = pos.below(i);
                    if (level.getBlockState(vinePos).isAir()) {
                      setBlock(
                          level,
                          vinePos,
                          CHEXBlocks.BIOLUMINESCENT_VINES.get().defaultBlockState());
                    } else {
                      break;
                    }
                  }
                }
              }
            });
  }

  private void addVines(WorldGenLevel level, BlockPos top, RandomSource random) {
    // Add some hanging vines from the bottom leaves
    for (int i = 0; i < 4; i++) {
      BlockPos vineStart =
          top.offset(random.nextInt(3) - 1, -random.nextInt(2), random.nextInt(3) - 1);

      if (level.getBlockState(vineStart).is(CHEXBlocks.BIOLUMINESCENT_LEAVES.get())) {
        int length = 1 + random.nextInt(3);
        for (int j = 0; j < length; j++) {
          BlockPos vinePos = vineStart.below(j);
          if (level.getBlockState(vinePos).isAir()) {
            setBlock(level, vinePos, CHEXBlocks.BIOLUMINESCENT_VINES.get().defaultBlockState());
          } else {
            break;
          }
        }
      }
    }
  }
}
