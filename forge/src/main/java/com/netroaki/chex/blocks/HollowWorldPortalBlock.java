package com.netroaki.chex.blocks;

import com.netroaki.chex.world.hollow.HollowWorldDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HollowWorldPortalBlock extends Block {
  public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
  protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
  protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

  public HollowWorldPortalBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return state.getValue(AXIS) == Direction.Axis.Z ? Z_AXIS_AABB : X_AXIS_AABB;
  }

  @Override
  public BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      LevelAccessor level,
      BlockPos pos,
      BlockPos neighborPos) {
    Direction.Axis axis = direction.getAxis();
    Direction.Axis axis1 = state.getValue(AXIS);
    boolean flag = axis1 != axis && axis.isHorizontal();
    return !flag
            && !neighborState.is(this)
            && !(new com.netroaki.chex.world.hollow.portal.HollowWorldPortalShape(
                    level, pos, axis1))
                .isComplete()
        ? Blocks.AIR.defaultBlockState()
        : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (random.nextInt(100) == 0) {
      level.playLocalSound(
          (double) pos.getX() + 0.5D,
          (double) pos.getY() + 0.5D,
          (double) pos.getZ() + 0.5D,
          SoundEvents.PORTAL_AMBIENT,
          SoundSource.BLOCKS,
          0.5F,
          random.nextFloat() * 0.4F + 0.8F,
          false);
    }

    for (int i = 0; i < 4; ++i) {
      double d0 = (double) pos.getX() + random.nextDouble();
      double d1 = (double) pos.getY() + random.nextDouble();
      double d2 = (double) pos.getZ() + random.nextDouble();
      double d3 = ((double) random.nextFloat() - 0.5D) * 0.5D;
      double d4 = ((double) random.nextFloat() - 0.5D) * 0.5D;
      double d5 = ((double) random.nextFloat() - 0.5D) * 0.5D;
      int j = random.nextInt(2) * 2 - 1;

      if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
        d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
        d3 = (double) (random.nextFloat() * 2.0F * (float) j);
      } else {
        d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
        d5 = (double) (random.nextFloat() * 2.0F * (float) j);
      }

      level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
    }
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()) {
      if (entity.isOnPortalCooldown()) {
        entity.setPortalCooldown();
      } else {
        if (!entity.level().isClientSide && !pos.equals(entity.portalEntrancePos)) {
          entity.portalEntrancePos = pos.immutable();
        }

        Level destinationLevel = entity.level();

        if (destinationLevel != null) {
          MinecraftServer minecraftserver = destinationLevel.getServer();
          ResourceKey<Level> destinationKey =
              entity.level().dimension() == HollowWorldDimension.HOLLOW_WORLD
                  ? Level.OVERWORLD
                  : HollowWorldDimension.HOLLOW_WORLD;

          if (minecraftserver != null) {
            ServerLevel destinationLevel1 = minecraftserver.getLevel(destinationKey);
            if (destinationLevel1 != null) {
              entity.setPortalCooldown();
              entity.changeDimension(
                  destinationLevel1,
                  new com.netroaki.chex.world.hollow.portal.HollowWorldTeleporter(
                      destinationLevel1));
            }
          }
        }
      }
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AXIS);
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }
}
