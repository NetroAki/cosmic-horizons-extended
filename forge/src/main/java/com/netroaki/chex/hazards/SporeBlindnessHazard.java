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

public class SporeBlindnessHazard {

  private static final int SPORE_RADIUS = 10;
  private static final int SPORE_HEIGHT = 6;
  private static final int PARTICLE_COOLDOWN = 2;
  private static final int SOUND_COOLDOWN = 80;
  private static final int SPORE_DAMAGE_COOLDOWN = 40; // 2 seconds
  private static final float SPORE_DAMAGE = 0.5F;

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

    // Create spore particles
    if (random.nextInt(PARTICLE_COOLDOWN) == 0) {
      for (int i = 0; i < 8; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * SPORE_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * SPORE_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * SPORE_RADIUS * 2;

        level.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }

    // Create floating spore particles
    if (random.nextInt(PARTICLE_COOLDOWN * 2) == 0) {
      for (int i = 0; i < 4; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * SPORE_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * SPORE_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * SPORE_RADIUS * 2;

        level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }

    // Create glowing spore particles
    if (random.nextInt(PARTICLE_COOLDOWN * 3) == 0) {
      for (int i = 0; i < 3; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * SPORE_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * SPORE_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * SPORE_RADIUS * 2;

        level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  private static void tickServerEffects(Level level, BlockPos centerPos) {
    AABB sporeArea =
        new AABB(
            centerPos.getX() - SPORE_RADIUS,
            centerPos.getY(),
            centerPos.getZ() - SPORE_RADIUS,
            centerPos.getX() + SPORE_RADIUS,
            centerPos.getY() + SPORE_HEIGHT,
            centerPos.getZ() + SPORE_RADIUS);

    for (Player player : level.getEntitiesOfClass(Player.class, sporeArea)) {
      if (player != null) {
        // Apply spore effects
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, false));

        // Apply spore damage if not protected
        if (!player.hasEffect(MobEffects.BLINDNESS)
            && player.tickCount % SPORE_DAMAGE_COOLDOWN == 0) {
          player.hurt(level.damageSources().magic(), SPORE_DAMAGE);
        }

        // Play spore sound occasionally
        if (level.random.nextInt(SOUND_COOLDOWN) == 0) {
          level.playSound(
              null,
              player.blockPosition(),
              SoundEvents.BEE_LOOP,
              SoundSource.AMBIENT,
              0.3F,
              0.5F + level.random.nextFloat() * 0.5F);
        }
      }
    }
  }

  public static boolean isSporeSourceBlock(BlockState state) {
    // Check if the block should create spore effects
    return state.is(Blocks.MOSS_BLOCK)
        || state.is(Blocks.MOSS_CARPET)
        || state.getBlock().getDescriptionId().contains("spore")
        || state.getBlock().getDescriptionId().contains("biolume")
        || state.getBlock().getDescriptionId().contains("fungal");
  }

  public static boolean shouldCreateSporeHazard(Level level, BlockPos pos) {
    // Check if there's a suitable spore source nearby
    for (int x = -3; x <= 3; x++) {
      for (int y = -1; y <= 2; y++) {
        for (int z = -3; z <= 3; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          BlockState state = level.getBlockState(checkPos);

          if (isSporeSourceBlock(state)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public static int getSporeIntensity(Level level, BlockPos pos) {
    int intensity = 0;

    // Count spore sources in a 7x4x7 area
    for (int x = -3; x <= 3; x++) {
      for (int y = -1; y <= 2; y++) {
        for (int z = -3; z <= 3; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          BlockState state = level.getBlockState(checkPos);

          if (isSporeSourceBlock(state)) {
            intensity++;
          }
        }
      }
    }

    return Math.min(intensity, 8); // Cap at 8 for performance
  }

  public static void createSporeBurst(Level level, BlockPos pos, int intensity) {
    if (level.isClientSide) return;

    AABB burstArea =
        new AABB(
            pos.getX() - SPORE_RADIUS,
            pos.getY(),
            pos.getZ() - SPORE_RADIUS,
            pos.getX() + SPORE_RADIUS,
            pos.getY() + SPORE_HEIGHT,
            pos.getZ() + SPORE_RADIUS);

    for (Player player : level.getEntitiesOfClass(Player.class, burstArea)) {
      if (player != null) {
        // Apply intense spore effects
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 150, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 0, false, false));

        // Apply burst damage
        player.hurt(level.damageSources().magic(), SPORE_DAMAGE * intensity);
      }
    }
  }
}
