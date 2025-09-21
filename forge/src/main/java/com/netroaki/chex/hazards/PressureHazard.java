package com.netroaki.chex.hazards;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class PressureHazard {

  private static final int PRESSURE_RADIUS = 8;
  private static final int PRESSURE_HEIGHT = 12;
  private static final int PARTICLE_COOLDOWN = 4;
  private static final int SOUND_COOLDOWN = 100;
  private static final int PRESSURE_DAMAGE_COOLDOWN = 60; // 3 seconds
  private static final float PRESSURE_DAMAGE = 0.8F;

  public static void tick(Level level, BlockPos centerPos) {
    if (level.isClientSide) {
      // Client-side particle effects
      tickClientEffects(level, centerPos);
    } else {
      // Server-side player effects
      tickServerEffects(level, centerPos);
    }
  }

  private static void tickClientEffects(Level level, BlockPos centerPos) {
    RandomSource random = level.random;

    // Create pressure bubble particles
    if (random.nextInt(PARTICLE_COOLDOWN) == 0) {
      for (int i = 0; i < 6; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * PRESSURE_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * PRESSURE_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * PRESSURE_RADIUS * 2;

        level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0.0D, 0.1D, 0.0D);
      }
    }

    // Create water pressure particles
    if (random.nextInt(PARTICLE_COOLDOWN * 2) == 0) {
      for (int i = 0; i < 4; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * PRESSURE_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * PRESSURE_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * PRESSURE_RADIUS * 2;

        level.addParticle(ParticleTypes.SPLASH, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  private static void tickServerEffects(Level level, BlockPos centerPos) {
    AABB pressureArea =
        new AABB(
            centerPos.getX() - PRESSURE_RADIUS,
            centerPos.getY(),
            centerPos.getZ() - PRESSURE_RADIUS,
            centerPos.getX() + PRESSURE_RADIUS,
            centerPos.getY() + PRESSURE_HEIGHT,
            centerPos.getZ() + PRESSURE_RADIUS);

    for (Player player : level.getEntitiesOfClass(Player.class, pressureArea)) {
      if (player != null) {
        // Apply pressure effects
        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 0, false, false));

        // Apply pressure damage if not protected
        if (!player.hasEffect(MobEffects.WATER_BREATHING)
            && player.tickCount % PRESSURE_DAMAGE_COOLDOWN == 0) {
          player.hurt(level.damageSources().drown(), PRESSURE_DAMAGE);
        }

        // Play pressure sound occasionally
        if (level.random.nextInt(SOUND_COOLDOWN) == 0) {
          level.playSound(
              null,
              player.blockPosition(),
              SoundEvents.ELDER_GUARDIAN_AMBIENT,
              SoundSource.AMBIENT,
              0.2F,
              0.8F + level.random.nextFloat() * 0.4F);
        }
      }
    }
  }

  public static boolean isPressureSourceBlock(BlockState state) {
    // Check if the block should create pressure effects
    return state.is(Blocks.WATER)
        || state.is(Blocks.KELP)
        || state.is(Blocks.SEAGRASS)
        || state.getBlock().getDescriptionId().contains("lumicoral")
        || state.getBlock().getDescriptionId().contains("ocean");
  }

  public static boolean shouldCreatePressureHazard(Level level, BlockPos pos) {
    // Check if there's a suitable pressure source nearby
    for (int x = -2; x <= 2; x++) {
      for (int y = -2; y <= 2; y++) {
        for (int z = -2; z <= 2; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          BlockState state = level.getBlockState(checkPos);

          if (isPressureSourceBlock(state)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public static int getPressureIntensity(Level level, BlockPos pos) {
    int intensity = 0;

    // Count pressure sources in a 5x5x5 area
    for (int x = -2; x <= 2; x++) {
      for (int y = -2; y <= 2; y++) {
        for (int z = -2; z <= 2; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          BlockState state = level.getBlockState(checkPos);

          if (isPressureSourceBlock(state)) {
            intensity++;
          }
        }
      }
    }

    return Math.min(intensity, 6); // Cap at 6 for performance
  }
}
