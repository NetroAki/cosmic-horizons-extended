package com.netroaki.chex.block.crystal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CrystalBlock extends Block implements EntityBlock {
  public static final EnumProperty<CrystalVariant> VARIANT =
      EnumProperty.create("variant", CrystalVariant.class);
  private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);

  public CrystalBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(
        this.stateDefinition
            .any()
            .setValue(VARIANT, CrystalVariant.SMALL)
            .setValue(BlockStateProperties.FACING, Direction.UP));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(VARIANT, BlockStateProperties.FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState()
        .setValue(BlockStateProperties.FACING, context.getClickedFace())
        .setValue(VARIANT, CrystalVariant.SMALL);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    Direction direction = state.getValue(BlockStateProperties.FACING);
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
    if (!state.canSurvive(level, pos)) {
      return Blocks.AIR.defaultBlockState();
    }
    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    CrystalGrowthHandler.onRandomTick(state, level, pos, random);
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    this.tick(state, level, pos, random);
  }

  // BlockEntity implementation
  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    // TODO: Implement CrystalBlockEntity
    return null;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level level, BlockState state, BlockEntityType<T> type) {
    return level.isClientSide
        ? null
        : (lvl, pos, blockState, blockEntity) -> {
          // TODO: Implement CrystalBlockEntity
          // if (blockEntity instanceof CrystalBlockEntity crystal) {
          // crystal.tick();
          // }
        };
  }

  @Override
  public PushReaction getPistonPushReaction(BlockState state) {
    return PushReaction.DESTROY;
  }

  public enum CrystalVariant implements StringRepresentable {
    SMALL,
    MEDIUM,
    LARGE,
    CLUSTER;

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase();
    }
  }
}
