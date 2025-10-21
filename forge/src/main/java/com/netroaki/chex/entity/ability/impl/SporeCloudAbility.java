package com.netroaki.chex.entity.ability.impl;

import com.netroaki.chex.entity.Sporefly;
import com.netroaki.chex.entity.ability.MobAbility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SporeCloudAbility extends MobAbility {
  private int cooldown;

  public SporeCloudAbility(Sporefly mob) {
    super(mob);
  }

  @Override
  public void tick() {
    if (!(mob instanceof Sporefly sporefly)) {
      return;
    }
    Level level = mob.level();
    if (!(level instanceof ServerLevel serverLevel)) {
      return;
    }

    if (cooldown > 0) {
      cooldown--;
      return;
    }

    if (sporefly.isLeader() && sporefly.getRandom().nextFloat() < 0.02F) {
      serverLevel.sendParticles(
          ParticleTypes.SPORE_BLOSSOM_AIR,
          sporefly.getX(),
          sporefly.getY() + sporefly.getBbHeight() / 2.0,
          sporefly.getZ(),
          40,
          0.4,
          0.3,
          0.4,
          0.02);

      for (LivingEntity nearby :
          serverLevel.getEntitiesOfClass(
              LivingEntity.class,
              sporefly.getBoundingBox().inflate(3.0),
              entity -> entity != sporefly)) {
        nearby.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 0, true, true));
      }

      cooldown = 200;
    }
  }

  @Override
  public void save(CompoundTag tag) {
    tag.putInt("Cooldown", cooldown);
  }

  @Override
  public void load(CompoundTag tag) {
    cooldown = tag.getInt("Cooldown");
  }
}
