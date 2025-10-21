package com.netroaki.chex.entity.arrakis;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StormHawkEntity extends Monster implements FlyingAnimal {
  public StormHawkEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.moveControl = new FlyingMoveControl(this, 10, true);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.FLYING_SPEED, 0.6D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 4.0D)
        .add(Attributes.FOLLOW_RANGE, 32.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
    this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));

    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Rabbit.class, true));
  }

  @Override
  protected PathNavigation createNavigation(Level level) {
    FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
    flyingpathnavigation.setCanOpenDoors(false);
    flyingpathnavigation.setCanFloat(true);
    flyingpathnavigation.setCanPassDoors(true);
    return flyingpathnavigation;
  }

  @Override
  public boolean causeFallDamage(float p_149683_, float p_149684_, DamageSource p_149685_) {
    return false;
  }

  @Override
  protected void checkFallDamage(
      double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {}

  @Override
  public boolean isFlying() {
    return !this.onGround();
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.PARROT_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource pDamageSource) {
    return SoundEvents.PARROT_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.PARROT_DEATH;
  }

  @Override
  protected float getSoundVolume() {
    return 0.4F;
  }

  @Override
  public void travel(Vec3 pTravelVector) {
    if (this.isInWater()) {
      this.moveRelative(0.02F, pTravelVector);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
    } else if (this.isInLava()) {
      this.moveRelative(0.02F, pTravelVector);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
    } else {
      float f = 0.91F;
      if (this.onGround()) {
        f =
            this.level()
                    .getBlockState(
                        new BlockPos(
                            (int) this.getX(),
                            (int) (this.getBoundingBox().minY - 1.0D),
                            (int) this.getZ()))
                    .getFriction(
                        this.level(),
                        new BlockPos(
                            (int) this.getX(),
                            (int) (this.getBoundingBox().minY - 1.0D),
                            (int) this.getZ()),
                        this)
                * 0.91F;
      }

      float f1 = 0.16277137F / (f * f * f);
      f = 0.91F;
      if (this.onGround()) {
        f =
            this.level()
                    .getBlockState(
                        new BlockPos(
                            (int) this.getX(),
                            (int) (this.getBoundingBox().minY - 1.0D),
                            (int) this.getZ()))
                    .getFriction(
                        this.level(),
                        new BlockPos(
                            (int) this.getX(),
                            (int) (this.getBoundingBox().minY - 1.0D),
                            (int) this.getZ()),
                        this)
                * 0.91F;
      }

      this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, pTravelVector);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale((double) f));
    }

    this.calculateEntityAnimation(false);
  }
}
