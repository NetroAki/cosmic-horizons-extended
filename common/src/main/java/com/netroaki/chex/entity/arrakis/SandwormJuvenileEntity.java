package com.netroaki.chex.entity.arrakis;

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
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SandwormJuvenileEntity extends Monster {
  private static final EntityDataAccessor<Boolean> IS_BURROWED =
      SynchedEntityData.defineId(SandwormJuvenileEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> BURROW_TICKS =
      SynchedEntityData.defineId(SandwormJuvenileEntity.class, EntityDataSerializers.INT);

  private static final int MAX_BURROW_TICKS = 100;
  private int timeUntilNextBurrow = 0;

  public SandwormJuvenileEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 10;
    this.setHealth(this.getMaxHealth());
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 40.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 6.0D)
        .add(Attributes.ARMOR, 2.0D)
        .add(Attributes.FOLLOW_RANGE, 16.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(IS_BURROWED, false);
    this.entityData.define(BURROW_TICKS, 0);
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.level().isClientSide) {
      // Handle burrow state
      if (this.isBurrowed()) {
        int burrowTicks = this.getBurrowTicks() + 1;
        this.setBurrowTicks(burrowTicks);

        if (burrowTicks >= MAX_BURROW_TICKS) {
          this.setBurrowed(false);
          this.setBurrowTicks(0);
          this.timeUntilNextBurrow = 200 + this.random.nextInt(400);
        }
      } else if (--this.timeUntilNextBurrow <= 0) {
        this.tryToBurrow();
      }

      // Update movement speed based on burrow state
      this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.isBurrowed() ? 0.0D : 0.25D);
    }
  }

  private void tryToBurrow() {
    if (!this.isBurrowed() && this.random.nextFloat() < 0.1F) {
      this.setBurrowed(true);
      this.setBurrowTicks(0);
      this.level()
          .playSound(
              null,
              this.getX(),
              this.getY(),
              this.getZ(),
              SoundEvents.SAND_BREAK,
              this.getSoundSource(),
              1.0F,
              0.8F);
    }
  }

  public boolean isBurrowed() {
    return this.entityData.get(IS_BURROWED);
  }

  public void setBurrowed(boolean burrowed) {
    this.entityData.set(IS_BURROWED, burrowed);
    this.noPhysics = burrowed;
    this.setNoGravity(burrowed);
  }

  public int getBurrowTicks() {
    return this.entityData.get(BURROW_TICKS);
  }

  public void setBurrowTicks(int ticks) {
    this.entityData.set(BURROW_TICKS, ticks);
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.HUSK_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.HUSK_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.HUSK_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.HUSK_STEP, 0.15F, 1.0F);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putBoolean("IsBurrowed", this.isBurrowed());
    tag.putInt("BurrowTicks", this.getBurrowTicks());
    tag.putInt("TimeUntilNextBurrow", this.timeUntilNextBurrow);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    if (tag.contains("IsBurrowed")) {
      this.setBurrowed(tag.getBoolean("IsBurrowed"));
    }
    if (tag.contains("BurrowTicks")) {
      this.setBurrowTicks(tag.getInt("BurrowTicks"));
    }
    if (tag.contains("TimeUntilNextBurrow")) {
      this.timeUntilNextBurrow = tag.getInt("TimeUntilNextBurrow");
    }
  }
}
