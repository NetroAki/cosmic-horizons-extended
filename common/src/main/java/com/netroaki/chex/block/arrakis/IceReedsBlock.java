package com.netroaki.chex.block.arrakis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class IceReedsBlock extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {
  public static final int MAX_HEIGHT = 3;
  public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

  public IceReedsBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(
        this.stateDefinition.any().setValue(AGE, 0).setValue(WATERLOGGED, true));
  }

  @Override
  public @NotNull VoxelShape getShape(
      BlockState state,
      @NotNull BlockGetter level,
      @NotNull BlockPos pos,
      @NotNull CollisionContext context) {
    return SHAPE;
  }

  @Override
  public void randomTick(
      BlockState state,
      @NotNull ServerLevel level,
      @NotNull BlockPos pos,
      @NotNull RandomSource random) {
    if (!level.isAreaLoaded(pos, 1)) return;

    if (level.getRawBrightness(pos, 0) >= 9) {
      int age = state.getValue(AGE);
      if (age < 7) {
        if (random.nextInt(5) == 0) {
          level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
      } else {
        // Try to grow taller
        BlockPos above = pos.above();
        if (level.getBlockState(above).isAir() && level.getFluidState(above).is(Fluids.WATER)) {
          int height = 1;
          while (level.getBlockState(pos.below(height)).is(this)) {
            height++;
          }

          if (height < MAX_HEIGHT) {
            level.setBlock(
                above, this.defaultBlockState().setValue(WATERLOGGED, true).setValue(AGE, 0), 3);
          }
        }
      }
    }
  }

  @Override
  public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
    BlockState belowState = level.getBlockState(pos.below());
    BlockState aboveState = level.getBlockState(pos.above());

    // Can be placed on ice, packed ice, or blue ice
    boolean canPlaceOn =
        belowState.is(Blocks.ICE)
            || belowState.is(Blocks.PACKED_ICE)
            || belowState.is(Blocks.BLUE_ICE)
            || belowState.is(this);

    // Must have water above or be the top block
    boolean validTop = aboveState.getFluidState().is(Fluids.WATER) || !aboveState.blocksMotion();

    return canPlaceOn && validTop;
  }

  @Override
  public @NotNull BlockState updateShape(
      BlockState state,
      @NotNull Direction direction,
      @NotNull BlockState neighborState,
      @NotNull LevelAccessor level,
      @NotNull BlockPos pos,
      @NotNull BlockPos neighborPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    if (!state.canSurvive(level, pos)) {
      level.scheduleTick(pos, this, 1);
    }

    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public void tick(
      BlockState state,
      @NotNull ServerLevel level,
      @NotNull BlockPos pos,
      @NotNull RandomSource random) {
    if (!state.canSurvive(level, pos)) {
      level.destroyBlock(pos, true);
    }
  }

  @Override
  public boolean isRandomlyTicking(@NotNull BlockState state) {
    return state.getValue(AGE) < 7;
  }

  @Override
  public boolean isValidBonemealTarget(
      @NotNull LevelReader level,
      @NotNull BlockPos pos,
      @NotNull BlockState state,
      boolean isClient) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(
      @NotNull Level level,
      @NotNull RandomSource random,
      @NotNull BlockPos pos,
      @NotNull BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(
      @NotNull ServerLevel level,
      @NotNull RandomSource random,
      @NotNull BlockPos pos,
      @NotNull BlockState state) {
    // Try to grow taller first
    BlockPos abovePos = pos.above();
    if (level.getBlockState(abovePos).isAir() && level.getFluidState(abovePos).is(Fluids.WATER)) {
      int height = 1;
      while (level.getBlockState(pos.below(height)).is(this)) {
        height++;
      }

      if (height < MAX_HEIGHT) {
        level.setBlock(
            abovePos, this.defaultBlockState().setValue(WATERLOGGED, true).setValue(AGE, 0), 3);
        return;
      }
    }

    // If can't grow taller, just increase age
    int age = state.getValue(AGE);
    if (age < 7) {
      level.setBlock(pos, state.setValue(AGE, Math.min(7, age + random.nextInt(3) + 1)), 2);
    }
  }

  @Override
  public @NotNull FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected void createBlockStateDefinition(
      StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    builder.add(AGE, WATERLOGGED);
  }
}
