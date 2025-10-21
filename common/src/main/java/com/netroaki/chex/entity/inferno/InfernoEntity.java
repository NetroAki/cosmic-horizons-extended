package com.netroaki.chex.entity.inferno;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/** Base class for all Inferno Prime entities with shared attributes and behaviors. */
public abstract class InfernoEntity extends Monster {
  protected InfernoEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 8;
    this.setNoGravity(false);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 3.0D)
        .add(Attributes.ARMOR, 2.0D)
        .add(Attributes.FOLLOW_RANGE, 16.0D);
  }

  @Override
  public boolean fireImmune() {
    return true;
  }
}
