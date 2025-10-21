package com.netroaki.chex.block.stormworld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ReformingCloudBlock extends Block {
  public ReformingCloudBlock(BlockBehaviour.Properties props) {
    super(props.randomTicks());
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    // Occasionally reform (spread) into adjacent air blocks
    if (random.nextInt(20) == 0) {
      Direction dir = Direction.getRandom(random);
      BlockPos target = pos.relative(dir);
      if (level.isEmptyBlock(target)) {
        level.setBlock(target, state, 2);
      }
    }
  }

  @Override
  public BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      LevelAccessor level,
      BlockPos pos,
      BlockPos neighborPos) {
    // If isolated, very slowly dissipate
    if (level instanceof ServerLevel server && server.random.nextInt(200) == 0) {
      // Chance to remove if surrounded by non-clouds
      boolean surrounded = true;
      for (Direction d : Direction.values()) {
        if (level.getBlockState(pos.relative(d)).getBlock() == this) {
          surrounded = false;
          break;
        }
      }
      if (surrounded) {
        server.removeBlock(pos, false);
      }
    }
    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }
}
