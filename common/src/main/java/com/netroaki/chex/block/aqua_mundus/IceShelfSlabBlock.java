package com.netroaki.chex.block.aqua_mundus;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;

/** A special slab block that is slippery and can be placed on water surfaces. */
public class IceShelfSlabBlock extends SlabBlock {
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public IceShelfSlabBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(
        this.stateDefinition.any().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, false));
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    // Can be placed on any solid surface or on water
    BlockPos belowPos = pos.below();
    BlockState belowState = level.getBlockState(belowPos);
    return belowState.isFaceSturdy(level, belowPos, Direction.UP)
        || belowState.getBlock() == net.minecraft.world.level.block.Blocks.WATER;
  }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockPos blockpos = context.getClickedPos();
    Level level = context.getLevel();
    BlockState blockstate = level.getBlockState(blockpos);

    if (blockstate.is(this)) {
      return blockstate.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false);
    } else {
      BlockState blockstate1 =
          this.defaultBlockState()
              .setValue(TYPE, SlabType.BOTTOM)
              .setValue(
                  WATERLOGGED,
                  level.getFluidState(blockpos).getType()
                      == net.minecraft.world.level.material.Fluids.WATER);

      Direction direction = context.getClickedFace();
      return direction != Direction.DOWN
              && (direction == Direction.UP
                  || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D))
          ? blockstate1
          : blockstate1.setValue(TYPE, SlabType.TOP);
    }
  }

  @Override
  public void fallOn(
      Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
    // Reduce fall damage on this block
    if (entity.isSuppressingBounce()) {
      super.fallOn(level, state, pos, entity, fallDistance);
    } else {
      entity.causeFallDamage(fallDistance, 0.2F, level.damageSources().fall());
    }
  }

  // TODO: Fix when updateEntityAfterFallOn method is available
  // @Override
  public void updateEntityAfterFallOn(Level level, Entity entity) {
    // Make entities bounce slightly when landing on this block
    if (entity.isSuppressingBounce()) {
      super.updateEntityAfterFallOn(level, entity);
    } else {
      this.bounceUp(entity);
    }
  }

  private void bounceUp(Entity entity) {
    Vec3 vector3d = entity.getDeltaMovement();
    if (vector3d.y < 0.0D) {
      double d0 = entity instanceof LivingEntity ? 0.5D : 0.3D;
      entity.setDeltaMovement(vector3d.x, -vector3d.y * (double) 0.66F * d0, vector3d.z);
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(TYPE, WATERLOGGED);
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
      level.scheduleTick(
          pos,
          net.minecraft.world.level.material.Fluids.WATER,
          net.minecraft.world.level.material.Fluids.WATER.getTickDelay(level));
    }

    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }
}
