package com.netroaki.chex.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class FrostbiteEffect extends MobEffect {
  private static final int DAMAGE_INTERVAL = 100; // Damage every 5 seconds (100 ticks)
  private static final float DAMAGE_AMOUNT = 1.0f;

  public FrostbiteEffect() {
    super(MobEffectCategory.HARMFUL, 0x9EE7F5); // Light blue color
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (!entity.level().isClientSide && entity.tickCount % DAMAGE_INTERVAL == 0) {
      // Apply damage based on amplifier (more severe frostbite)
      entity.hurt(entity.damageSources().freeze(), DAMAGE_AMOUNT * (amplifier + 1));

      // Apply additional effects at higher levels
      if (amplifier >= 1) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
      }
      if (amplifier >= 2) {
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 1));
      }
    }
  }

  @Override
  public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
    super.addAttributeModifiers(entity, attributes, amplifier);
    // Add visual effects when effect is applied
    if (!entity.level().isClientSide) {
      // TODO: Add frost particles around the player
    }
  }

  @Override
  public void removeAttributeModifiers(
      LivingEntity entity, AttributeMap attributes, int amplifier) {
    super.removeAttributeModifiers(entity, attributes, amplifier);
    // Clean up any effects when frostbite is removed
    entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
    entity.removeEffect(MobEffects.DIG_SLOWDOWN);
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    // Apply effect every tick for continuous checks
    return true;
  }

  /** Checks if an entity is protected from frostbite */
  public static boolean isProtected(LivingEntity entity) {
    // Check for frost resistance effect
    if (entity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
      return true;
    }

    // Check for frost walker boots
    if (entity instanceof Player player) {
      // TODO: Implement armor check when FrostWalkerEnchantment API is available
      return false; // FrostWalkerEnchantment.hasEnoughArmor(entity);
    }

    // TODO: Add custom armor check for frost protection

    return false;
  }
}
