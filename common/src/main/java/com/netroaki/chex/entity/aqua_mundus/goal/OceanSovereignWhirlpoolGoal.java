package com.netroaki.chex.entity.aqua_mundus.goal;

import com.netroaki.chex.entity.aqua_mundus.OceanSovereignEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OceanSovereignWhirlpoolGoal extends Goal {
  private final OceanSovereignEntity sovereign;
  private int attackCooldown = 0;
  private int attackTime = 0;
  private static final int MAX_COOLDOWN = 300; // 15 seconds at 20 ticks/second
  private static final int ATTACK_DURATION = 200; // 10 seconds
  private static final float WHIRLPOOL_RADIUS = 8.0F;
  private static final float PULL_STRENGTH = 0.2F;
  private static final float DAMAGE_INTERVAL = 20.0F; // Damage every second
  private static final float DAMAGE = 4.0F;

  // Whirlpool state
  private BlockPos whirlpoolCenter;
  private int nextDamageTick = 0;
  private boolean isActive = false;

  public OceanSovereignWhirlpoolGoal(OceanSovereignEntity entity) {
    this.sovereign = entity;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE));
  }

  @Override
  public boolean canUse() {
    if (this.attackCooldown > 0) {
      this.attackCooldown--;
      return false;
    }

    // Only use if there are nearby players
    List<LivingEntity> nearbyPlayers =
        this.sovereign
            .level()
            .getEntitiesOfClass(
                LivingEntity.class,
                this.sovereign.getBoundingBox().inflate(WHIRLPOOL_RADIUS * 1.5),
                e -> e instanceof net.minecraft.world.entity.player.Player);

    return !nearbyPlayers.isEmpty();
  }

  @Override
  public void start() {
    this.attackTime = ATTACK_DURATION;
    this.whirlpoolCenter = this.sovereign.blockPosition();
    this.isActive = true;
    this.nextDamageTick = 0;
    this.sovereign.setWhirlpoolState(true);

    // Play start sound
    Level level = this.sovereign.level();
    level.playSound(
        null,
        this.sovereign.getX(),
        this.sovereign.getY(),
        this.sovereign.getZ(),
        SoundEvents.ELDER_GUARDIAN_CURSE,
        SoundSource.HOSTILE,
        5.0F,
        0.5F);
  }

  @Override
  public void tick() {
    if (this.attackTime > 0) {
      this.attackTime--;
      Level level = this.sovereign.level();

      // Update whirlpool position to follow the boss
      if (this.attackTime % 20 == 0) {
        this.whirlpoolCenter = this.sovereign.blockPosition();
      }

      // Create water vortex effect
      this.createVortexEffect(level);

      // Pull and damage entities in range
      if (this.attackTime % 2 == 0) {
        this.pullAndDamageEntities(level);
      }

      // Periodically damage entities
      if (this.attackTime < ATTACK_DURATION - 20) { // Wait 1 second before starting damage
        if (this.nextDamageTick <= 0) {
          this.damageEntities(level);
          this.nextDamageTick = (int) DAMAGE_INTERVAL;
        } else {
          this.nextDamageTick--;
        }
      }

      // End attack
      if (this.attackTime <= 0) {
        this.attackCooldown = MAX_COOLDOWN;
        this.isActive = false;
        this.sovereign.setWhirlpoolState(false);

        // Play end sound
        level.playSound(
            null,
            this.sovereign.getX(),
            this.sovereign.getY(),
            this.sovereign.getZ(),
            SoundEvents.ELDER_GUARDIAN_AMBIENT,
            SoundSource.HOSTILE,
            3.0F,
            0.8F);
      }
    }
  }

  private void createVortexEffect(Level level) {
    // Create water particles in a spiral
    for (int i = 0; i < 3; i++) {
      float progress = 1.0F - ((float) this.attackTime / ATTACK_DURATION);
      float angle = (level.getGameTime() * 10 + i * 120) * 0.017453292F;
      float radius = WHIRLPOOL_RADIUS * progress;

      double x = this.whirlpoolCenter.getX() + 0.5 + Math.cos(angle) * radius;
      double z = this.whirlpoolCenter.getZ() + 0.5 + Math.sin(angle) * radius;
      double y = this.whirlpoolCenter.getY() + 0.1;

      level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0, 0.1, 0);
      level.addParticle(ParticleTypes.BUBBLE, x, y + 0.5, z, 0, 0.1, 0);

      // Create water splash at the edge
      if (level.random.nextFloat() < 0.1F) {
        level.addParticle(
            ParticleTypes.SPLASH,
            x + (level.random.nextFloat() - 0.5) * 0.5,
            y + 0.1,
            z + (level.random.nextFloat() - 0.5) * 0.5,
            0,
            0.5,
            0);
      }
    }
  }

  private void pullAndDamageEntities(Level level) {
    if (this.whirlpoolCenter == null) return;

    AABB area =
        new AABB(
            this.whirlpoolCenter.getX() - WHIRLPOOL_RADIUS,
            this.whirlpoolCenter.getY() - 2,
            this.whirlpoolCenter.getZ() - WHIRLPOOL_RADIUS,
            this.whirlpoolCenter.getX() + WHIRLPOOL_RADIUS,
            this.whirlpoolCenter.getY() + 4,
            this.whirlpoolCenter.getZ() + WHIRLPOOL_RADIUS);

    Vec3 center =
        new Vec3(
            this.whirlpoolCenter.getX() + 0.5,
            this.whirlpoolCenter.getY(),
            this.whirlpoolCenter.getZ() + 0.5);

    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
      if (entity == this.sovereign) continue;

      Vec3 toCenter = center.subtract(entity.position()).normalize();
      double distance = center.distanceTo(entity.position());

      // Pull towards center
      if (distance > 1.5) {
        double strength = PULL_STRENGTH * (1.0 - (distance / (WHIRLPOOL_RADIUS * 2)));
        entity.setDeltaMovement(
            entity
                .getDeltaMovement()
                .add(toCenter.x * strength, toCenter.y * 0.1, toCenter.z * strength));
        entity.hurtMarked = true;
      }

      // Apply slow falling and mining fatigue
      if (entity.tickCount % 20 == 0) {
        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, false, false));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 2, false, false));
      }
    }
  }

  private void damageEntities(Level level) {
    if (this.whirlpoolCenter == null) return;

    AABB damageArea =
        new AABB(
            this.whirlpoolCenter.getX() - WHIRLPOOL_RADIUS * 0.8,
            this.whirlpoolCenter.getY() - 1,
            this.whirlpoolCenter.getZ() - WHIRLPOOL_RADIUS * 0.8,
            this.whirlpoolCenter.getX() + WHIRLPOOL_RADIUS * 0.8,
            this.whirlpoolCenter.getY() + 3,
            this.whirlpoolCenter.getZ() + WHIRLPOOL_RADIUS * 0.8);

    for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, damageArea)) {
      if (entity == this.sovereign) continue;

      float damage =
          DAMAGE
              * (1.0F
                  - (float)
                      (entity.distanceToSqr(
                              this.whirlpoolCenter.getX() + 0.5,
                              this.whirlpoolCenter.getY(),
                              this.whirlpoolCenter.getZ() + 0.5)
                          / (WHIRLPOOL_RADIUS * WHIRLPOOL_RADIUS)));

      if (damage > 0.1F) {
        entity.hurt(DamageSource.mobAttack(this.sovereign), damage);

        // Visual effect
        level.addParticle(
            ParticleTypes.BUBBLE_POP,
            entity.getX(),
            entity.getY() + entity.getBbHeight() * 0.5,
            entity.getZ(),
            0,
            0.1,
            0);
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
    this.isActive = false;
    this.sovereign.setWhirlpoolState(false);
  }

  public boolean isActive() {
    return this.isActive;
  }
}
