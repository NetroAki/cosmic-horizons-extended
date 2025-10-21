package com.netroaki.chex.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class SporeSicknessEffect extends MobEffect {
  private static final String SPEED_UUID = "7107DE5E-7CE8-4030-940E-514C1F160890";
  private static final String DAMAGE_UUID = "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC";

  public SporeSicknessEffect() {
    super(
        MobEffectCategory.HARMFUL, // Harmful effect
        0x55FF55 // Green color
        );

    // Add attribute modifiers
    this.addAttributeModifier(
        Attributes.MOVEMENT_SPEED,
        SPEED_UUID,
        -0.15, // 15% slowness
        AttributeModifier.Operation.MULTIPLY_TOTAL);

    this.addAttributeModifier(
        Attributes.ATTACK_DAMAGE,
        DAMAGE_UUID,
        -0.1, // 10% damage reduction
        AttributeModifier.Operation.MULTIPLY_TOTAL);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    // Apply visual effects
    if (entity.level().isClientSide) {
      spawnParticles(entity);
    } else {
      // Server-side effects
      if (entity.getRandom().nextFloat() < 0.1f) {
        // Occasionally play a coughing sound
        entity
            .level()
            .playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                net.minecraft.sounds.SoundEvents.HUSK_AMBIENT,
                net.minecraft.sounds.SoundSource.AMBIENT,
                0.5f,
                0.8f + entity.getRandom().nextFloat() * 0.4f);
      }
    }
  }

  private void spawnParticles(LivingEntity entity) {
    // Only spawn particles for players or when specifically configured
    if (!(entity instanceof Player) && entity.tickCount % 10 != 0) {
      return;
    }

    // Spawn particles around the entity's head
    for (int i = 0; i < 3; i++) {
      double offsetX = (entity.getRandom().nextDouble() - 0.5) * 0.5;
      double offsetY = entity.getRandom().nextDouble() * 1.5 + 1.0;
      double offsetZ = (entity.getRandom().nextDouble() - 0.5) * 0.5;

      entity
          .level()
          .addParticle(
              net.minecraft.core.particles.ParticleTypes.ASH,
              entity.getX() + offsetX,
              entity.getY() + offsetY,
              entity.getZ() + offsetZ,
              0.0,
              0.0,
              0.0);
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    // Apply effect every tick
    return true;
  }

  @Override
  public boolean isInstantenous() {
    return false;
  }
}
