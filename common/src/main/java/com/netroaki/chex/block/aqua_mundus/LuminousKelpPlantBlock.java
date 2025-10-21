package com.netroaki.chex.block.aqua_mundus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

/** The plant part of the luminous kelp that grows underwater. */
public class LuminousKelpPlantBlock extends GrowingPlantBodyBlock {
  public static final BooleanProperty GLOWING = LuminousKelpBlock.GLOWING;
  public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

  public LuminousKelpPlantBlock(Properties properties) {
    super(properties, Direction.UP, SHAPE, false);
    this.registerDefaultState(this.stateDefinition.any().setValue(GLOWING, false));
  }

  // TODO: Fix when getHeadBlock method is available
  // @Override
  protected GrowingPlantHeadBlock getHeadBlock() {
    return (GrowingPlantHeadBlock) AquaMundusBlocks.LUMINOUS_KELP.get();
  }

  // TODO: Fix when updateHeadAfterReceivedDecay method is available
  // @Override
  protected BlockState updateHeadAfterReceivedDecay(BlockState state, BlockState headState) {
    return headState.setValue(GLOWING, state.getValue(GLOWING));
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
      level.scheduleTick(pos, this, 1);
    }
    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public void tick(
      BlockState state,
      net.minecraft.server.level.ServerLevel level,
      BlockPos pos,
      RandomSource random) {
    // Randomly toggle glowing state
    if (random.nextInt(100) == 0) {
      level.setBlock(pos, state.setValue(GLOWING, !state.getValue(GLOWING)), 2);
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(GLOWING);
  }
}
