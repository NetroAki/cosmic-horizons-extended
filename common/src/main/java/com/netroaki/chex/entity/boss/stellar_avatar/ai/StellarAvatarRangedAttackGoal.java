package com.netroaki.chex.entity.boss.stellar_avatar.ai;

import com.netroaki.chex.entity.boss.stellar_avatar.StellarAvatarEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class StellarAvatarRangedAttackGoal extends Goal {
  private final StellarAvatarEntity avatar;
  private final int attackCooldown;
  private int attackTime;

  public StellarAvatarRangedAttackGoal(
      StellarAvatarEntity avatar,
      double speedModifier,
      int attackCooldown,
      float maxAttackDistance) {
    this.avatar = avatar;
    this.attackCooldown = Math.max(attackCooldown, 20);
    this.attackTime = this.attackCooldown;
    this.setFlags(EnumSet.of(Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return this.avatar.getTarget() != null;
  }

  @Override
  public boolean canContinueToUse() {
    return this.canUse();
  }

  @Override
  public void tick() {
    LivingEntity target = this.avatar.getTarget();
    if (target == null) {
      return;
    }

    this.avatar.getLookControl().setLookAt(target, 30.0F, 30.0F);
    if (--this.attackTime <= 0) {
      // Placeholder: future projectile logic will live here.
      this.attackTime = this.attackCooldown;
    }
  }
}
