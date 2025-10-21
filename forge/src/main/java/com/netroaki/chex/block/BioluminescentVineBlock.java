package com.netroaki.chex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BioluminescentVineBlock extends GrowingPlantHeadBlock {
  private static final VoxelShape SHAPE = Block.box(4.0D, 9.0D, 4.0D, 12.0D, 16.0D, 12.0D);
  private static final int MAX_AGE = 25;
  private static final double GROW_PER_TICK_PROBABILITY = 0.2D;

  public BioluminescentVineBlock(Properties properties) {
    super(properties, Direction.DOWN, SHAPE, false, GROW_PER_TICK_PROBABILITY);
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (random.nextDouble() < GROW_PER_TICK_PROBABILITY) {
      if (state.getValue(AGE) < MAX_AGE) {
        level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), 2);
      } else {
        BlockPos below = pos.below();
        if (level.getBlockState(below).isAir()) {
          level.setBlock(below, this.getStateForPlacement(level), 2);
        }
      }
    }
  }

  @Override
  protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
    return 1;
  }

  @Override
  protected boolean canGrowInto(BlockState state) {
    return state.isAir();
  }

  @Override
  protected Block getBodyBlock() {
    return this;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    BlockPos above = pos.above();
    BlockState aboveState = level.getBlockState(above);
    return aboveState.isFaceSturdy(level, above, Direction.DOWN) || aboveState.getBlock() == this;
    // || aboveState.is(CHEXBlocks.BIOLUMINESCENT_LEAVES.get()); // TODO: Fix when CHEXBlocks is
    // implemented
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (level.isClientSide && random.nextInt(10) == 0) {
      // Add glowing particle effects
      double x = (double) pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;
      double y = (double) pos.getY() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;
      double z = (double) pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;

      level.addParticle(
          net.minecraft.core.particles.ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
    }
  }
}
