package com.netroaki.chex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public class FloatingStoneBlock extends Block {
  private static final int CHECK_RADIUS = 4;
  private static final int FALL_DELAY = 2;

  public FloatingStoneBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
    level.scheduleTick(pos, this, FALL_DELAY);
  }

  @Override
  public BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      LevelAccessor level,
      BlockPos currentPos,
      BlockPos neighborPos) {
    level.scheduleTick(currentPos, this, FALL_DELAY);
    return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
  }

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (!isSupported(level, pos)) {
      level.destroyBlock(pos, true);
      // Spawn falling block entity with custom behavior
      // FallingBlockEntity.fall(level, pos, state);
    }
  }

  private boolean isSupported(Level level, BlockPos pos) {
    // Check if there's a solid block below within the check radius
    for (int x = -CHECK_RADIUS; x <= CHECK_RADIUS; x++) {
      for (int z = -CHECK_RADIUS; z <= CHECK_RADIUS; z++) {
        BlockPos checkPos = pos.offset(x, -1, z);
        if (level.getBlockState(checkPos).isFaceSturdy(level, checkPos, Direction.UP)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public PushReaction getPistonPushReaction(BlockState state) {
    return PushReaction.DESTROY;
  }
}
