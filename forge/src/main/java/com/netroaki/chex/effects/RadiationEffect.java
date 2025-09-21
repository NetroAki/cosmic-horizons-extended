package com.netroaki.chex.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RadiationEffect extends MobEffect {
  public RadiationEffect() {
    super(MobEffectCategory.HARMFUL, 0x5aff5a);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    // Poison-like periodic damage that bypasses armor, scales mildly with amplifier
    int interval = com.netroaki.chex.config.CHEXConfig.radiationTickInterval();
    if (interval > 0 && entity.tickCount % interval == 0) {
      float base = (float) com.netroaki.chex.config.CHEXConfig.radiationDamageBase();
      float per = (float) com.netroaki.chex.config.CHEXConfig.radiationDamagePerAmplifier();
      float dmg = Math.max(0.0F, base + (amplifier * per));
      if (dmg > 0) entity.hurt(entity.damageSources().magic(), dmg);
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    // Let applyEffectTick run regularly
    return true;
  }
}
