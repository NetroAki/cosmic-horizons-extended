package com.netroaki.chex.world.hazard;

import com.netroaki.chex.registry.CHEXEffects;
import com.netroaki.chex.registry.CHEXParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SporeCloudHazard extends HazardZone {
  private static final float DAMAGE = 1.0f;
  private static final int DURATION = 20 * 30; // 30 seconds
  private static final float SPORE_EFFECT_CHANCE = 0.1f;
  private static final int PARTICLE_COUNT = 50;

  private int age = 0;
  private final int maxAge;

  public SporeCloudHazard(BlockPos center, float radius) {
    this(center, radius, DURATION);
  }

  public SporeCloudHazard(BlockPos center, float radius, int maxAge) {
    super(center, radius, DAMAGE, 10); // Check every 10 ticks (0.5 seconds)
    this.maxAge = maxAge;
  }

  @Override
  protected boolean isAffected(Entity entity) {
    // Only affect living entities that aren't immune to spores
    return entity instanceof LivingEntity
        && !(entity instanceof Player && ((Player) entity).isCreative());
  }

  @Override
  protected void affectEntity(Entity entity, float damageAmount) {
    if (entity instanceof LivingEntity living) {
      // Apply damage
      if (damageAmount > 0) {
        living.hurt(entity.damageSources().magic(), damageAmount);
      }

      // Chance to apply spore effect
      if (entity.level().random.nextFloat() < SPORE_EFFECT_CHANCE) {
        living.addEffect(
            new MobEffectInstance(
                CHEXEffects.SPORE_SICKNESS.get(),
                20 * 15, // 15 seconds
                0, // Amplifier 0
                false, // No particles (we'll handle them)
                true // Show icon
                ));
      }
    }
  }

  @Override
  protected void updateHazard(Level level) {
    // Update age and remove if expired
    if (++age >= maxAge) {
      // Mark for removal
      age = maxAge;
      return;
    }

    // Visual and sound effects
    if (level instanceof ServerLevel serverLevel) {
      // Play ambient sound occasionally
      if (age % 40 == 0) {
        float volume = 0.5f + level.random.nextFloat() * 0.5f;
        float pitch = 0.8f + level.random.nextFloat() * 0.4f;
        serverLevel.playSound(
            null,
            center.getX() + 0.5,
            center.getY() + 0.5,
            center.getZ() + 0.5,
            SoundEvents.FIRE_AMBIENT,
            SoundSource.AMBIENT,
            volume,
            pitch);
      }

      // Spawn particles
      for (int i = 0; i < PARTICLE_COUNT; i++) {
        double offsetX = (level.random.nextDouble() - 0.5) * radius * 2;
        double offsetY = (level.random.nextDouble() - 0.5) * radius * 1.5;
        double offsetZ = (level.random.nextDouble() - 0.5) * radius * 2;

        // Only spawn particles within the spherical volume
        if (offsetX * offsetX + offsetY * offsetY * 0.5 + offsetZ * offsetZ > radius * radius) {
          continue;
        }

        double x = center.getX() + 0.5 + offsetX;
        double y = center.getY() + 0.5 + offsetY;
        double z = center.getZ() + 0.5 + offsetZ;

        // Choose particle type (spore or mycelium)
        if (level.random.nextFloat() < 0.7f) {
          // Main spore particle
          serverLevel.sendParticles(
              CHEXParticleTypes.SPORE_PARTICLE.get(),
              x,
              y,
              z, // Position
              1, // Count
              0,
              0,
              0, // Random offset
              0.05 // Speed
              );
        } else {
          // Secondary particle effect
          serverLevel.sendParticles(
              ParticleTypes.MYCELIUM,
              x,
              y,
              z, // Position
              1, // Count
              0.1,
              0.1,
              0.1, // Random offset
              0.05 // Speed
              );
        }
      }
    }
  }

  @Override
  public boolean isActive() {
    return age < maxAge;
  }

  public float getAgeRatio() {
    return (float) age / maxAge;
  }

  public static void createSporeCloud(Level level, BlockPos center, float radius) {
    if (!level.isClientSide) {
      HazardManager.addHazardZone(level, new SporeCloudHazard(center, radius));
    }
  }

  public static void createSporeCloud(Level level, BlockPos center, float radius, int duration) {
    if (!level.isClientSide) {
      HazardManager.addHazardZone(level, new SporeCloudHazard(center, radius, duration));
    }
  }
}
