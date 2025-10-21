package com.netroaki.chex.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class SnowBlindnessEffect extends MobEffect {
  public SnowBlindnessEffect() {
    super(MobEffectCategory.HARMFUL, 0xE0F7FF); // pale blue/white
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return duration % 40 == 0; // periodically apply visuals
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (!entity.level().isClientSide) {
      // Apply mild vanilla blindness to enforce reduced visibility, stacking with our overlay
      int amp = Math.min(1 + amplifier, 2);
      entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, amp, false, false, false));
    }
  }
}
