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

public class WindHazard {

  private static final int WIND_RADIUS = 12;
  private static final int WIND_HEIGHT = 8;
  private static final int PARTICLE_COOLDOWN = 3;
  private static final int SOUND_COOLDOWN = 60;
  private static final double WIND_STRENGTH = 0.2D;

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

    // Create wind particles
    if (random.nextInt(PARTICLE_COOLDOWN) == 0) {
      for (int i = 0; i < 6; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * WIND_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * WIND_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * WIND_RADIUS * 2;

        level.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }

    // Create wind stream particles
    if (random.nextInt(PARTICLE_COOLDOWN * 2) == 0) {
      for (int i = 0; i < 4; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * WIND_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * WIND_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * WIND_RADIUS * 2;

        level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }

    // Create wind gust particles
    if (random.nextInt(PARTICLE_COOLDOWN * 3) == 0) {
      for (int i = 0; i < 3; i++) {
        double x = centerPos.getX() + 0.5D + (random.nextDouble() - 0.5D) * WIND_RADIUS * 2;
        double y = centerPos.getY() + random.nextDouble() * WIND_HEIGHT;
        double z = centerPos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * WIND_RADIUS * 2;

        level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  private static void tickServerEffects(Level level, BlockPos centerPos) {
    AABB windArea =
        new AABB(
            centerPos.getX() - WIND_RADIUS,
            centerPos.getY(),
            centerPos.getZ() - WIND_RADIUS,
            centerPos.getX() + WIND_RADIUS,
            centerPos.getY() + WIND_HEIGHT,
            centerPos.getZ() + WIND_RADIUS);

    for (Player player : level.getEntitiesOfClass(Player.class, windArea)) {
      if (player != null) {
        // Apply wind effects
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 40, 0, false, false));

        // Apply wind velocity
        Vec3 currentVelocity = player.getDeltaMovement();
        Vec3 windDirection = getWindDirection(level, centerPos, player.blockPosition());
        player.setDeltaMovement(
            currentVelocity.x + windDirection.x * WIND_STRENGTH,
            currentVelocity.y + windDirection.y * WIND_STRENGTH,
            currentVelocity.z + windDirection.z * WIND_STRENGTH);

        // Play wind sound occasionally
        if (level.random.nextInt(SOUND_COOLDOWN) == 0) {
          level.playSound(
              null,
              player.blockPosition(),
              SoundEvents.WEATHER_RAIN_ABOVE,
              SoundSource.AMBIENT,
              0.4F,
              0.6F + level.random.nextFloat() * 0.8F);
        }
      }
    }
  }

  private static Vec3 getWindDirection(Level level, BlockPos centerPos, BlockPos playerPos) {
    // Create a circular wind pattern
    double angle =
        Math.atan2(playerPos.getZ() - centerPos.getZ(), playerPos.getX() - centerPos.getX());
    double x = Math.cos(angle + Math.PI / 2) * 0.5D;
    double z = Math.sin(angle + Math.PI / 2) * 0.5D;
    double y = (level.random.nextDouble() - 0.5D) * 0.3D;

    return new Vec3(x, y, z);
  }

  public static boolean isWindSourceBlock(BlockState state) {
    // Check if the block should create wind effects
    return state.is(Blocks.AIR)
        || state.is(Blocks.CAVE_AIR)
        || state.is(Blocks.VOID_AIR)
        || state.getBlock().getDescriptionId().contains("cloudstone")
        || state.getBlock().getDescriptionId().contains("sky");
  }

  public static boolean shouldCreateWindHazard(Level level, BlockPos pos) {
    // Check if there's a suitable wind source nearby
    for (int x = -3; x <= 3; x++) {
      for (int y = -1; y <= 2; y++) {
        for (int z = -3; z <= 3; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          BlockState state = level.getBlockState(checkPos);

          if (isWindSourceBlock(state)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public static int getWindIntensity(Level level, BlockPos pos) {
    int intensity = 0;

    // Count wind sources in a 7x4x7 area
    for (int x = -3; x <= 3; x++) {
      for (int y = -1; y <= 2; y++) {
        for (int z = -3; z <= 3; z++) {
          BlockPos checkPos = pos.offset(x, y, z);
          BlockState state = level.getBlockState(checkPos);

          if (isWindSourceBlock(state)) {
            intensity++;
          }
        }
      }
    }

    return Math.min(intensity, 8); // Cap at 8 for performance
  }

  public static void createWindGust(Level level, BlockPos pos, int intensity) {
    if (level.isClientSide) return;

    AABB gustArea =
        new AABB(
            pos.getX() - WIND_RADIUS,
            pos.getY(),
            pos.getZ() - WIND_RADIUS,
            pos.getX() + WIND_RADIUS,
            pos.getY() + WIND_HEIGHT,
            pos.getZ() + WIND_RADIUS);

    for (Player player : level.getEntitiesOfClass(Player.class, gustArea)) {
      if (player != null) {
        // Apply intense wind effects
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 100, 1, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 80, 0, false, false));

        // Apply strong wind velocity
        Vec3 windDirection = getWindDirection(level, pos, player.blockPosition());
        player.setDeltaMovement(
            player.getDeltaMovement().x + windDirection.x * WIND_STRENGTH * intensity,
            player.getDeltaMovement().y + windDirection.y * WIND_STRENGTH * intensity,
            player.getDeltaMovement().z + windDirection.z * WIND_STRENGTH * intensity);

        // Play wind gust sound
        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.WEATHER_RAIN_ABOVE,
            SoundSource.AMBIENT,
            0.8F,
            0.4F);
      }
    }

    // Create gust particles
    RandomSource random = level.random;
    for (int i = 0; i < 40; i++) {
      double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * WIND_RADIUS * 2;
      double y = pos.getY() + random.nextDouble() * WIND_HEIGHT;
      double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * WIND_RADIUS * 2;

      level.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0D, 0.0D, 0.0D);
    }
  }
}
