package com.netroaki.chex.entities.boss;

import javax.annotation.Nonnull;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SkySovereignEntity extends Monster {

  private int stormCooldown = 0;
  private int phase = 1; // 1 = normal, 2 = enraged

  public SkySovereignEntity(EntityType<? extends Monster> entityType, Level level) {
    super(entityType, level);
    this.setNoAi(false);
    this.setNoGravity(true);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.7D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.5D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 25.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 350.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.5D)
        .add(Attributes.FLYING_SPEED, 0.8D)
        .add(Attributes.ATTACK_DAMAGE, 16.0D)
        .add(Attributes.FOLLOW_RANGE, 35.0D)
        .add(Attributes.ARMOR, 12.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
  }

  @Override
  public void tick() {
    super.tick();

    // Phase transition at 50% health
    if (this.getHealth() <= this.getMaxHealth() * 0.5F && phase == 1) {
      phase = 2;
      this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 2, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 1, false, false));
      this.addEffect(new MobEffectInstance(MobEffects.JUMP, 999999, 2, false, false));
    }

    // Storm attack
    if (this.stormCooldown <= 0 && this.getTarget() != null) {
      performStormAttack();
      this.stormCooldown = phase == 2 ? 90 : 150; // Faster in enraged phase
    }

    if (this.stormCooldown > 0) {
      this.stormCooldown--;
    }

    // Floating movement
    if (this.level().isClientSide) {
      this.setDeltaMovement(
          this.getDeltaMovement().add(0.0D, 0.02D * Math.sin(this.tickCount * 0.1D), 0.0D));
    }

    // Particle effects
    if (this.level().isClientSide) {
      if (this.random.nextInt(3) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.CLOUD,
                this.getX() + (this.random.nextDouble() - 0.5D) * 5.0D,
                this.getY() + this.random.nextDouble() * 3.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 5.0D,
                0.0D,
                0.0D,
                0.0D);
      }

      if (phase == 2 && this.random.nextInt(2) == 0) {
        this.level()
            .addParticle(
                ParticleTypes.END_ROD,
                this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D,
                this.getY() + this.random.nextDouble() * 2.0D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D,
                0.0D,
                0.0D,
                0.0D);
      }
    }

    // Apply storm effects to nearby players
    if (!this.level().isClientSide) {
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(15.0D))) {
        if (this.random.nextFloat() < 0.08F) {
          player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 0, false, false));
          player.addEffect(
              new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0, false, false));
          player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
        }
      }
    }
  }

  private void performStormAttack() {
    if (this.level().isClientSide) return;

    // Create storm effect
    for (int i = 0; i < 40; i++) {
      double angle = (i / 40.0) * 2 * Math.PI;
      double x = this.getX() + Math.cos(angle) * 10.0D;
      double z = this.getZ() + Math.sin(angle) * 10.0D;
      double y = this.getY() + this.random.nextDouble() * 3.0D;

      this.level().addParticle(ParticleTypes.CLOUD, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    // Apply storm effects to nearby players
    for (Player player :
        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(12.0D))) {
      player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 1, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 180, 1, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 120, 0, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, false, false));
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.PHANTOM_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
    return SoundEvents.PHANTOM_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.PHANTOM_DEATH;
  }

  @Override
  protected void dropCustomDeathLoot(
      @Nonnull DamageSource damageSource, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(damageSource, looting, recentlyHit);

    // Boss drops
    if (this.random.nextFloat() < 0.9F) {
      this.spawnAtLocation(Items.EMERALD, 7 + this.random.nextInt(5));
    }
    if (this.random.nextFloat() < 0.8F) {
      this.spawnAtLocation(Items.DIAMOND, 4 + this.random.nextInt(3));
    }
    if (this.random.nextFloat() < 0.6F) {
      this.spawnAtLocation(Items.NETHERITE_SCRAP, 3 + this.random.nextInt(2));
    }
    if (this.random.nextFloat() < 0.4F) {
      this.spawnAtLocation(Items.ENCHANTED_GOLDEN_APPLE, 1);
    }
    if (this.random.nextFloat() < 0.2F) {
      this.spawnAtLocation(Items.NETHERITE_INGOT, 1);
    }
    if (this.random.nextFloat() < 0.1F) {
      this.spawnAtLocation(Items.ELYTRA, 1);
    }
  }

  @Override
  public boolean doHurtTarget(@Nonnull Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof Player) {
      // Apply storm effects on successful attack
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.LEVITATION, 80, 0, false, false));
      ((Player) target)
          .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false));
    }
    return flag;
  }

  @Override
  public boolean isNoGravity() {
    return true;
  }

  @Override
  public boolean isPersistenceRequired() {
    return true; // Boss should not despawn
  }
}
