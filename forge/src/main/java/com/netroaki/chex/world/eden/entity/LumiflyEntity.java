package com.netroaki.chex.world.eden.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LumiflyEntity extends EdenCreature {
  private static final EntityDataAccessor<Integer> VARIANT =
      SynchedEntityData.defineId(LumiflyEntity.class, EntityDataSerializers.INT);
  private static final int MAX_VARIANTS = 3;

  public LumiflyEntity(EntityType<? extends EdenCreature> type, Level level) {
    super(type, level);
    this.setVariant(this.random.nextInt(MAX_VARIANTS));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return EdenCreature.createAttributes()
        .add(Attributes.MAX_HEALTH, 8.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.FLYING_SPEED, 0.4D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(VARIANT, 0);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt("Variant", this.getVariant());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.setVariant(tag.getInt("Variant"));
  }

  public int getVariant() {
    return this.entityData.get(VARIANT);
  }

  public void setVariant(int variant) {
    this.entityData.set(VARIANT, Math.max(0, Math.min(variant, MAX_VARIANTS - 1)));
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(5, new RandomStrollGoalFlying(this, 1.0D));
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Add particle effects
    if (this.level() instanceof ServerLevel serverLevel) {
      if (this.random.nextFloat() < 0.1F) {
        double d0 = this.getX() + (this.random.nextDouble() - 0.5D);
        double d1 = this.getY() + this.getEyeHeight() + (this.random.nextDouble() - 0.5D);
        double d2 = this.getZ() + (this.random.nextDouble() - 0.5D);

        serverLevel.sendParticles(ParticleTypes.END_ROD, d0, d1, d2, 1, 0, 0, 0, 0);
      }
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.BEE_LOOP;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.BEE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.BEE_DEATH;
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return stack.is(Items.HONEY_BOTTLE);
  }

  @Nullable
  @Override
  public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
    LumiflyEntity baby =
        new LumiflyEntity((EntityType<? extends EdenCreature>) this.getType(), level);
    baby.setVariant(
        this.random.nextBoolean() ? this.getVariant() : ((LumiflyEntity) otherParent).getVariant());
    return baby;
  }

  // Custom flying random stroll goal
  static class RandomStrollGoalFlying extends RandomStrollGoal {
    public RandomStrollGoalFlying(PathfinderMob mob, double speed) {
      super(mob, speed);
    }

    @Override
    public boolean canUse() {
      return !this.mob.isInWater() && super.canUse();
    }

    @Override
    protected Vec3 getPosition() {
      // Prefer flying to higher places
      RandomSource random = this.mob.getRandom();
      return new Vec3(
          this.mob.getX() + (random.nextDouble() - 0.5D) * 10.0D,
          this.mob.getY() + (random.nextDouble() * 3.0D) + 1.0D,
          this.mob.getZ() + (random.nextDouble() - 0.5D) * 10.0D);
    }
  }
}
