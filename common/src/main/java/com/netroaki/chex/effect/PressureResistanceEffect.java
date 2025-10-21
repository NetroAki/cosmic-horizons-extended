package com.netroaki.chex.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PressureResistanceEffect extends MobEffect {
  public PressureResistanceEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x1E90FF); // Dodger Blue color
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (entity instanceof Player player) {
      Level level = player.level();

      // Only apply in Aqua Mundus
      if (!level.dimension().location().getPath().equals("aqua_mundus")) {
        return;
      }

      // Create bubble particles around the player
      if (level.isClientSide && level.random.nextFloat() < 0.1f) {
        spawnBubbleParticles(level, player.position().x, player.getY(0.5), player.position().z);
      }
    }
  }

  private void spawnBubbleParticles(Level level, double x, double y, double z) {
    if (level.isClientSide) {
      // Create a ring of bubbles around the player
      for (int i = 0; i < 3; i++) {
        double angle = level.random.nextDouble() * Math.PI * 2;
        double offsetX = Math.cos(angle) * 0.5;
        double offsetZ = Math.sin(angle) * 0.5;

        level.addParticle(
            net.minecraft.core.particles.ParticleTypes.BUBBLE,
            x + offsetX,
            y + (level.random.nextDouble() * 1.5) - 0.75,
            z + offsetZ,
            0.0D,
            0.05D,
            0.0D);

        // Add some random bubbles around the player
        if (level.random.nextFloat() < 0.3f) {
          level.addParticle(
              net.minecraft.core.particles.ParticleTypes.BUBBLE,
              x + (level.random.nextDouble() - 0.5) * 0.8,
              y + (level.random.nextDouble() - 0.5) * 0.8,
              z + (level.random.nextDouble() - 0.5) * 0.8,
              (level.random.nextDouble() - 0.5) * 0.1,
              level.random.nextDouble() * 0.1 + 0.05,
              (level.random.nextDouble() - 0.5) * 0.1);
        }
      }
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return true; // Apply the effect every tick
  }
}
