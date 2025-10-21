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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FrostMossBlock extends Block implements BonemealableBlock {
  public static final int MAX_AGE = 3;
  public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
  public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
  private static final VoxelShape[] SHAPE_BY_AGE =
      new VoxelShape[] {
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), // Age 0
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), // Age 1
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D), // Age 2
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D) // Age 3 (fully grown)
      };

  public FrostMossBlock() {
    super(
        Properties.of()
            .mapColor(
                blockState ->
                    blockState.getValue(AGE) == MAX_AGE
                        ? net.minecraft.world.level.material.MapColor.SNOW
                        : net.minecraft.world.level.material.MapColor.ICE)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.MOSS_CARPET)
            .offsetType(Block.OffsetType.XZ));
    this.registerDefaultState(
        this.stateDefinition.any().setValue(AGE, 0).setValue(PERSISTENT, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE, PERSISTENT);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE_BY_AGE[state.getValue(AGE)];
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (!state.getValue(PERSISTENT) && level.getRandom().nextInt(5) == 0) {
      // TODO: Implement grow method when available
      // this.grow(level, random, pos, state);
    }

    // Try to spread to nearby blocks
    if (random.nextInt(10) == 0) {
      this.trySpread(level, pos, random);
    }

    // Occasionally add frost particles
    if (random.nextInt(20) == 0) {
      double x = pos.getX() + random.nextDouble();
      double y = pos.getY() + 0.1;
      double z = pos.getZ() + random.nextDouble();
      level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0, 0);
    }
  }

  private void trySpread(Level level, BlockPos pos, RandomSource random) {
    BlockPos spreadPos =
        pos.offset(
            random.nextInt(3) - 1, // -1 to 1
            random.nextInt(2) - 1, // -1 to 0
            random.nextInt(3) - 1 // -1 to 1
            );

    if (canSpreadTo(level, spreadPos)) {
      level.setBlock(spreadPos, this.defaultBlockState(), 2);

      // Play spread sound
      level.playSound(
          null,
          spreadPos,
          SoundEvents.MOSS_CARPET_PLACE,
          SoundSource.BLOCKS,
          0.3F,
          0.8F + random.nextFloat() * 0.4F);
    }
  }

  private boolean canSpreadTo(LevelReader level, BlockPos pos) {
    BlockState state = level.getBlockState(pos);
    BlockState below = level.getBlockState(pos.below());

    return state.isAir()
        && (below.is(Blocks.STONE)
            || below.is(Blocks.ICE)
            || below.is(Blocks.PACKED_ICE)
            || below.is(Blocks.BLUE_ICE)
            || below.is(Blocks.SNOW_BLOCK))
        && level.getMaxLocalRawBrightness(pos) <= 10; // Only spread in dim light
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return state.getValue(AGE) < MAX_AGE;
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
    if (age < MAX_AGE) {
      level.setBlock(pos, state.setValue(AGE, age + 1), 2);
    } else {
      // If already fully grown, try to spread
      this.trySpread(level, pos, random);
    }
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    // Slow down entities walking on the moss
    if (entity instanceof LivingEntity) {
      entity.makeStuckInBlock(state, new Vec3(0.8D, 0.75D, 0.8D));

      // Apply slowness if fully grown
      if (state.getValue(AGE) == MAX_AGE && !level.isClientSide) {
        ((LivingEntity) entity)
            .addEffect(
                new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, false, true));
      }
    }
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    BlockPos belowPos = pos.below();
    return canSupportRigidBlock(level, belowPos) || canSupportCenter(level, belowPos, Direction.UP);
  }

  @Override
  public void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (!level.isClientSide && !state.is(state.getBlock())) {
      // Play frosty sound when placed
      level.playSound(
          null,
          pos,
          SoundEvents.SNOW_PLACE,
          SoundSource.BLOCKS,
          0.3F,
          0.6F + level.random.nextFloat() * 0.4F);
    }
  }
}
