package com.netroaki.chex.block.arrakis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SpiceCactusBlock extends BushBlock implements BonemealableBlock {
  public static final int MAX_AGE = 3;
  public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
  private static final VoxelShape[] SHAPE_BY_AGE =
      new VoxelShape[] {
        Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D),
        Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
        Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
        Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)
      };

  public SpiceCactusBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
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
      if (age < MAX_AGE) {
        if (random.nextInt(5) == 0) {
          level.setBlock(pos, state.setValue(AGE, age + 1), 2);
        }
      }
    }
  }

  @Override
  public boolean isRandomlyTicking(@NotNull BlockState state) {
    return state.getValue(AGE) < MAX_AGE;
  }

  @Override
  public @NotNull VoxelShape getShape(
      BlockState state,
      @NotNull BlockGetter level,
      @NotNull BlockPos pos,
      @NotNull CollisionContext context) {
    return SHAPE_BY_AGE[state.getValue(AGE)];
  }

  @Override
  protected boolean mayPlaceOn(
      BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
    return state.is(Blocks.SAND)
        || state.is(Blocks.RED_SAND)
        || state.is(Blocks.SANDSTONE)
        || state.is(Blocks.RED_SANDSTONE)
        || state.is(Blocks.TERRACOTTA)
        || state.is(Blocks.WHITE_TERRACOTTA)
        || state.is(Blocks.ORANGE_TERRACOTTA)
        || state.is(Blocks.RED_TERRACOTTA)
        || state.is(Blocks.YELLOW_TERRACOTTA)
        || state.is(Blocks.BROWN_TERRACOTTA);
  }

  @Override
  public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
    BlockPos belowPos = pos.below();
    return this.mayPlaceOn(level.getBlockState(belowPos), level, belowPos);
  }

  @Override
  public @NotNull BlockState updateShape(
      BlockState state,
      @NotNull Direction direction,
      @NotNull BlockState neighborState,
      @NotNull LevelAccessor level,
      @NotNull BlockPos pos,
      @NotNull BlockPos neighborPos) {
    if (!state.canSurvive(level, pos)) {
      return Blocks.AIR.defaultBlockState();
    }
    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public boolean isValidBonemealTarget(
      @NotNull LevelReader level,
      @NotNull BlockPos pos,
      @NotNull BlockState state,
      boolean isClient) {
    return state.getValue(AGE) < MAX_AGE;
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
    int newAge = Math.min(MAX_AGE, state.getValue(AGE) + 1);
    level.setBlock(pos, state.setValue(AGE, newAge), 2);
  }

  @Override
  protected void createBlockStateDefinition(
      StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }
}
