package com.netroaki.chex.entity.aqua_mundus;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

/**
 * Base class for all Aqua Mundus aquatic entities. Provides common functionality for underwater
 * movement, AI, and behavior.
 */
public abstract class AquaticEntity extends WaterAnimal {
  private static final EntityDataAccessor<Boolean> FROM_BUCKET =
      SynchedEntityData.defineId(AquaticEntity.class, EntityDataSerializers.BOOLEAN);

  public AquaticEntity(EntityType<? extends WaterAnimal> type, Level level) {
    super(type, level);
    this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
    this.lookControl = new SmoothSwimmingLookControl(this, 10);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 10.0D)
        .add(Attributes.MOVEMENT_SPEED, 1.0D)
        .add(Attributes.ATTACK_DAMAGE, 2.0D)
        .add(Attributes.FOLLOW_RANGE, 16.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(FROM_BUCKET, false);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
    this.goalSelector.addGoal(
        2,
        new AvoidEntityGoal<>(
            this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
    this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
  }

  @Override
  protected PathNavigation createNavigation(Level level) {
    return new WaterBoundPathNavigation(this, level);
  }

  @Override
  public void travel(Vec3 travelVector) {
    if (this.isEffectiveAi() && this.isInWater()) {
      this.moveRelative(0.01F, travelVector);
      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));

      if (this.getTarget() == null) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
      }
    } else {
      super.travel(travelVector);
    }
  }

  @Override
  public void aiStep() {
    if (!this.isInWater() && this.onGround() && this.verticalCollision) {
      this.setDeltaMovement(
          this.getDeltaMovement()
              .add(
                  ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F),
                  0.4F,
                  ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
      this.onGround = false;
      this.hasImpulse = true;
      this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
    }

    super.aiStep();
  }

  protected SoundEvent getFlopSound() {
    return SoundEvents.COD_FLOP;
  }

  @Override
  public boolean isPushedByFluid() {
    return !this.isSwimming();
  }

  @Override
  public boolean isPushedByFluidType(net.minecraftforge.fluids.FluidType type) {
    return !this.isSwimming();
  }

  @Override
  public boolean canBreatheUnderwater() {
    return true;
  }

  @Override
  public MobType getMobType() {
    return MobType.WATER;
  }

  @Override
  public boolean isSensitiveToWater() {
    return false;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("FromBucket", this.isFromBucket());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setFromBucket(compound.getBoolean("FromBucket"));
  }

  public boolean isFromBucket() {
    return this.entityData.get(FROM_BUCKET);
  }

  public void setFromBucket(boolean fromBucket) {
    this.entityData.set(FROM_BUCKET, fromBucket);
  }

  @Override
  public boolean requiresCustomPersistence() {
    return super.requiresCustomPersistence() || this.isFromBucket();
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return !this.isFromBucket() && !this.hasCustomName();
  }

  protected abstract ItemStack getBucketItemStack();

  protected SoundEvent getAmbientSound() {
    return SoundEvents.COD_AMBIENT;
  }

  protected SoundEvent getDeathSound() {
    return SoundEvents.COD_DEATH;
  }

  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.COD_HURT;
  }

  protected SoundEvent getSwimSound() {
    return SoundEvents.FISH_SWIM;
  }

  public static boolean checkAquaticSpawnRules(
      EntityType<? extends AquaticEntity> type,
      LevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      Random random) {
    return level.getBlockState(pos).is(Blocks.WATER)
        && level.getBlockState(pos.above()).is(Blocks.WATER);
  }
}
