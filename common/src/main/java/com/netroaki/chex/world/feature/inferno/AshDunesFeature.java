package com.netroaki.chex.world.feature.inferno;

import com.mojang.serialization.Codec;
import com.netroaki.chex.block.inferno.InfernoBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AshDunesFeature extends Feature<NoneFeatureConfiguration> {
  private static final BlockState COMPACTED_ASH =
      InfernoBlocks.COMPACTED_ASH.get().defaultBlockState();
  private static final BlockState ASHEN_SOIL = InfernoBlocks.ASHEN_SOIL.get().defaultBlockState();
  private static final BlockState SOUL_SAND = Blocks.SOUL_SAND.defaultBlockState();
  private static final BlockState SOUL_SOIL = Blocks.SOUL_SOIL.defaultBlockState();

  public AshDunesFeature(Codec<NoneFeatureConfiguration> codec) {
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

    // Check if we're in a suitable biome (ash wastes)
    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, y, z);
    if (!level.getBlockState(mutablePos.below()).is(Blocks.NETHERRACK)
        && !level.getBlockState(mutablePos.below()).is(Blocks.SOUL_SAND)
        && !level.getBlockState(mutablePos.below()).is(Blocks.SOUL_SOIL)) {
      return false;
    }

    // Generate the dune
    int radius = 2 + random.nextInt(4);
    int height = 1 + random.nextInt(3);

    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        // Skip if outside the circular area
        if (dx * dx + dz * dz > radius * radius) continue;

        // Calculate the height at this position (parabolic dune shape)
        double distance = Math.sqrt(dx * dx + dz * dz) / radius;
        int localHeight = (int) (height * (1 - distance * distance));

        // Place the blocks
        for (int dy = 0; dy <= localHeight; dy++) {
          BlockPos pos = mutablePos.offset(dx, dy, dz);

          // Only replace air or replaceable blocks
          if (level.getBlockState(pos).isAir()
              || level.getBlockState(pos).getMaterial().isReplaceable()) {
            // Use different blocks based on height
            BlockState blockToPlace;
            if (dy == localHeight) {
              // Top layer
              blockToPlace = random.nextFloat() < 0.3f ? COMPACTED_ASH : SOUL_SAND;
            } else if (dy > localHeight / 2) {
              // Upper half
              blockToPlace = random.nextFloat() < 0.7f ? ASHEN_SOIL : SOUL_SOIL;
            } else {
              // Lower half
              blockToPlace = random.nextFloat() < 0.3f ? ASHEN_SOIL : SOUL_SOIL;
            }

            setBlock(level, pos, blockToPlace);
          }
        }

        // Add some variation at the base
        if (random.nextFloat() < 0.2f) {
          BlockPos basePos = mutablePos.offset(dx, -1, dz);
          if (level.getBlockState(basePos).is(Blocks.NETHERRACK)) {
            setBlock(level, basePos, random.nextBoolean() ? SOUL_SAND : SOUL_SOIL);
          }
        }
      }
    }

    // Occasionally add some bone blocks sticking out
    if (random.nextFloat() < 0.3f) {
      int bones = 1 + random.nextInt(3);
      for (int i = 0; i < bones; i++) {
        int dx = random.nextInt(radius * 2) - radius;
        int dz = random.nextInt(radius * 2) - radius;
        BlockPos bonePos = mutablePos.offset(dx, 0, dz);

        // Find the top block
        while (bonePos.getY() < level.getMaxBuildHeight()
            && (level.getBlockState(bonePos).isAir()
                || level.getBlockState(bonePos).getMaterial().isReplaceable())) {
          bonePos = bonePos.above();
        }
        bonePos = bonePos.below();

        // Place bone blocks
        if (level.getBlockState(bonePos).is(COMPACTED_ASH.getBlock())
            || level.getBlockState(bonePos).is(ASHEN_SOIL.getBlock())
            || level.getBlockState(bonePos).is(SOUL_SAND.getBlock())
            || level.getBlockState(bonePos).is(SOUL_SOIL.getBlock())) {

          int boneHeight = 1 + random.nextInt(2);
          for (int dy = 0; dy < boneHeight; dy++) {
            setBlock(level, bonePos.above(dy + 1), Blocks.BONE_BLOCK.defaultBlockState());
          }
        }
      }
    }

    return true;
  }
}
