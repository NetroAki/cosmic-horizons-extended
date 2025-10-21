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

public class ObsidianFloraFeature extends Feature<NoneFeatureConfiguration> {
  private static final BlockState OBSIDIAN = Blocks.OBSIDIAN.defaultBlockState();
  private static final BlockState CRYING_OBSIDIAN = Blocks.CRYING_OBSIDIAN.defaultBlockState();
  private static final BlockState OBSIDIAN_SPIKE =
      InfernoBlocks.OBSIDIAN_SPIKE.get().defaultBlockState();
  private static final BlockState OBSIDIAN_THORN_VINES =
      InfernoBlocks.OBSIDIAN_THORN_VINES.get().defaultBlockState();

  public ObsidianFloraFeature(Codec<NoneFeatureConfiguration> codec) {
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

    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, y, z);

    // Only generate on obsidian or crying obsidian
    BlockState belowState = level.getBlockState(mutablePos.below());
    if (belowState.getBlock() != Blocks.OBSIDIAN
        && belowState.getBlock() != Blocks.CRYING_OBSIDIAN) {
      return false;
    }

    // Choose a random flora type
    if (random.nextFloat() < 0.7f) {
      // Generate obsidian spikes
      generateSpikes(level, mutablePos, random);
    } else {
      // Generate obsidian thorn vines
      generateThornVines(level, mutablePos, random);
    }

    return true;
  }

  private void generateSpikes(
      WorldGenLevel level, BlockPos.MutableBlockPos pos, RandomSource random) {
    int count = 1 + random.nextInt(3);

    for (int i = 0; i < count; i++) {
      int dx = random.nextInt(5) - 2;
      int dz = random.nextInt(5) - 2;
      BlockPos spikePos = pos.offset(dx, 0, dz);

      // Only place on obsidian or crying obsidian
      BlockState belowState = level.getBlockState(spikePos.below());
      if (belowState.getBlock() == Blocks.OBSIDIAN
          || belowState.getBlock() == Blocks.CRYING_OBSIDIAN) {
        setBlock(level, spikePos, OBSIDIAN_SPIKE);

        // Sometimes add a second spike on top
        if (random.nextFloat() < 0.3f) {
          setBlock(level, spikePos.above(), OBSIDIAN_SPIKE);
        }
      }
    }
  }

  private void generateThornVines(
      WorldGenLevel level, BlockPos.MutableBlockPos pos, RandomSource random) {
    int count = 2 + random.nextInt(4);

    for (int i = 0; i < count; i++) {
      int dx = random.nextInt(5) - 2;
      int dz = random.nextInt(5) - 2;
      BlockPos vinePos = pos.offset(dx, 0, dz);

      // Find the highest obsidian block
      while (vinePos.getY() > level.getMinBuildHeight() + 4
          && (level.getBlockState(vinePos).isAir()
              || level.getBlockState(vinePos).getBlock() == Blocks.OBSIDIAN
              || level.getBlockState(vinePos).getBlock() == Blocks.CRYING_OBSIDIAN)) {
        vinePos = vinePos.below();
      }
      vinePos = vinePos.above();

      // Only place on obsidian or crying obsidian
      BlockState belowState = level.getBlockState(vinePos.below());
      if (belowState.getBlock() == Blocks.OBSIDIAN
          || belowState.getBlock() == Blocks.CRYING_OBSIDIAN) {
        // Generate a column of vines
        int height = 1 + random.nextInt(3);
        for (int dy = 0; dy < height; dy++) {
          if (level.getBlockState(vinePos.above(dy)).isAir()) {
            setBlock(level, vinePos.above(dy), OBSIDIAN_THORN_VINES);
          } else {
            break;
          }
        }
      }
    }
  }
}
