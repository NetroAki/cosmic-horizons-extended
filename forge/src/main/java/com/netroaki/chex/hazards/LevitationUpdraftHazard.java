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
import net.minecraft.world.phys.Vec3;

public class LevitationUpdraftHazard {

  private static final int UPDRAFT_RADIUS = 8;
  private static final int UPDRAFT_HEIGHT = 16;
  private static final double UPDRAFT_STRENGTH = 0.3D;
  private static final int PARTICLE_COOLDOWN = 5;
  private static final int SOUND_COOLDOWN = 40;

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

    // Create updraft particles
    if (random.nextInt(PARTICLE_COOLDOWN) == 0) {
      for (int i = 0; i < 3; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * UPDRAFT_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * UPDRAFT_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * UPDRAFT_RADIUS * 2;

        level.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0D, 0.1D, 0.0D);
      }
    }

    // Create wind particles
    if (random.nextInt(PARTICLE_COOLDOWN * 2) == 0) {
      for (int i = 0; i < 2; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * UPDRAFT_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * UPDRAFT_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * UPDRAFT_RADIUS * 2;

        level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.05D, 0.0D);
      }
    }
  }

  private static void tickServerEffects(Level level, BlockPos centerPos) {
    AABB updraftArea =
        new AABB(
            centerPos.getX() - UPDRAFT_RADIUS,
            centerPos.getY(),
            centerPos.getZ() - UPDRAFT_RADIUS,
            centerPos.getX() + UPDRAFT_RADIUS,
            centerPos.getY() + UPDRAFT_HEIGHT,
            centerPos.getZ() + UPDRAFT_RADIUS);

    for (Player player : level.getEntitiesOfClass(Player.class, updraftArea)) {
      if (player != null) {
        // Apply levitation effect
        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0, false, false));

        // Apply upward velocity
        Vec3 currentVelocity = player.getDeltaMovement();
        player.setDeltaMovement(
            currentVelocity.x, currentVelocity.y + UPDRAFT_STRENGTH, currentVelocity.z);

        // Play wind sound occasionally
        if (level.random.nextInt(SOUND_COOLDOWN) == 0) {
          level.playSound(
              null,
              player.blockPosition(),
              SoundEvents.WEATHER_RAIN_ABOVE,
              SoundSource.AMBIENT,
              0.3F,
              0.8F + level.random.nextFloat() * 0.4F);
        }
      }
    }
  }

  public static boolean isUpdraftBlock(BlockState state) {
    // Check if the block should create an updraft
    return state.is(Blocks.AIR)
        || state.is(Blocks.CAVE_AIR)
        || state.is(Blocks.VOID_AIR)
        || state.getBlock().getDescriptionId().contains("cloudstone");
  }

  public static boolean shouldCreateUpdraft(Level level, BlockPos pos) {
    // Check if there's a suitable updraft source below
    for (int i = 1; i <= 3; i++) {
      BlockPos checkPos = pos.below(i);
      BlockState state = level.getBlockState(checkPos);

      if (state.is(Blocks.MAGMA_BLOCK)
          || state.getBlock().getDescriptionId().contains("pandorite")
          || state.getBlock().getDescriptionId().contains("cloudstone")) {
        return true;
      }
    }

    return false;
  }
}
