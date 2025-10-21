package com.netroaki.chex.entity.boss.verdant_colossus.ai;

import com.netroaki.chex.entity.boss.verdant_colossus.VerdantColossusEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

public class VerdantColossusGoal extends Goal {
  private final VerdantColossusEntity boss;
  private int attackStep;
  private int attackTime;
  private int phase = 0;
  private static final int[] PHASE_HEALTH_THRESHOLDS = {350, 200, 100};

  public VerdantColossusGoal(VerdantColossusEntity entity) {
    this.boss = entity;
    this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    LivingEntity target = this.boss.getTarget();
    return target != null && target.isAlive() && this.boss.getSensing().hasLineOfSight(target);
  }

  @Override
  public void start() {
    this.attackStep = 0;
    this.attackTime = 0;
  }

  @Override
  public void tick() {
    if (--this.attackTime <= 0) {
      LivingEntity target = this.boss.getTarget();
      if (target != null && target.isAlive()) {
        updatePhase();
        performAttack(target);
      } else {
        this.attackStep = 0;
      }
    }
  }

  private void updatePhase() {
    float healthRatio = this.boss.getHealth() / this.boss.getMaxHealth();
    int newPhase = 0;
    for (int i = 0; i < PHASE_HEALTH_THRESHOLDS.length; i++) {
      if (this.boss.getHealth() <= PHASE_HEALTH_THRESHOLDS[i]) {
        newPhase = i + 1;
      }
    }
    if (newPhase > this.phase) {
      this.phase = newPhase;
      this.boss.triggerPhaseChange(phase);
    }
  }

  private void performAttack(LivingEntity target) {
    double distanceSq = this.boss.distanceToSqr(target);

    // Melee attack
    if (distanceSq < 16.0D) {
      this.boss.doHurtTarget(target);
      this.attackTime = 20;
    }
    // Ground slam attack
    else if (this.attackStep == 0) {
      if (this.boss.getRandom().nextFloat() < 0.1F) {
        performGroundSlam();
        this.attackTime = 40 + this.boss.getRandom().nextInt(20);
        this.attackStep = 1;
      }
    }
    // Vine Whip attack
    else if (this.attackStep == 1) {
      if (this.boss.getRandom().nextFloat() < 0.2F) {
        performVineWhip(target);
        this.attackTime = 30 + this.boss.getRandom().nextInt(20);
        this.attackStep = 0;
      }
    }
  }

  private void performGroundSlam() {
    // Create shockwave effect and damage nearby entities
    AABB aabb = this.boss.getBoundingBox().inflate(5.0D, 1.0D, 5.0D);
    this.boss
        .level()
        .getEntitiesOfClass(LivingEntity.class, aabb, e -> e != this.boss)
        .forEach(
            entity -> {
              entity.hurt(this.boss.damageSources().mobAttack(this.boss), 8.0F);
              entity.setDeltaMovement(
                  entity.getX() - this.boss.getX(), 0.5D, entity.getZ() - this.boss.getZ());
            });
    // TODO: Add particle effects and sound
  }

  private void performVineWhip(LivingEntity target) {
    // Create vine projectile or line of damage
    // TODO: Implement vine whip attack logic
  }
}
