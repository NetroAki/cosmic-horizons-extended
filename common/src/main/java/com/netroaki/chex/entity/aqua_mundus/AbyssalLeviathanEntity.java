package com.netroaki.chex.entity.aqua_mundus;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class AbyssalLeviathanEntity extends AquaticEntity {
  private static final EntityDataAccessor<Boolean> IS_CHARGE_ATTACK =
      SynchedEntityData.defineId(AbyssalLeviathanEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> CHARGE_COOLDOWN =
      SynchedEntityData.defineId(AbyssalLeviathanEntity.class, EntityDataSerializers.INT);

  private int attackAnimation;
  private int attackAnimationTick;
  private final int attackAnimationLength = 20;

  public AbyssalLeviathanEntity(EntityType<? extends AbyssalLeviathanEntity> type, Level level) {
    super(type, level);
    this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
    this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 0.0F);
    this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
    this.xpReward = 50;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return AquaticEntity.createAttributes()
        .add(Attributes.MAX_HEALTH, 200.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.8D)
        .add(Attributes.ATTACK_DAMAGE, 12.0D)
        .add(Attributes.ARMOR, 10.0D)
        .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.FOLLOW_RANGE, 64.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(IS_CHARGE_ATTACK, false);
    this.entityData.define(CHARGE_COOLDOWN, 0);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new LeviathanBreachGoal(this));
    this.goalSelector.addGoal(1, new LeviathanChargeAttackGoal(this));
    this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
    this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1.0D, 10));
    this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 16.0F));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Update charge cooldown
    if (this.getChargeCooldown() > 0) {
      this.setChargeCooldown(this.getChargeCooldown() - 1);
    }

    // Update attack animation
    if (this.attackAnimation > 0) {
      this.attackAnimation--;
    }

    if (this.attackAnimationTick > 0) {
      this.attackAnimationTick--;
    }

    // Create water particles when moving fast
    if (this.getDeltaMovement().lengthSqr() > 0.1 && this.isInWater()) {
      for (int i = 0; i < 2; i++) {
        this.level()
            .addParticle(
                net.minecraft.core.particles.ParticleTypes.BUBBLE,
                this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                this.getY() + this.random.nextDouble() * this.getBbHeight(),
                this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                0,
                -0.1,
                0);
      }
    }
  }

  @Override
  public void travel(Vec3 travelVector) {
    if (this.isEffectiveAi() && this.isInWater()) {
      this.moveRelative(0.01F, travelVector);
      this.move(MoverType.SELF, this.getDeltaMovement());

      float speedModifier = this.isCharging() ? 2.5F : 0.9F;
      this.setDeltaMovement(this.getDeltaMovement().scale(speedModifier));

      if (this.getTarget() == null) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
      }
    } else {
      super.travel(travelVector);
    }
  }

  public boolean isCharging() {
    return this.entityData.get(IS_CHARGE_ATTACK);
  }

  public void setCharging(boolean charging) {
    this.entityData.set(IS_CHARGE_ATTACK, charging);
  }

  public int getChargeCooldown() {
    return this.entityData.get(CHARGE_COOLDOWN);
  }

  public void setChargeCooldown(int cooldown) {
    this.entityData.set(CHARGE_COOLDOWN, cooldown);
  }

  public float getAttackAnimationScale(float partialTicks) {
    if (this.attackAnimationTick <= 0) return 0.0F;

    float progress =
        (this.attackAnimationLength - this.attackAnimationTick + partialTicks)
            / this.attackAnimationLength;
    return Mth.sin((progress * progress * 3.5F) * (float) Math.PI);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("IsCharging", this.isCharging());
    compound.putInt("ChargeCooldown", this.getChargeCooldown());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setCharging(compound.getBoolean("IsCharging"));
    this.setChargeCooldown(compound.getInt("ChargeCooldown"));
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ELDER_GUARDIAN_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.ELDER_GUARDIAN_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ELDER_GUARDIAN_DEATH;
  }

  @Override
  protected SoundEvent getFlopSound() {
    return SoundEvents.ELDER_GUARDIAN_FLOP;
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false;
  }

  @Override
  public boolean isPersistenceRequired() {
    return true;
  }

  @Override
  protected ItemStack getBucketItemStack() {
    return new ItemStack(Items.HEART_OF_THE_SEA); // TODO: Replace with custom item
  }

  public static boolean checkLeviathanSpawnRules(
      EntityType<AbyssalLeviathanEntity> type,
      LevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    return level.getBlockState(pos).is(Blocks.WATER)
        && level.getBlockState(pos.above()).is(Blocks.WATER)
        && pos.getY() < level.getSeaLevel() - 40; // Spawn in deep ocean trenches
  }

  private static class LeviathanBreachGoal extends Goal {
    private final AbyssalLeviathanEntity leviathan;
    private int cooldown;

    public LeviathanBreachGoal(AbyssalLeviathanEntity leviathan) {
      this.leviathan = leviathan;
    }

    @Override
    public boolean canUse() {
      if (this.cooldown > 0) {
        this.cooldown--;
        return false;
      }

      if (this.leviathan.getRandom().nextInt(200) != 0) {
        return false;
      }

      if (this.leviathan.getTarget() != null
          && this.leviathan.distanceToSqr(this.leviathan.getTarget()) < 100.0D) {
        return false;
      }

      return this.leviathan.isInWater()
          && this.leviathan.getY() < this.leviathan.level().getSeaLevel() - 10;
    }

    @Override
    public void start() {
      this.leviathan.setDeltaMovement(this.leviathan.getDeltaMovement().add(0.0D, 0.8D, 0.0D));
      this.cooldown = 600 + this.leviathan.getRandom().nextInt(1200);
    }
  }

  private static class LeviathanChargeAttackGoal extends Goal {
    private final AbyssalLeviathanEntity leviathan;
    private int chargeTime;
    private int chargeDuration;
    private Vec3 chargeDirection;

    public LeviathanChargeAttackGoal(AbyssalLeviathanEntity leviathan) {
      this.leviathan = leviathan;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      LivingEntity target = this.leviathan.getTarget();

      if (target == null || !target.isAlive() || !this.leviathan.hasLineOfSight(target)) {
        return false;
      }

      if (this.leviathan.getChargeCooldown() > 0) {
        return false;
      }

      double distance = this.leviathan.distanceToSqr(target);
      return distance >= 16.0D && distance <= 100.0D && this.leviathan.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
      return this.chargeTime < this.chargeDuration && this.leviathan.isInWater();
    }

    @Override
    public void start() {
      LivingEntity target = this.leviathan.getTarget();
      if (target != null) {
        this.chargeDirection =
            new Vec3(
                    target.getX() - this.leviathan.getX(),
                    target.getY() - this.leviathan.getY(),
                    target.getZ() - this.leviathan.getZ())
                .normalize();

        this.chargeDuration = 20 + this.leviathan.getRandom().nextInt(30);
        this.chargeTime = 0;
        this.leviathan.setCharging(true);
        this.leviathan.getNavigation().stop();
      }
    }

    @Override
    public void stop() {
      this.leviathan.setCharging(false);
      this.leviathan.setChargeCooldown(200 + this.leviathan.getRandom().nextInt(200));
    }

    @Override
    public void tick() {
      this.chargeTime++;

      if (this.chargeDirection != null) {
        // Apply movement
        Vec3 motion = this.chargeDirection.scale(1.5D);
        this.leviathan.setDeltaMovement(motion);

        // Look in the direction of movement
        float yRot = (float) (Mth.atan2(motion.z, motion.x) * (180F / (float) Math.PI)) - 90.0F;
        float xRot =
            (float)
                (Mth.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z))
                    * (180F / (float) Math.PI));

        this.leviathan.setYRot(lerpRotation(this.leviathan.yRotO, yRot));
        this.leviathan.setXRot(lerpRotation(this.leviathan.xRotO, xRot));

        // Damage entities in the way
        for (Entity entity :
            this.leviathan
                .level()
                .getEntities(
                    this.leviathan,
                    this.leviathan.getBoundingBox().inflate(2.0D),
                    (e) -> e instanceof LivingEntity && e.isAlive() && e != this.leviathan)) {
          entity.hurt(this.leviathan.damageSources().mobAttack(this.leviathan), 10.0F);

          if (entity instanceof LivingEntity living) {
            living.knockback(1.5D, -motion.x, -motion.z);
            living.setDeltaMovement(living.getDeltaMovement().add(0.0D, 0.2D, 0.0D));
          }
        }

        // Create water particles
        if (this.leviathan.tickCount % 2 == 0) {
          for (int i = 0; i < 5; i++) {
            this.leviathan
                .level()
                .addParticle(
                    net.minecraft.core.particles.ParticleTypes.BUBBLE_COLUMN_UP,
                    this.leviathan.getX()
                        + (this.leviathan.getRandom().nextDouble() - 0.5)
                            * this.leviathan.getBbWidth(),
                    this.leviathan.getY()
                        + this.leviathan.getRandom().nextDouble() * this.leviathan.getBbHeight(),
                    this.leviathan.getZ()
                        + (this.leviathan.getRandom().nextDouble() - 0.5)
                            * this.leviathan.getBbWidth(),
                    0,
                    0.1,
                    0);
          }
        }
      }
    }

    private static float lerpRotation(float current, float target) {
      float f = Mth.wrapDegrees(target - current);
      return current + f * 0.4F;
    }
  }
}
