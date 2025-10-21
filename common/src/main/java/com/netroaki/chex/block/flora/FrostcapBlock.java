package com.netroaki.chex.block.flora;

import javax.annotation.Nullable;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FrostcapBlock extends BushBlock implements BonemealableBlock {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

  private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D);
  private static final VoxelShape SHAPE_GLOWING = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 11.0D, 13.0D);

  public FrostcapBlock() {
    super(
        Properties.of()
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .sound(SoundType.FUNGUS)
            .noOcclusion()
            .instabreak()
            .lightLevel(state -> state.getValue(GLOWING) ? 5 : 0)
            .offsetType(Block.OffsetType.XZ));
    this.registerDefaultState(
        this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(GLOWING, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, GLOWING);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return state.getValue(GLOWING) ? SHAPE_GLOWING : SHAPE;
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    super.randomTick(state, level, pos, random);

    // Chance to start or stop glowing based on light level
    if (random.nextInt(10) == 0) {
      int light = level.getMaxLocalRawBrightness(pos);
      boolean shouldGlow = light < 8 && random.nextFloat() < 0.3f;

      if (state.getValue(GLOWING) != shouldGlow) {
        level.setBlock(pos, state.setValue(GLOWING, shouldGlow), 3);

        // Play sound when changing state
        level.playSound(
            null,
            pos,
            shouldGlow ? SoundEvents.AMETHYST_BLOCK_CHIME : SoundEvents.SNOW_STEP,
            SoundSource.BLOCKS,
            0.5f,
            shouldGlow ? 0.8f : 1.2f);
      }
    }

    // Try to spread to nearby blocks when glowing
    if (state.getValue(GLOWING) && random.nextInt(15) == 0) {
      trySpread(level, pos, random);
    }
  }

  private void trySpread(ServerLevel level, BlockPos pos, RandomSource random) {
    // Try to spread to adjacent blocks
    BlockPos spreadPos =
        pos.offset(
            random.nextInt(3) - 1, // -1 to 1
            0, // Same Y level
            random.nextInt(3) - 1 // -1 to 1
            );

    if (canSurvive(level.getBlockState(spreadPos.below()), level, spreadPos.below())
        && level.isEmptyBlock(spreadPos)) {

      // 50% chance to be glowing when spreading
      boolean shouldGlow = random.nextBoolean();
      level.setBlock(
          spreadPos,
          this.defaultBlockState()
              .setValue(FACING, Direction.from2DDataValue(random.nextInt(4)))
              .setValue(GLOWING, shouldGlow),
          3);

      // Play spread sound
      level.playSound(
          null,
          spreadPos,
          shouldGlow ? SoundEvents.AMETHYST_BLOCK_CHIME : SoundEvents.SNOW_PLACE,
          SoundSource.BLOCKS,
          0.3f,
          0.8f + random.nextFloat() * 0.4f);
    }
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (state.getValue(GLOWING)) {
      // Add sparkle particles
      if (random.nextInt(5) == 0) {
        double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
        double y = pos.getY() + 0.5 + random.nextDouble() * 0.5;
        double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5;

        level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0.05, 0);
      }

      // Occasionally play a chime sound
      if (random.nextInt(100) == 0) {
        level.playLocalSound(
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            SoundEvents.AMETHYST_BLOCK_CHIME,
            SoundSource.BLOCKS,
            0.3f,
            1.0f + random.nextFloat() * 0.3f,
            false);
      }
    }
  }

  @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
      // Apply effects when glowing
      if (state.getValue(GLOWING)) {
        livingEntity.addEffect(
            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, false, true));

        // Small chance to freeze the entity
        if (level.random.nextInt(20) == 0) {
          livingEntity.addEffect(
              new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 1, false, false, true));

          // Play freeze sound
          level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 0.5f, 1.5f);
        }
      }
    }

    // Slow down movement when inside the block
    Vec3 vec3 = entity.getDeltaMovement();
    entity.setDeltaMovement(vec3.multiply(0.9D, 1.0D, 0.9D));
  }

  @Override
  public BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      LevelAccessor level,
      BlockPos pos,
      BlockPos neighborPos) {
    return !state.canSurvive(level, pos)
        ? Blocks.AIR.defaultBlockState()
        : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    BlockPos belowPos = pos.below();
    BlockState belowState = level.getBlockState(belowPos);

    // Can grow on various cold blocks
    return belowState.is(Blocks.SNOW_BLOCK)
        || belowState.is(Blocks.ICE)
        || belowState.is(Blocks.PACKED_ICE)
        || belowState.is(Blocks.BLUE_ICE)
        || belowState.is(Blocks.DIRT)
        || belowState.is(Blocks.GRASS_BLOCK)
        || belowState.is(Blocks.PODZOL)
        || belowState.is(Blocks.MYCELIUM)
        || belowState.is(Blocks.STONE)
        || belowState.is(Blocks.ANDESITE)
        || belowState.is(Blocks.DIORITE)
        || belowState.is(Blocks.GRANITE)
        || belowState.is(Blocks.TUFF);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState()
        .setValue(FACING, context.getHorizontalDirection().getOpposite())
        .setValue(GLOWING, context.getLevel().getRandom().nextFloat() < 0.3f);
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    // Toggle glowing state when bonemealed
    boolean newGlowing = !state.getValue(GLOWING);
    level.setBlock(pos, state.setValue(GLOWING, newGlowing), 3);

    // Play sound based on new state
    level.playSound(
        null,
        pos,
        newGlowing ? SoundEvents.AMETHYST_BLOCK_CHIME : SoundEvents.SNOW_STEP,
        SoundSource.BLOCKS,
        0.5f,
        newGlowing ? 0.8f : 1.2f);

    // Try to spread when bonemealed
    trySpread(level, pos, random);
  }
}
