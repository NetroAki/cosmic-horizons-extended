package com.netroaki.chex.world.feature.inferno;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BasaltPillarFeature extends Feature<NoneFeatureConfiguration> {
  private static final BlockState BASALT = Blocks.BASALT.defaultBlockState();
  private static final BlockState POLISHED_BASALT = Blocks.POLISHED_BASALT.defaultBlockState();
  private static final BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
  private static final BlockState GILDED_BLACKSTONE = Blocks.GILDED_BLACKSTONE.defaultBlockState();

  public BasaltPillarFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel level = context.level();
    BlockPos origin = context.origin();
    RandomSource random = context.random();

    // Find a suitable position
    int x = origin.getX();
    int z = origin.getZ();
    int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;

    // Don't generate in the void
    if (y < level.getMinBuildHeight() + 4) {
      return false;
    }

    // Check if the block below is solid
    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, y, z);
    if (!level.getBlockState(mutablePos.below()).isSolidRender(level, mutablePos.below())) {
      return false;
    }

    // Generate a single pillar or a cluster
    if (random.nextFloat() < 0.7f) {
      // Single pillar
      generatePillar(level, mutablePos, random, 4 + random.nextInt(8));
    } else {
      // Cluster of pillars
      int count = 2 + random.nextInt(3);
      for (int i = 0; i < count; i++) {
        int dx = random.nextInt(5) - 2;
        int dz = random.nextInt(5) - 2;
        if ((dx != 0 || dz != 0)
            && level
                .getBlockState(mutablePos.offset(dx, -1, dz))
                .isSolidRender(level, mutablePos.offset(dx, -1, dz))) {
          generatePillar(level, mutablePos.offset(dx, 0, dz), random, 2 + random.nextInt(6));
        }
      }
    }

    return true;
  }

  private void generatePillar(
      WorldGenLevel level, BlockPos.MutableBlockPos pos, RandomSource random, int height) {
    // Base
    setBlock(level, pos, BLACKSTONE);

    // Main pillar
    for (int dy = 1; dy < height; dy++) {
      BlockState state = random.nextFloat() < 0.1f ? POLISHED_BASALT : BASALT;
      setBlock(level, pos.offset(0, dy, 0), state);

      // Add some variation
      if (random.nextFloat() < 0.2f) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        setBlock(level, pos.offset(0, dy, 0).relative(direction), state);
      }
    }

    // Top decoration
    if (random.nextFloat() < 0.3f) {
      setBlock(level, pos.offset(0, height, 0), GILDED_BLACKSTONE);
    } else {
      setBlock(level, pos.offset(0, height, 0), POLISHED_BASALT);
    }

    // Add some small decorations around the base
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      if (random.nextFloat() < 0.4f) {
        setBlock(level, pos.relative(direction), BLACKSTONE);
        if (random.nextFloat() < 0.5f) {
          setBlock(level, pos.relative(direction).below(), BLACKSTONE);
        }
      }
    }
  }
}
