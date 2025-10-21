package com.netroaki.chex.entity.boss.stellar_avatar.ai;

import com.netroaki.chex.entity.boss.stellar_avatar.StellarAvatarEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class StellarAvatarPhaseGoal extends Goal {
  private final StellarAvatarEntity avatar;

  public StellarAvatarPhaseGoal(StellarAvatarEntity avatar) {
    this.avatar = avatar;
    this.setFlags(EnumSet.of(Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return true;
  }

  @Override
  public void tick() {
    LivingEntity target = this.avatar.getTarget();
    if (target != null) {
      this.avatar.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }
  }
}
