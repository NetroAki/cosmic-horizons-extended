package com.netroaki.chex.entity.crystalis;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
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

public class AbyssDrifterEntity extends Monster {
  private static final EntityDataAccessor<Boolean> DATA_IS_PHASING =
      SynchedEntityData.defineId(AbyssDrifterEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> DATA_PHASE_TIMER =
      SynchedEntityData.defineId(AbyssDrifterEntity.class, EntityDataSerializers.INT);

  public AbyssDrifterEntity(EntityType<? extends Monster> type, Level world) {
    super(type, world);
    this.moveControl = new FlyingMoveControl(this, 20, true);
    this.xpReward = 15;
    this.setNoGravity(true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 60.0D)
        .add(Attributes.FLYING_SPEED, 0.6D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 5.0D)
        .add(Attributes.ARMOR, 4.0D)
        .add(Attributes.FOLLOW_RANGE, 35.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_IS_PHASING, false);
    this.entityData.define(DATA_PHASE_TIMER, 0);
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
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Handle phasing effect
    if (this.isPhasing()) {
      int phaseTimer = this.getPhaseTimer();
      if (phaseTimer > 0) {
        this.setPhaseTimer(phaseTimer - 1);

        // Make the entity semi-transparent and invulnerable
        this.setInvulnerable(true);

        // Add particle effects
        if (this.level().isClientSide) {
          for (int i = 0; i < 3; i++) {
            double d0 = this.getRandomX(1.0D);
            double d1 = this.getRandomY();
            double d2 = this.getRandomZ(1.0D);
            this.level().addParticle(ParticleTypes.ENCHANT, d0, d1, d2, 0.0D, 0.0D, 0.0D);
          }
        }
      } else {
        this.setPhasing(false);
        this.setInvulnerable(false);
      }
    }

    // Randomly start phasing
    if (!this.level().isClientSide && this.random.nextInt(200) == 0 && !this.isPhasing()) {
      this.setPhasing(true);
      this.setPhaseTimer(40 + this.random.nextInt(40)); // 2-4 seconds of phasing
    }

    // Add floating movement
    if (!this.onGround() && this.getDeltaMovement().y < 0.0D) {
      this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    if (this.isPhasing()) {
      // Teleport behind the target
      double d0 = target.getX() - this.getX();
      double d1 = target.getZ() - this.getZ();
      float f = (float) (Mth.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;

      double x = target.getX() + Mth.sin(f * ((float) Math.PI / 180F)) * 2.0F;
      double z = target.getZ() - Mth.cos(f * ((float) Math.PI / 180F)) * 2.0F;

      this.teleportTo(x, target.getY(), z);
      this.getLookControl().setLookAt(target, 30.0F, 30.0F);

      // End phasing after attack
      this.setPhasing(false);
      this.setPhaseTimer(0);
      this.setInvulnerable(false);
    }

    // Normal attack with chance to apply levitation
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof LivingEntity) {
      if (this.random.nextInt(3) == 0) {
        ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0));
      }
    }
    return flag;
  }

  public boolean isPhasing() {
    return this.entityData.get(DATA_IS_PHASING);
  }

  public void setPhasing(boolean phasing) {
    this.entityData.set(DATA_IS_PHASING, phasing);
  }

  public int getPhaseTimer() {
    return this.entityData.get(DATA_PHASE_TIMER);
  }

  public void setPhaseTimer(int time) {
    this.entityData.set(DATA_PHASE_TIMER, time);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("Phasing", this.isPhasing());
    compound.putInt("PhaseTimer", this.getPhaseTimer());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setPhasing(compound.getBoolean("Phasing"));
    this.setPhaseTimer(compound.getInt("PhaseTimer"));
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENDERMAN_AMBIENT;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.ENDERMAN_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENDERMAN_DEATH;
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    return (this.isPhasing() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
        || super.isInvulnerableTo(source);
  }

  @Override
  public boolean isPushedByFluid() {
    return false;
  }

  @Override
  public boolean isPushable() {
    return !this.isPhasing();
  }
}
