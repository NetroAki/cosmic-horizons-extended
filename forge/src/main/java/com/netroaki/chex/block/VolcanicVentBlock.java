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

public class VolcanicVentBlock extends Block {
  public VolcanicVentBlock() {
    super(
        Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(2.0f, 10.0f)
            .requiresCorrectToolForDrops()
            .lightLevel(state -> 6)
            .randomTicks());
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    super.animateTick(state, level, pos, random);

    // Emit smoke and ember particles
    if (random.nextInt(3) == 0) {
      double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;
      double y = pos.getY() + 1.0D;
      double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;

      // Main smoke column
      level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.0D, 0.1D, 0.0D);

      // Occasional embers
      if (random.nextInt(5) == 0) {
        level.addParticle(
            ParticleTypes.FLAME,
            x,
            y,
            z,
            (random.nextDouble() - 0.5D) * 0.1D,
            0.1D,
            (random.nextDouble() - 0.5D) * 0.1D);
      }
    }
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    // Small chance to spawn lava or fire nearby
    if (random.nextInt(100) == 0) {
      BlockPos targetPos =
          pos.offset(random.nextInt(3) - 1, random.nextInt(2) - 1, random.nextInt(3) - 1);

      if (level.isEmptyBlock(targetPos)) {
        if (random.nextBoolean()) {
          level.setBlockAndUpdate(targetPos, Blocks.FIRE.defaultBlockState());
        } else if (level.isEmptyBlock(targetPos.below())) {
          level.setBlockAndUpdate(targetPos, Blocks.LAVA.defaultBlockState());
        }
      }
    }
  }
}
