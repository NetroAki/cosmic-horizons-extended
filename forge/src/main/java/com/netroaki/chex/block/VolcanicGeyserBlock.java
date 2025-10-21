package com.netroaki.chex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class VolcanicGeyserBlock extends Block {
  private static final int ERUPT_COOLDOWN = 200; // 10 seconds at 20 ticks per second
  private static final int ERUPTION_DURATION = 40; // 2 seconds
  private static final double ERUPTION_HEIGHT = 5.0D;

  public VolcanicGeyserBlock() {
    super(
        Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(3.0f, 10.0f)
            .requiresCorrectToolForDrops()
            .lightLevel(state -> state.getValue(ERUPTING) ? 10 : 3));

    this.registerDefaultState(
        this.stateDefinition.any().setValue(ERUPTING, false).setValue(ERUPTION_TICKS, 0));
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (state.getValue(ERUPTING)) {
      // During eruption, create a column of steam and bubbles
      int eruptionTicks = state.getValue(ERUPTION_TICKS);
      double progress = (double) eruptionTicks / ERUPTION_DURATION;
      double height = ERUPTION_HEIGHT * Math.sin(progress * Math.PI);

      for (int i = 0; i < 10; i++) {
        double offsetX = (random.nextDouble() - 0.5) * 0.5;
        double offsetZ = (random.nextDouble() - 0.5) * 0.5;

        level.addParticle(
            ParticleTypes.CLOUD,
            pos.getX() + 0.5 + offsetX,
            pos.getY() + 1.0 + height * random.nextDouble(),
            pos.getZ() + 0.5 + offsetZ,
            0,
            0.1,
            0);

        if (random.nextBoolean()) {
          level.addParticle(
              ParticleTypes.BUBBLE_COLUMN_UP,
              pos.getX() + 0.5 + offsetX * 0.5,
              pos.getY() + 1.0,
              pos.getZ() + 0.5 + offsetZ * 0.5,
              0,
              0.2,
              0);
        }
      }

      // Play sound effect
      if (eruptionTicks % 10 == 0) {
        level.playLocalSound(
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            SoundEvents.LAVA_AMBIENT,
            SoundSource.BLOCKS,
            0.5f + random.nextFloat() * 0.5f,
            0.8f + random.nextFloat() * 0.4f,
            false);
      }
    } else if (random.nextInt(20) == 0) {
      // Idle steam particles
      level.addParticle(
          ParticleTypes.CAMPFIRE_COSY_SMOKE,
          pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5,
          pos.getY() + 1.0,
          pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5,
          0,
          0.05,
          0);
    }
  }

  @Override
  public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
    if (state.getValue(ERUPTING) && entity instanceof LivingEntity) {
      // Launch entities during eruption
      double yMotion = 0.5 + (double) state.getValue(ERUPTION_TICKS) / ERUPTION_DURATION;
      entity.setDeltaMovement(
          entity.getDeltaMovement().x() * 0.5,
          Math.min(yMotion, 1.5),
          entity.getDeltaMovement().z() * 0.5);
      entity.hurtMarked = true;
    }
    super.stepOn(level, pos, state, entity);
  }

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (state.getValue(ERUPTING)) {
      int ticks = state.getValue(ERUPTION_TICKS) + 1;

      if (ticks >= ERUPTION_DURATION) {
        // End eruption
        level.setBlock(pos, state.setValue(ERUPTING, false).setValue(ERUPTION_TICKS, 0), 3);
        level.scheduleTick(pos, this, ERUPT_COOLDOWN + random.nextInt(100));
      } else {
        // Continue eruption
        level.setBlock(pos, state.setValue(ERUPTION_TICKS, ticks), 3);
        level.scheduleTick(pos, this, 1);

        // Create hot spring water source blocks above
        if (ticks % 5 == 0) {
          BlockPos waterPos = pos.above();
          if (level.isEmptyBlock(waterPos)) {
            level.setBlock(waterPos, Blocks.WATER.defaultBlockState(), 3);
          }
        }
      }
    } else if (random.nextInt(100) == 0) {
      // Start new eruption
      level.setBlock(pos, state.setValue(ERUPTING, true).setValue(ERUPTION_TICKS, 0), 3);
      level.scheduleTick(pos, this, 1);

      // Play eruption sound
      level.playSound(
          null,
          pos,
          SoundEvents.GENERIC_EXPLODE,
          SoundSource.BLOCKS,
          1.0f,
          0.8f + random.nextFloat() * 0.4f);
    } else {
      // Schedule next check
      level.scheduleTick(pos, this, 20 + random.nextInt(20));
    }
  }

  @Override
  public void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (!level.isClientSide) {
      level.scheduleTick(pos, this, 100 + level.random.nextInt(100));
    }
  }

  // Register block states
  public static final BooleanProperty ERUPTING = BooleanProperty.create("erupting");
  public static final IntegerProperty ERUPTION_TICKS =
      IntegerProperty.create("eruption_ticks", 0, ERUPTION_DURATION);

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(ERUPTING, ERUPTION_TICKS);
  }
}
