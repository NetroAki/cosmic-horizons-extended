package com.netroaki.chex.entity.crystalis;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MistWraithEntity extends Monster {
  public MistWraithEntity(EntityType<? extends Monster> type, Level world) {
    super(type, world);
    this.moveControl = new FlyingMoveControl(this, 20, true);
    this.xpReward = 10;
    this.setNoGravity(true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 40.0D)
        .add(Attributes.FLYING_SPEED, 0.4D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 6.0D)
        .add(Attributes.FOLLOW_RANGE, 24.0D)
        .add(Attributes.ARMOR, 2.0D);
  }

  @Override
  protected PathNavigation createNavigation(Level world) {
    FlyingPathNavigation flyingPathNavigator = new FlyingPathNavigation(this, world);
    flyingPathNavigator.setCanOpenDoors(false);
    flyingPathNavigator.setCanFloat(true);
    flyingPathNavigator.setCanPassDoors(true);
    return flyingPathNavigator;
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
    this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Add floating effect
    if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
      this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }

    // Add mist particles
    if (this.level().isClientSide && this.random.nextInt(5) == 0) {
      for (int i = 0; i < 2; ++i) {
        this.level()
            .addParticle(
                ParticleTypes.CLOUD,
                this.getRandomX(0.5D),
                this.getRandomY() - 0.25D,
                this.getRandomZ(0.5D),
                (this.random.nextDouble() - 0.5D) * 0.1D,
                this.random.nextDouble() * 0.1D,
                (this.random.nextDouble() - 0.5D) * 0.1D);
      }
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    if (super.doHurtTarget(target)) {
      if (target instanceof LivingEntity) {
        ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
        ((LivingEntity) target)
            .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
      }
      return true;
    } else {
      return false;
    }
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.GHAST_AMBIENT;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.GHAST_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.GHAST_DEATH;
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    return source.is(DamageTypeTags.IS_FALL) || super.isInvulnerableTo(source);
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  protected void doPush(Entity entity) {
    // Don't push or get pushed by other entities
  }

  @Override
  public boolean isPushedByFluid() {
    return false;
  }
}
