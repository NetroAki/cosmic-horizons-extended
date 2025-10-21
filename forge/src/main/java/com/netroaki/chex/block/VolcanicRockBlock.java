package com.netroaki.chex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class VolcanicRockBlock extends Block {
  public VolcanicRockBlock() {
    super(
        Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(2.0f, 10.0f)
            .requiresCorrectToolForDrops()
            .lightLevel(state -> 3));
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (random.nextInt(10) == 0) {
      // Emit smoke particles occasionally
      double x = (double) pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;
      double y = (double) pos.getY() + 1.0D;
      double z = (double) pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;

      level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.0D, 0.1D, 0.0D);
    }
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    // Small chance to spawn lava below if air is below
    if (random.nextInt(100) == 0 && level.isEmptyBlock(pos.below())) {
      level.setBlockAndUpdate(pos.below(), Blocks.LAVA.defaultBlockState());
    }
  }
}
