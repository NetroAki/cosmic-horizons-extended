package com.netroaki.chex.entity.boss.stellar_avatar.ai;

import com.netroaki.chex.entity.boss.stellar_avatar.StellarAvatarEntity;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;

public class StellarAvatarRandomLookGoal extends Goal {
  private final StellarAvatarEntity avatar;
  private double relX;
  private double relZ;
  private int lookTime;

  public StellarAvatarRandomLookGoal(StellarAvatarEntity avatar) {
    this.avatar = avatar;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    // Only look around if not in combat or not attacking
    return this.avatar.getRandom().nextFloat() < 0.02F
        && (this.avatar.getTarget() == null
            || this.avatar.getTarget().distanceToSqr(this.avatar) > 16.0D);
  }

  @Override
  public boolean canContinueToUse() {
    return this.lookTime >= 0;
  }

  @Override
  public void start() {
    // Choose a random direction to look
    double d0 = (Math.PI * 2D) * this.avatar.getRandom().nextDouble();
    this.relX = Mth.cos((float) d0);
    this.relZ = Mth.sin((float) d0);
    this.lookTime = 20 + this.avatar.getRandom().nextInt(40); // Look for 1-3 seconds
  }

  @Override
  public void tick() {
    --this.lookTime;

    // Look at the chosen direction
    this.avatar
        .getLookControl()
        .setLookAt(
            this.avatar.getX() + this.relX,
            this.avatar.getEyeY(),
            this.avatar.getZ() + this.relZ,
            this.avatar.getMaxHeadYRot(),
            this.avatar.getMaxHeadXRot());

    // If in phase 3, occasionally create particle effects while looking around
    if (this.avatar.getPhase() == 2 && this.avatar.getRandom().nextFloat() < 0.1F) {
      this.createParticles();
    }
  }

  private void createParticles() {
    // Create a small burst of flame particles around the avatar's head
    for (int i = 0; i < 3; ++i) {
      double d0 = this.avatar.getRandom().nextGaussian() * 0.02D;
      double d1 = this.avatar.getRandom().nextGaussian() * 0.02D;
      double d2 = this.avatar.getRandom().nextGaussian() * 0.02D;

      this.avatar
          .level()
          .addParticle(
              net.minecraft.core.particles.ParticleTypes.FLAME,
              this.avatar.getX()
                  + (this.avatar.getRandom().nextFloat() * this.avatar.getBbWidth() * 2.0F)
                  - this.avatar.getBbWidth(),
              this.avatar.getY()
                  + this.avatar.getEyeHeight()
                  + (this.avatar.getRandom().nextFloat() * 0.5F),
              this.avatar.getZ()
                  + (this.avatar.getRandom().nextFloat() * this.avatar.getBbWidth() * 2.0F)
                  - this.avatar.getBbWidth(),
              d0,
              d1,
              d2);
    }
  }

  @Override
  public void stop() {
    // Reset look when done
    this.avatar
        .getLookControl()
        .setLookAt(
            this.avatar.getX(),
            this.avatar.getEyeY(),
            this.avatar.getZ(),
            this.avatar.getYRot(),
            this.avatar.getXRot());
  }
}
