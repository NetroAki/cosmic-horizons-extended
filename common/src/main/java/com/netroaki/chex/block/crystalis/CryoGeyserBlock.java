package com.netroaki.chex.block.crystalis;

import com.netroaki.chex.config.CrystalisHazardsConfig;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;

public class CryoGeyserBlock extends Block {
  public static final BooleanProperty ACTIVE = BlockStateProperties.TRIGGERED;
  private static final int ERUPTION_COOLDOWN = 200; // 10 seconds
  private static final int ERUPTION_DURATION = 100; // 5 seconds

  public CryoGeyserBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(ACTIVE);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (state.getValue(ACTIVE)) {
      // Active geyser particles
      for (int i = 0; i < 3; ++i) {
        double d0 = (double) pos.getX() + random.nextDouble();
        double d1 = (double) pos.getY() + 1.0D + random.nextDouble() * 0.5D;
        double d2 = (double) pos.getZ() + random.nextDouble();
        level.addParticle(ParticleTypes.SNOWFLAKE, d0, d1, d2, 0.0D, 0.1D, 0.0D);

        if (random.nextInt(5) == 0) {
          level.addParticle(ParticleTypes.CLOUD, d0, d1, d2, 0.0D, 0.1D, 0.0D);
        }
      }
    } else if (random.nextInt(10) == 0) {
      // Idle particles
      double d0 = (double) pos.getX() + random.nextDouble();
      double d1 = (double) pos.getY() + 1.0D;
      double d2 = (double) pos.getZ() + random.nextDouble();
      level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.1D, 0.0D);
    }
  }

  @Override
  public void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (!level.isClientSide) {
      level.scheduleTick(pos, this, ERUPTION_COOLDOWN + level.random.nextInt(100));
    }
  }

  // TODO: Fix when tick method is available
  // @Override
  public void tick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (!level.isClientSide) {
      if (!state.getValue(ACTIVE)) {
        // Start eruption
        level.setBlock(pos, state.setValue(ACTIVE, true), 3);
        level.playSound(
            null, pos, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.BLOCKS, 1.0F, 0.5F);
        // Encase nearby entities at eruption start (configurable)
        if (CrystalisHazardsConfig.GEYSER_ENCASE_ENABLED.get()) {
          encaseNearbyEntities(level, pos);
        }
        level.scheduleTick(pos, this, ERUPTION_DURATION);
      } else {
        // End eruption
        level.setBlock(pos, state.setValue(ACTIVE, false), 3);
        level.scheduleTick(pos, this, ERUPTION_COOLDOWN + random.nextInt(100));
      }
    }
  }

  // TODO: Fix when entityInside method is available
  // @Override
  public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (state.getValue(ACTIVE) && entity instanceof LivingEntity living) {
      // Apply effects to entities inside the geyser
      living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
      living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));

      // Push entities upward
      entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.5, 0));

      // Play sound
      if (entity.tickCount % 10 == 0) {
        level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 0.5F, 1.0F);
      }
    }
    super.entityInside(state, level, pos, entity);
  }

  @Override
  public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
    if (state.getValue(ACTIVE)) {
      // Freeze water blocks around the geyser
      if (!level.isClientSide) {
        freezeWater(level, pos);
      }
    }
    super.stepOn(level, pos, state, entity);
  }

  private void freezeWater(Level level, BlockPos pos) {
    // Check blocks in a 3x3 area around the geyser
    for (int x = -1; x <= 1; x++) {
      for (int z = -1; z <= 1; z++) {
        BlockPos checkPos = pos.offset(x, 0, z);
        if (level.getBlockState(checkPos).getBlock() == Blocks.WATER) {
          level.setBlockAndUpdate(checkPos, Blocks.ICE.defaultBlockState());
        } else if (level.getBlockState(checkPos).getBlock() == Blocks.ICE) {
          level.setBlockAndUpdate(checkPos, Blocks.PACKED_ICE.defaultBlockState());
        }
      }
    }
  }

  private void encaseNearbyEntities(Level level, BlockPos pos) {
    // Find living entities in a radius above the vent based on config
    double r = CrystalisHazardsConfig.GEYSER_ENCASE_RADIUS.get();
    if (r < 0.5D) r = 0.5D;
    AABB box = new AABB(pos).inflate(r, Math.max(2.0D, r + 1.0D), r);
    List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, box);
    for (LivingEntity target : entities) {
      // Positions around the entity (feet and head)
      BlockPos feet = target.blockPosition();
      BlockPos head = feet.above();
      tryPlaceFrostedIce(level, feet);
      tryPlaceFrostedIce(level, head);
      // Simple ring around
      for (int dx = -1; dx <= 1; dx++) {
        for (int dz = -1; dz <= 1; dz++) {
          if (Math.abs(dx) + Math.abs(dz) != 1) continue; // plus shape
          tryPlaceFrostedIce(level, feet.offset(dx, 0, dz));
          tryPlaceFrostedIce(level, head.offset(dx, 0, dz));
        }
      }
      // Small upward nudge to avoid suffocation inside solid blocks
      target.setDeltaMovement(target.getDeltaMovement().add(0, 0.2, 0));
    }
  }

  private void tryPlaceFrostedIce(Level level, BlockPos targetPos) {
    BlockState stateAt = level.getBlockState(targetPos);
    // Only replace air or water, and avoid tile entities/solid important blocks
    if (stateAt.isAir() || stateAt.is(Blocks.WATER)) {
      level.setBlockAndUpdate(targetPos, Blocks.FROSTED_ICE.defaultBlockState());
    }
  }
}
