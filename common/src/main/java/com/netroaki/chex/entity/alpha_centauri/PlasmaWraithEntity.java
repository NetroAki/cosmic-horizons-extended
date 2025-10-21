package com.netroaki.chex.entity.alpha_centauri;

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
import net.minecraft.world.phys.Vec3;

public class PlasmaWraithEntity extends Monster {
  private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING =
      SynchedEntityData.defineId(PlasmaWraithEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> DATA_CHARGE_TIME =
      SynchedEntityData.defineId(PlasmaWraithEntity.class, EntityDataSerializers.INT);

  private int attackAnimationTick;
  private int chargeCooldown = 0;

  public PlasmaWraithEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 15;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 60.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 6.0D)
        .add(Attributes.ARMOR, 4.0D)
        .add(Attributes.FOLLOW_RANGE, 32.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new PlasmaWraithChargeAttackGoal(this));
    this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
    this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_IS_CHARGING, false);
    this.entityData.define(DATA_CHARGE_TIME, 0);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.attackAnimationTick > 0) {
      --this.attackAnimationTick;
    }

    if (this.chargeCooldown > 0) {
      --this.chargeCooldown;
    }

    if (this.level().isClientSide) {
      // Client-side effects
      if (this.isCharging()) {
        // Add charging particles
        for (int i = 0; i < 2; ++i) {
          double d0 = this.random.nextGaussian() * 0.02D;
          double d1 = this.random.nextGaussian() * 0.02D;
          double d2 = this.random.nextGaussian() * 0.02D;
          this.level()
              .addParticle(
                  net.minecraft.core.particles.ParticleTypes.FLAME,
                  this.getRandomX(1.0D),
                  this.getRandomY() + 0.5D,
                  this.getRandomZ(1.0D),
                  d0,
                  d1,
                  d2);
        }
      }
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    this.attackAnimationTick = 10;
    this.level().broadcastEntityEvent(this, (byte) 4);

    boolean flag =
        target.hurt(
            this.damageSources().mobAttack(this),
            (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
    if (flag) {
      target.setSecondsOnFire(5);
      this.doEnchantDamageEffects(this, target);
    }

    return flag;
  }

  @Override
  public void handleEntityEvent(byte id) {
    if (id == 4) {
      this.attackAnimationTick = 10;
    } else {
      super.handleEntityEvent(id);
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.BLAZE_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.BLAZE_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.BLAZE_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState blockIn) {
    // Silent movement
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setCharging(compound.getBoolean("IsCharging"));
    this.setChargeTime(compound.getInt("ChargeTime"));
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("IsCharging", this.isCharging());
    compound.putInt("ChargeTime", this.getChargeTime());
  }

  // Getters and setters for charge state
  public boolean isCharging() {
    return this.entityData.get(DATA_IS_CHARGING);
  }

  public void setCharging(boolean charging) {
    this.entityData.set(DATA_IS_CHARGING, charging);
  }

  public int getChargeTime() {
    return this.entityData.get(DATA_CHARGE_TIME);
  }

  public void setChargeTime(int time) {
    this.entityData.set(DATA_CHARGE_TIME, time);
  }

  public int getAttackAnimationTick() {
    return this.attackAnimationTick;
  }

  // Custom AI for charge attack
  static class PlasmaWraithChargeAttackGoal extends Goal {
    private final PlasmaWraithEntity wraith;
    private int chargeTime;

    public PlasmaWraithChargeAttackGoal(PlasmaWraithEntity wraith) {
      this.wraith = wraith;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      LivingEntity target = this.wraith.getTarget();
      return target != null && target.isAlive() && this.wraith.chargeCooldown <= 0;
    }

    @Override
    public void start() {
      this.chargeTime = 0;
      this.wraith.setCharging(true);
    }

    @Override
    public void stop() {
      this.wraith.setCharging(false);
      this.wraith.chargeCooldown = 100 + this.wraith.random.nextInt(100);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
      return true;
    }

    @Override
    public void tick() {
      LivingEntity target = this.wraith.getTarget();
      if (target == null) return;

      double distanceSq = this.wraith.distanceToSqr(target);

      if (distanceSq > 4.0D && distanceSq < 100.0D) {
        this.chargeTime++;

        // Look at target
        this.wraith.getLookControl().setLookAt(target, 10.0F, 10.0F);

        // Charge towards target
        if (this.chargeTime >= 20) {
          Vec3 vec3 = this.wraith.getDeltaMovement();
          Vec3 vec31 =
              new Vec3(
                  target.getX() - this.wraith.getX(), 0.0D, target.getZ() - this.wraith.getZ());
          if (vec31.lengthSqr() > 1.0E-7D) {
            vec31 = vec31.normalize().scale(0.4D).add(vec3.scale(0.2D));
          }

          this.wraith.setDeltaMovement(vec31.x, 0.2D, vec31.z);
        }

        // Reset charge after a while
        if (this.chargeTime >= 40) {
          this.wraith.chargeCooldown = 100 + this.wraith.random.nextInt(100);
          this.wraith.setCharging(false);
        }
      } else {
        this.wraith.getNavigation().moveTo(target, 1.0D);
      }
    }
  }
}
