package com.netroaki.chex.block.flora;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PressureSpongeBlock extends BushBlock
    implements SimpleWaterloggedBlock, BonemealableBlock {
  public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final VoxelShape SHAPE_SMALL = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
  private static final VoxelShape SHAPE_MEDIUM = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D);
  private static final VoxelShape SHAPE_LARGE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

  public PressureSpongeBlock() {
    super(
        Properties.of()
            .mapColor(MapColor.COLOR_BLUE)
            .sound(SoundType.WET_GRASS)
            .noOcclusion()
            .strength(0.2f)
            .lightLevel(state -> state.getValue(AGE) == 3 ? 5 : 0));
    this.registerDefaultState(
        this.stateDefinition.any().setValue(AGE, 0).setValue(WATERLOGGED, true));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE, WATERLOGGED);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    int age = state.getValue(AGE);
    if (age < 2) {
      return SHAPE_SMALL;
    } else if (age == 2) {
      return SHAPE_MEDIUM;
    } else {
      return SHAPE_LARGE;
    }
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    super.randomTick(state, level, pos, random);

    // Only grow when waterlogged
    if (!state.getValue(WATERLOGGED)) {
      return;
    }

    int age = state.getValue(AGE);

    // Chance to grow
    if (random.nextInt(10) == 0 && age < 3) {
      // Check if there's enough space above
      boolean canGrow = true;
      if (age >= 2) {
        for (int y = 1; y <= 2; y++) {
          BlockPos checkPos = pos.above(y);
          if (!level.getFluidState(checkPos).is(Fluids.WATER)
              || !level.getBlockState(checkPos).isAir()) {
            canGrow = false;
            break;
          }
        }
      }

      if (canGrow) {
        // Grow to next stage
        level.setBlock(pos, state.setValue(AGE, age + 1), 3);

        // Play sound when growing
        level.playSound(
            null,
            pos,
            SoundEvents.WET_GRASS_PLACE,
            SoundSource.BLOCKS,
            0.5f,
            0.8f + random.nextFloat() * 0.4f);

        // If fully grown, try to spawn a new sponge nearby
        if (age == 2) {
          trySpawnNewSponge(level, pos, random);
        }
      }
    }

    // Randomly emit bubbles when underwater
    if (random.nextInt(20) == 0 && state.getValue(WATERLOGGED)) {
      level.addParticle(
          ParticleTypes.BUBBLE_COLUMN_UP,
          pos.getX() + 0.5,
          pos.getY() + 0.1,
          pos.getZ() + 0.5,
          0,
          0.1,
          0);
    }
  }

  private void trySpawnNewSponge(ServerLevel level, BlockPos pos, RandomSource random) {
    if (random.nextFloat() < 0.3f) {
      // Try to find a nearby water block to spawn a new sponge
      for (int i = 0; i < 5; i++) {
        BlockPos spawnPos =
            pos.offset(
                random.nextInt(5) - 2, // -2 to 2
                random.nextInt(3) - 1, // -1 to 1
                random.nextInt(5) - 2 // -2 to 2
                );

        // Check if the block is water and has a solid block below
        if (level.getBlockState(spawnPos).is(Blocks.WATER)
            && canSustainPlant(
                level.getBlockState(spawnPos.below()),
                level,
                spawnPos.below(),
                Direction.UP,
                this)) {

          // Place a new sponge
          level.setBlock(
              spawnPos, this.defaultBlockState().setValue(AGE, 0).setValue(WATERLOGGED, true), 3);

          // Play sound when spawning a new sponge
          level.playSound(
              null,
              spawnPos,
              SoundEvents.WET_GRASS_PLACE,
              SoundSource.BLOCKS,
              0.3f,
              0.9f + random.nextFloat() * 0.2f);

          break;
        }
      }
    }
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity) {
      // Apply slowness when inside the sponge
      entity.makeStuckInBlock(state, new Vec3(0.8D, 0.75D, 0.8D));

      // If fully grown, apply water breathing effect
      if (state.getValue(AGE) == 3) {
        ((LivingEntity) entity)
            .addEffect(
                new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true));
      }
    }
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    // Add bubble particles when underwater
    if (state.getValue(WATERLOGGED) && random.nextInt(10) == 0) {
      double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
      double y = pos.getY() + 0.1;
      double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5;

      level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0.1, 0);
    }
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    BlockPos belowPos = pos.below();
    BlockState belowState = level.getBlockState(belowPos);

    // Can grow on various underwater surfaces
    return belowState.isFaceSturdy(level, belowPos, Direction.UP)
        || belowState.is(Blocks.CLAY)
        || belowState.is(Blocks.GRAVEL)
        || belowState.is(Blocks.SAND)
        || belowState.is(Blocks.PRISMARINE)
        || belowState.is(Blocks.PRISMARINE_BRICKS)
        || belowState.is(Blocks.DARK_PRISMARINE)
        || belowState.is(Blocks.SEA_LANTERN);
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

    // Die if not waterlogged
    if (!state.getValue(WATERLOGGED) || !canSurvive(state, level, pos)) {
      return Blocks.WATER.defaultBlockState();
    }

    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
    return false;
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return state.getValue(AGE) < 3;
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    int age = state.getValue(AGE);
    if (age < 3) {
      int newAge = Math.min(age + 1 + random.nextInt(2), 3);
      level.setBlock(pos, state.setValue(AGE, newAge), 3);

      // Play sound when bonemealed
      level.playSound(
          null,
          pos,
          SoundEvents.WET_GRASS_PLACE,
          SoundSource.BLOCKS,
          0.5f,
          0.8f + random.nextFloat() * 0.4f);

      // Try to spawn a new sponge when fully grown
      if (newAge == 3) {
        trySpawnNewSponge(level, pos, random);
      }
    }
  }

  @Override
  public void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (!level.isClientSide) {
      // Play sound when placed
      level.playSound(
          null,
          pos,
          SoundEvents.WET_GRASS_PLACE,
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
          SoundEvents.WET_GRASS_BREAK,
          SoundSource.BLOCKS,
          0.7f,
          0.9f + level.random.nextFloat() * 0.2f);
    }

    super.onRemove(state, level, pos, newState, isMoving);
  }
}
