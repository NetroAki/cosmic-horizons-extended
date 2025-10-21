package com.netroaki.chex.entity.boss.stellar_avatar.ai;

import com.netroaki.chex.entity.boss.stellar_avatar.StellarAvatarEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class StellarAvatarMeleeAttackGoal extends MeleeAttackGoal {
  private final StellarAvatarEntity avatar;

  public StellarAvatarMeleeAttackGoal(
      StellarAvatarEntity avatar, double speedModifier, boolean followingTargetEvenIfNotSeen) {
    super(avatar, speedModifier, followingTargetEvenIfNotSeen);
    this.avatar = avatar;
  }

  @Override
  public void stop() {
    super.stop();
    this.avatar.setAggressive(false);
  }
}
