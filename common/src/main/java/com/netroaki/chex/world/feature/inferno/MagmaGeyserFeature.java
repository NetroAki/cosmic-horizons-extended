package com.netroaki.chex.world.feature.inferno;

import com.mojang.serialization.Codec;
import com.netroaki.chex.block.inferno.InfernoBlocks;
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

public class MagmaGeyserFeature extends Feature<NoneFeatureConfiguration> {
  private static final BlockState MAGMA_BLOCK = Blocks.MAGMA_BLOCK.defaultBlockState();
  private static final BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
  private static final BlockState LAVA = Blocks.LAVA.defaultBlockState();
  private static final BlockState MAGMA_GEYSER =
      InfernoBlocks.MAGMA_GEYSER.get().defaultBlockState();

  public MagmaGeyserFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel level = context.level();
    BlockPos origin = context.origin();
    RandomSource random = context.random();

    // Find a suitable position on the surface
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

    // Create the geyser structure
    int height = 3 + random.nextInt(3);
    int radius = 1 + random.nextInt(2);

    // Generate the base
    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        if (dx * dx + dz * dz <= radius * radius + 1) {
          setBlock(level, mutablePos.offset(dx, -1, dz), NETHERRACK);
          setBlock(level, mutablePos.offset(dx, 0, dz), MAGMA_BLOCK);
        }
      }
    }

    // Generate the central column
    for (int dy = 1; dy <= height; dy++) {
      setBlock(level, mutablePos.offset(0, dy, 0), dy == height ? MAGMA_GEYSER : MAGMA_BLOCK);

      // Add some randomness to the column
      if (dy < height - 1 && random.nextFloat() < 0.3f) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        setBlock(level, mutablePos.offset(0, dy, 0).relative(direction), MAGMA_BLOCK);
      }
    }

    // Add some lava pools around
    if (random.nextFloat() < 0.7f) {
      int pools = 1 + random.nextInt(3);
      for (int i = 0; i < pools; i++) {
        int dx = random.nextInt(5) - 2;
        int dz = random.nextInt(5) - 2;
        if ((dx != 0 || dz != 0)
            && level
                .getBlockState(mutablePos.offset(dx, -1, dz))
                .isSolidRender(level, mutablePos.offset(dx, -1, dz))) {
          setBlock(level, mutablePos.offset(dx, 0, dz), LAVA);
        }
      }
    }

    return true;
  }
}
