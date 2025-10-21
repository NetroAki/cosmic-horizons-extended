package com.netroaki.chex.block.aqua_mundus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.KelpBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/** Custom kelp block that emits light and can grow. */
public class LuminousKelpBlock extends KelpBlock {
  public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

  public LuminousKelpBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(GLOWING, false));
  }

  @Override
  protected Block getBodyBlock() {
    return AquaMundusBlocks.LUMINOUS_KELP_PLANT.get();
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return super.getStateForPlacement(context)
        .setValue(GLOWING, context.getLevel().random.nextFloat() < 0.7f);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(GLOWING);
  }

  @Override
  public BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      LevelAccessor level,
      BlockPos pos,
      BlockPos neighborPos) {
    // Randomly toggle glowing state when updated
    if (level.getRandom().nextInt(100) == 0) {
      state = state.setValue(GLOWING, !state.getValue(GLOWING));
    }
    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public boolean isRandomlyTicking(BlockState state) {
    return true;
  }

  @Override
  public void randomTick(
      BlockState state,
      net.minecraft.server.level.ServerLevel level,
      BlockPos pos,
      RandomSource random) {
    super.randomTick(state, level, pos, random);
    // Randomly toggle glowing state
    if (random.nextInt(100) == 0) {
      level.setBlock(pos, state.setValue(GLOWING, !state.getValue(GLOWING)), 2);
    }
  }
}
