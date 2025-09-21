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

public class HeatAuraHazard {

  private static final int HEAT_RADIUS = 12;
  private static final int HEAT_HEIGHT = 8;
  private static final int DAMAGE_COOLDOWN = 20; // 1 second
  private static final int PARTICLE_COOLDOWN = 3;
  private static final int SOUND_COOLDOWN = 60;
  private static final float HEAT_DAMAGE = 1.0F;

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

    // Create heat particles
    if (random.nextInt(PARTICLE_COOLDOWN) == 0) {
      for (int i = 0; i < 5; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * HEAT_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * HEAT_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * HEAT_RADIUS * 2;

        level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }

    // Create flame particles
    if (random.nextInt(PARTICLE_COOLDOWN * 2) == 0) {
      for (int i = 0; i < 3; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * HEAT_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * HEAT_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * HEAT_RADIUS * 2;

        level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }

    // Create heat shimmer particles
    if (random.nextInt(PARTICLE_COOLDOWN * 3) == 0) {
      for (int i = 0; i < 2; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * HEAT_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * HEAT_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * HEAT_RADIUS * 2;

        level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  private static void tickServerEffects(Level level, BlockPos centerPos) {
    AABB heatArea =
        new AABB(
            centerPos.getX() - HEAT_RADIUS,
            centerPos.getY(),
            centerPos.getZ() - HEAT_RADIUS,
            centerPos.getX() + HEAT_RADIUS,
            centerPos.getY() + HEAT_HEIGHT,
            centerPos.getZ() + HEAT_RADIUS);

    for (Player player : level.getEntitiesOfClass(Player.class, heatArea)) {
      if (player != null) {
        // Apply heat effects
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, false));

        // Apply heat damage if not protected
        if (!player.hasEffect(MobEffects.FIRE_RESISTANCE)
            && player.tickCount % DAMAGE_COOLDOWN == 0) {
          player.hurt(level.damageSources().onFire(), HEAT_DAMAGE);
        }

        // Play heat sound occasionally
        if (level.random.nextInt(SOUND_COOLDOWN) == 0) {
          level.playSound(
              null,
              player.blockPosition(),
              SoundEvents.FIRE_AMBIENT,
              SoundSource.AMBIENT,
              0.4F,
              0.6F + level.random.nextFloat() * 0.8F);
        }
      }
    }
  }

  public static boolean isHeatSourceBlock(BlockState state) {
    // Check if the block should create a heat aura
    return state.is(Blocks.MAGMA_BLOCK)
        || state.is(Blocks.LAVA)
        || state.is(Blocks.FIRE)
        || state.getBlock().getDescriptionId().contains("pandorite")
        || state.getBlock().getDescriptionId().contains("volcanic");
  }

  public static boolean shouldCreateHeatAura(Level level, BlockPos pos) {
    // Check if there's a suitable heat source nearby
    for (int x = -2; x <= 2; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -2; z <= 2; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          BlockState state = level.getBlockState(checkPos);

          if (isHeatSourceBlock(state)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public static int getHeatIntensity(Level level, BlockPos pos) {
    int intensity = 0;

    // Count heat sources in a 5x5x3 area
    for (int x = -2; x <= 2; x++) {
      for (int y = -1; y <= 1; y++) {
        for (int z = -2; z <= 2; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          BlockState state = level.getBlockState(checkPos);

          if (isHeatSourceBlock(state)) {
            intensity++;
          }
        }
      }
    }

    return Math.min(intensity, 5); // Cap at 5 for performance
  }
}
