package com.netroaki.chex.block.flora;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IcicleSpireBlock extends Block {
  public static final DirectionProperty FACING = BlockStateProperties.FACING;
  public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 3);
  public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

  private static final VoxelShape[] SHAPES_UP =
      new VoxelShape[] {
        Block.box(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D), // Size 0 (smallest)
        Block.box(5.0D, 0.0D, 5.0D, 11.0D, 12.0D, 11.0D), // Size 1
        Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D), // Size 2
        Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D) // Size 3 (largest)
      };

  private static final VoxelShape[] SHAPES_DOWN =
      new VoxelShape[] {
        Block.box(6.0D, 8.0D, 6.0D, 10.0D, 16.0D, 10.0D), // Size 0 (smallest)
        Block.box(5.0D, 4.0D, 5.0D, 11.0D, 16.0D, 11.0D), // Size 1
        Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D), // Size 2
        Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D) // Size 3 (largest)
      };

  private static final VoxelShape[] SHAPES_HORIZONTAL =
      new VoxelShape[] {
        Block.box(8.0D, 6.0D, 0.0D, 10.0D, 10.0D, 8.0D), // Size 0 (smallest)
        Block.box(7.0D, 5.0D, 0.0D, 11.0D, 11.0D, 12.0D), // Size 1
        Block.box(6.0D, 4.0D, 0.0D, 12.0D, 12.0D, 16.0D), // Size 2
        Block.box(5.0D, 3.0D, 0.0D, 13.0D, 13.0D, 16.0D) // Size 3 (largest)
      };

  public IcicleSpireBlock() {
    super(
        Properties.of()
            .mapColor(MapColor.ICE)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .strength(0.5f, 1.5f)
            .requiresCorrectToolForDrops()
            .lightLevel(state -> state.getValue(SIZE) >= 2 ? 3 : 0));
    this.registerDefaultState(
        this.stateDefinition
            .any()
            .setValue(FACING, Direction.DOWN)
            .setValue(SIZE, 0)
            .setValue(HANGING, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, SIZE, HANGING);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    Direction direction = state.getValue(FACING);
    int size = state.getValue(SIZE);

    if (direction == Direction.UP) {
      return SHAPES_UP[Math.min(size, SHAPES_UP.length - 1)];
    } else if (direction == Direction.DOWN) {
      return SHAPES_DOWN[Math.min(size, SHAPES_DOWN.length - 1)];
    } else {
      return SHAPES_HORIZONTAL[Math.min(size, SHAPES_HORIZONTAL.length - 1)].move(
          0, 0.5 - 0.5 * (size / 3.0), 0);
    }
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    // Randomly grow over time if conditions are right
    if (random.nextInt(10) == 0 && canGrow(level, pos, state)) {
      int currentSize = state.getValue(SIZE);
      if (currentSize < 3) {
        level.setBlock(pos, state.setValue(SIZE, currentSize + 1), 3);

        // Play sound when growing
        level.playSound(
            null,
            pos,
            SoundEvents.GLASS_PLACE,
            SoundSource.BLOCKS,
            0.5f,
            0.8f + random.nextFloat() * 0.4f);

        // Try to grow adjacent icicles
        growAdjacent(level, pos, random);
      }
    }

    // Randomly melt if too warm
    if (random.nextInt(20) == 0 && shouldMelt(level, pos, state)) {
      int currentSize = state.getValue(SIZE);
      if (currentSize > 0) {
        level.setBlock(pos, state.setValue(SIZE, currentSize - 1), 3);

        // Play sound when melting
        level.playSound(
            null,
            pos,
            SoundEvents.GLASS_BREAK,
            SoundSource.BLOCKS,
            0.5f,
            1.2f + random.nextFloat() * 0.4f);
      } else {
        level.destroyBlock(pos, false);
      }
    }
  }

  private boolean canGrow(Level level, BlockPos pos, BlockState state) {
    if (level.isRainingAt(pos.above()) && level.canSeeSky(pos.above())) {
      return true;
    }

    // Check if there's ice or snow nearby
    BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -1; z <= 1; z++) {
          if (x == 0 && y == 0 && z == 0) continue;

          BlockState nearby = level.getBlockState(checkPos.setWithOffset(pos, x, y, z));
          if (nearby.is(Blocks.ICE)
              || nearby.is(Blocks.PACKED_ICE)
              || nearby.is(Blocks.BLUE_ICE)
              || nearby.is(Blocks.SNOW_BLOCK)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  private boolean shouldMelt(Level level, BlockPos pos, BlockState state) {
    // Melt faster in warm biomes or near heat sources
    if (level.getBiome(pos).value().warmEnoughToRain(pos)) {
      return true;
    }

    // Check for nearby heat sources
    BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
    for (int x = -2; x <= 2; x++) {
      for (int y = -2; y <= 2; y++) {
        for (int z = -2; z <= 2; z++) {
          if (x == 0 && y == 0 && z == 0) continue;

          BlockState nearby = level.getBlockState(checkPos.setWithOffset(pos, x, y, z));
          if (nearby.is(Blocks.LAVA)
              || nearby.is(Blocks.FIRE)
              || nearby.is(Blocks.CAMPFIRE)
              || nearby.is(Blocks.LAVA_CAULDRON)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  private void growAdjacent(Level level, BlockPos pos, RandomSource random) {
    if (random.nextFloat() < 0.3f) {
      Direction direction = random.nextBoolean() ? Direction.UP : Direction.DOWN;
      BlockPos growPos = pos.relative(direction);

      if (level.isEmptyBlock(growPos) && canSupportIcicle(level, growPos, direction)) {
        level.setBlock(
            growPos,
            this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(SIZE, 0)
                .setValue(HANGING, direction == Direction.DOWN),
            3);

        // Play sound when growing a new icicle
        level.playSound(
            null,
            growPos,
            SoundEvents.GLASS_PLACE,
            SoundSource.BLOCKS,
            0.3f,
            0.9f + random.nextFloat() * 0.2f);
      }
    }
  }

  private boolean canSupportIcicle(LevelReader level, BlockPos pos, Direction direction) {
    BlockPos supportPos = pos.relative(direction.getOpposite());
    BlockState supportState = level.getBlockState(supportPos);

    // Can attach to solid blocks or other icicles
    return supportState.isFaceSturdy(level, supportPos, direction)
        || supportState.getBlock() instanceof IcicleSpireBlock;
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity livingEntity) {
      // Deal damage when walking into large icicles
      int size = state.getValue(SIZE);
      if (size >= 2) {
        livingEntity.hurt(level.damageSources().sting(livingEntity), size - 1);

        // Apply slowness
        livingEntity.addEffect(
            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, size - 1, false, false, true));

        // Play sound when damaging
        if (!level.isClientSide) {
          level.playSound(
              null,
              pos,
              SoundEvents.GLASS_BREAK,
              SoundSource.BLOCKS,
              0.5f,
              1.0f + level.random.nextFloat() * 0.5f);
        }
      }
    }

    // Bounce off slightly
    Vec3 motion = entity.getDeltaMovement();
    Direction facing = state.getValue(FACING);

    if (facing == Direction.UP && motion.y < 0) {
      entity.setDeltaMovement(motion.x, -motion.y * 0.5, motion.z);
    } else if (facing == Direction.DOWN && motion.y > 0) {
      entity.setDeltaMovement(motion.x, -motion.y * 0.5, motion.z);
    } else if (facing.getAxis() != Direction.Axis.Y) {
      entity.setDeltaMovement(
          facing.getStepX() != 0 ? -motion.x * 0.5 : motion.x,
          motion.y,
          facing.getStepZ() != 0 ? -motion.z * 0.5 : motion.z);
    }
  }

  @Override
  public void fallOn(
      Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
    // Deal extra fall damage when landing on icicles
    if (state.getValue(FACING) == Direction.UP) {
      int size = state.getValue(SIZE);
      if (size >= 1) {
        entity.causeFallDamage(fallDistance, 0.5f * (size + 1), level.damageSources().fall());

        // Break the icicle if fallen on hard enough
        if (fallDistance > 3.0f && size > 0) {
          level.destroyBlock(pos, true);
          level.playSound(
              null,
              pos,
              SoundEvents.GLASS_BREAK,
              SoundSource.BLOCKS,
              1.0f,
              0.8f + level.random.nextFloat() * 0.4f);
          return;
        }
      }
    }

    super.fallOn(level, state, pos, entity, fallDistance);
  }

  @Override
  public void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (!level.isClientSide) {
      // Play sound when placed
      level.playSound(
          null,
          pos,
          SoundEvents.GLASS_PLACE,
          SoundSource.BLOCKS,
          0.5f,
          0.8f + level.random.nextFloat() * 0.4f);
    }
  }

  @Override
  public void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
    if (!level.isClientSide && !state.is(newState.getBlock())) {
      // Play sound when broken
      level.playSound(
          null,
          pos,
          SoundEvents.GLASS_BREAK,
          SoundSource.BLOCKS,
          0.7f,
          0.9f + level.random.nextFloat() * 0.2f);
    }

    super.onRemove(state, level, pos, newState, isMoving);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    Direction direction = state.getValue(FACING);
    BlockPos supportPos = pos.relative(direction.getOpposite());
    BlockState supportState = level.getBlockState(supportPos);

    // Can attach to solid blocks or other icicles
    return supportState.isFaceSturdy(level, supportPos, direction)
        || supportState.getBlock() instanceof IcicleSpireBlock;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    Direction direction = context.getNearestLookingDirection().getOpposite();
    boolean hanging = direction == Direction.DOWN;

    // Only allow placing on solid surfaces
    if (canSupportIcicle(context.getLevel(), context.getClickedPos(), direction)) {
      return this.defaultBlockState()
          .setValue(FACING, direction)
          .setValue(SIZE, 0)
          .setValue(HANGING, hanging);
    }

    return null;
  }

  @Override
  public PushReaction getPistonPushReaction(BlockState state) {
    return PushReaction.DESTROY;
  }
}
