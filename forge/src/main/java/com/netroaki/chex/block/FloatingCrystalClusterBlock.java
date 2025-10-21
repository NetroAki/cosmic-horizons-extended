package com.netroaki.chex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FloatingCrystalClusterBlock extends AmethystClusterBlock {
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final DirectionProperty FACING = DirectionalBlock.FACING;
  protected final VoxelShape northAabb;
  protected final VoxelShape southAabb;
  protected final VoxelShape eastAabb;
  protected final VoxelShape westAabb;
  protected final VoxelShape upAabb;
  protected final VoxelShape downAabb;

  public FloatingCrystalClusterBlock(int height, int xzOffset, Properties properties) {
    super(height, xzOffset, properties);
    this.registerDefaultState(
        this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(FACING, Direction.UP));

    // Custom hitbox shapes for better interaction
    this.upAabb = Block.box(xzOffset, 0.0D, xzOffset, 16 - xzOffset, height, 16 - xzOffset);
    this.downAabb = Block.box(xzOffset, 16 - height, xzOffset, 16 - xzOffset, 16.0D, 16 - xzOffset);
    this.northAabb =
        Block.box(xzOffset, xzOffset, 16 - height, 16 - xzOffset, 16 - xzOffset, 16.0D);
    this.southAabb = Block.box(xzOffset, xzOffset, 0.0D, 16 - xzOffset, 16 - xzOffset, height);
    this.eastAabb = Block.box(0.0D, xzOffset, xzOffset, height, 16 - xzOffset, 16 - xzOffset);
    this.westAabb = Block.box(16 - height, xzOffset, xzOffset, 16.0D, 16 - xzOffset, 16 - xzOffset);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    Direction direction = state.getValue(FACING);
    return switch (direction) {
      case NORTH -> this.northAabb;
      case SOUTH -> this.southAabb;
      case EAST -> this.eastAabb;
      case WEST -> this.westAabb;
      case DOWN -> this.downAabb;
      case UP -> this.upAabb;
    };
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    Direction direction = state.getValue(FACING);
    BlockPos blockpos = pos.relative(direction.getOpposite());
    return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, direction);
  }

  @Override
  public BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      LevelAccessor level,
      BlockPos pos,
      BlockPos neighborPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)
        ? Blocks.AIR.defaultBlockState()
        : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    LevelAccessor levelaccessor = context.getLevel();
    BlockPos blockpos = context.getClickedPos();
    return this.defaultBlockState()
        .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)
        .setValue(FACING, context.getClickedFace());
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED, FACING);
  }

  @Override
  public boolean isRandomlyTicking(BlockState state) {
    return true;
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    // Add particle effects or other random behaviors
    if (random.nextInt(10) == 0) {
      // Spawn particles or other effects
    }
  }
}
