package com.netroaki.chex.block.arrakis;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class DesertShrubBlock extends BushBlock implements BonemealableBlock {
  public static final int MAX_AGE = 2;
  public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
  private static final VoxelShape[] SHAPE_BY_AGE =
      new VoxelShape[] {
        Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
        Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)
      };

  public DesertShrubBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
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
  public void randomTick(
      BlockState state,
      @NotNull ServerLevel level,
      @NotNull BlockPos pos,
      @NotNull RandomSource random) {
    if (!level.isAreaLoaded(pos, 1)) return;

    int age = state.getValue(AGE);
    if (age < MAX_AGE && random.nextInt(5) == 0 && level.getRawBrightness(pos, 0) >= 9) {
      level.setBlock(pos, state.setValue(AGE, age + 1), 2);
    }

    // Try to spread to adjacent blocks
    if (age >= 1 && random.nextInt(10) == 0) {
      BlockPos spreadPos =
          pos.offset(
              random.nextInt(3) - 1, // -1, 0, or 1
              random.nextInt(2) - 1, // -1 or 0
              random.nextInt(3) - 1 // -1, 0, or 1
              );

      if (level.isEmptyBlock(spreadPos) && canSurvive(state, level, spreadPos)) {
        level.setBlock(spreadPos, this.defaultBlockState(), 2);
      }
    }
  }

  @Override
  protected boolean mayPlaceOn(
      BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
    return state.is(BlockTags.SAND)
        || state.is(Blocks.TERRACOTTA)
        || state.is(Blocks.WHITE_TERRACOTTA)
        || state.is(Blocks.ORANGE_TERRACOTTA)
        || state.is(Blocks.RED_TERRACOTTA)
        || state.is(Blocks.YELLOW_TERRACOTTA)
        || state.is(Blocks.BROWN_TERRACOTTA);
  }

  @Override
  public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
    return !context.isSecondaryUseActive()
            && context.getItemInHand().is(this.asItem())
            && state.getValue(AGE) < MAX_AGE
        ? true
        : super.canBeReplaced(state, context);
  }

  @Override
  public boolean isRandomlyTicking(@NotNull BlockState state) {
    return state.getValue(AGE) < MAX_AGE;
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
    int age = state.getValue(AGE);
    int newAge = Math.min(MAX_AGE, age + 1);
    level.setBlock(pos, state.setValue(AGE, newAge), 2);

    // Try to spread when bonemealed
    if (newAge >= 1) {
      for (int i = 0; i < 3; ++i) {
        BlockPos spreadPos =
            pos.offset(
                random.nextInt(3) - 1,
                random.nextInt(2) - random.nextInt(2),
                random.nextInt(3) - 1);

        if (level.isEmptyBlock(spreadPos) && canSurvive(state, level, spreadPos)) {
          level.setBlock(spreadPos, this.defaultBlockState(), 2);
          break;
        }
      }
    }
  }

  @Override
  protected void createBlockStateDefinition(
      StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }
}
