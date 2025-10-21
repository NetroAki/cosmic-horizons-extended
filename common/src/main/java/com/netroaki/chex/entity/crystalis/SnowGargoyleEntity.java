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
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SnowGargoyleEntity extends Monster {
  private int timeUntilNextAttack = 0;

  public SnowGargoyleEntity(EntityType<? extends Monster> type, Level world) {
    super(type, world);
    this.moveControl = new FlyingMoveControl(this, 10, true);
    this.xpReward = 12;
    this.setNoGravity(true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 45.0D)
        .add(Attributes.FLYING_SPEED, 0.5D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 7.0D)
        .add(Attributes.ARMOR, 5.0D)
        .add(Attributes.FOLLOW_RANGE, 40.0D);
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
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
    this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Add floating effect
    if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
      this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }

    // Add snow particles
    if (this.level().isClientSide && this.random.nextInt(5) == 0) {
      this.level()
          .addParticle(
              ParticleTypes.SNOWFLAKE,
              this.getRandomX(0.5D),
              this.getRandomY(),
              this.getRandomZ(0.5D),
              0.0D,
              0.0D,
              0.0D);
    }

    // Handle attack cooldown
    if (this.timeUntilNextAttack > 0) {
      this.timeUntilNextAttack--;
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    if (super.doHurtTarget(target)) {
      if (target instanceof LivingEntity) {
        ((LivingEntity) target)
            .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));

        // Knockback effect
        if (target instanceof Player) {
          Vec3 vec = this.position().subtract(target.position()).normalize();
          target.setDeltaMovement(vec.x * 0.8D, Math.min(0.8D, vec.y * 0.5D + 0.5D), vec.z * 0.8D);
        }
      }
      this.timeUntilNextAttack = 20; // 1 second cooldown
      return true;
    }
    return false;
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.VEX_AMBIENT;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.IRON_GOLEM_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.IRON_GOLEM_DEATH;
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    return source.is(DamageTypeTags.IS_FREEZE) || super.isInvulnerableTo(source);
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
