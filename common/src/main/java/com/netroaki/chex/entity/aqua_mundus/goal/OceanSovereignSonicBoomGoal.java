package com.netroaki.chex.entity.aqua_mundus.goal;

import com.netroaki.chex.entity.aqua_mundus.OceanSovereignEntity;
import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OceanSovereignSonicBoomGoal extends Goal {
  private final OceanSovereignEntity sovereign;
  private int attackCooldown = 0;
  private int attackTime = 0;
  private static final int MAX_COOLDOWN = 200; // 10 seconds at 20 ticks/second
  private static final int ATTACK_DURATION = 40; // 2 seconds
  private static final float ATTACK_RANGE = 16.0F;
  private static final float ATTACK_ANGLE = 45.0F; // 45 degrees each side
  private static final float DAMAGE = 8.0F;

  public OceanSovereignSonicBoomGoal(OceanSovereignEntity entity) {
    this.sovereign = entity;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    if (this.attackCooldown > 0) {
      this.attackCooldown--;
      return false;
    }

    LivingEntity target = this.sovereign.getTarget();
    return target != null
        && target.isAlive()
        && this.sovereign.distanceToSqr(target) <= (ATTACK_RANGE * ATTACK_RANGE)
        && this.sovereign.hasLineOfSight(target);
  }

  @Override
  public void start() {
    this.attackTime = ATTACK_DURATION;
    this.sovereign.setSonicAttackState(true);

    // Play charge-up sound
    Level level = this.sovereign.level();
    level.playSound(
        null,
        this.sovereign.getX(),
        this.sovereign.getY(),
        this.sovereign.getZ(),
        SoundEvents.WARDEN_SONIC_BOOM,
        SoundSource.HOSTILE,
        5.0F,
        0.7F);
  }

  @Override
  public void tick() {
    if (this.attackTime > 0) {
      this.attackTime--;

      // Look at target during charge-up
      LivingEntity target = this.sovereign.getTarget();
      if (target != null) {
        this.sovereign.getLookControl().setLookAt(target, 30.0F, 30.0F);
      }

      // Visual charge-up effect
      if (this.attackTime % 5 == 0) {
        this.spawnChargeParticles();
      }

      // Execute attack at the end of the charge-up
      if (this.attackTime <= 0) {
        this.executeSonicBoom();
        this.attackCooldown = MAX_COOLDOWN;
        this.sovereign.setSonicAttackState(false);
      }
    }
  }

  private void spawnChargeParticles() {
    Level level = this.sovereign.level();
    Vec3 look = this.sovereign.getLookAngle();
    Vec3 pos = this.sovereign.position().add(0, 2.0, 0);

    for (int i = 0; i < 16; i++) {
      double offsetX = (level.random.nextDouble() - 0.5) * 2.0;
      double offsetY = (level.random.nextDouble() - 0.5) * 2.0;
      double offsetZ = (level.random.nextDouble() - 0.5) * 2.0;

      level.addParticle(
          ParticleTypes.BUBBLE_COLUMN_UP,
          pos.x + offsetX,
          pos.y + offsetY,
          pos.z + offsetZ,
          look.x * 0.2,
          look.y * 0.2,
          look.z * 0.2);
    }
  }

  private void executeSonicBoom() {
    Level level = this.sovereign.level();

    // Play sonic boom sound
    level.playSound(
        null,
        this.sovereign.getX(),
        this.sovereign.getY(),
        this.sovereign.getZ(),
        SoundEvents.WARDEN_SONIC_BOOM,
        SoundSource.HOSTILE,
        10.0F,
        0.8F);

    // Calculate attack cone
    Vec3 lookVec = this.sovereign.getLookAngle().normalize();
    Vec3 origin = this.sovereign.position().add(0, 2.0, 0);

    // Find all entities in the attack cone
    AABB aabb = this.sovereign.getBoundingBox().inflate(ATTACK_RANGE);
    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, aabb)) {
      if (entity == this.sovereign) continue;

      Vec3 toTarget = entity.position().subtract(origin).normalize();
      double angle = Math.toDegrees(Math.acos(lookVec.dot(toTarget)));

      if (angle <= ATTACK_ANGLE) {
        // Apply damage and knockback
        float distance = (float) Math.sqrt(entity.distanceToSqr(origin));
        float damage = DAMAGE * (1.0F - (distance / (ATTACK_RANGE * 2)));

        entity.hurt(DamageSource.mobAttack(this.sovereign), damage);

        // Apply knockback
        Vec3 knockback = toTarget.normalize().scale(1.5);
        entity.push(knockback.x, 0.5, knockback.z);

        // Apply nausea effect
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));

        // Visual effect
        level.addParticle(
            ParticleTypes.SONIC_BOOM, entity.getX(), entity.getY() + 1.0, entity.getZ(), 0, 0, 0);
      }
    }

    // Visual effect for the sonic wave
    for (int i = 0; i < 3; i++) {
      double radius = (i + 1) * 2.0;
      for (int j = 0; j < 16; j++) {
        double angle = (j / 16.0) * Math.PI * 2.0;
        double x = origin.x + Math.cos(angle) * radius;
        double z = origin.z + Math.sin(angle) * radius;

        level.addParticle(
            ParticleTypes.BUBBLE_POP, x, origin.y, z, lookVec.x * 0.5, 0.1, lookVec.z * 0.5);
      }
    }
  }

  @Override
  public boolean canContinueToUse() {
    return this.attackTime > 0;
  }

  @Override
  public boolean isInterruptable() {
    return false;
  }

  @Override
  public void stop() {
    this.sovereign.setSonicAttackState(false);
  }
}
