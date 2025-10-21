package com.netroaki.chex.entity.crystalis;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PressureLeviathanEntity extends WaterAnimal {
  private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING =
      SynchedEntityData.defineId(PressureLeviathanEntity.class, EntityDataSerializers.BOOLEAN);
  private final BossEvent bossEvent =
      (BossEvent)
          (new BossEvent(
              this.getDisplayName(),
              BossEvent.BossBarColor.BLUE,
              BossEvent.BossBarOverlay.PROGRESS));
  private int attackCooldown = 0;
  private int chargeCooldown = 0;
  private Vec3 chargeDirection = Vec3.ZERO;

  public PressureLeviathanEntity(EntityType<? extends WaterAnimal> type, Level world) {
    super(type, world);
    this.moveControl = new PressureLeviathanEntity.MoveHelperController(this);
    this.xpReward = 100;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 300.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 12.0D)
        .add(Attributes.ARMOR, 10.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.FOLLOW_RANGE, 50.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_IS_CHARGING, false);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public void tick() {
    super.tick();

    // Update boss bar
    this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

    // Handle attack cooldown
    if (this.attackCooldown > 0) {
      this.attackCooldown--;
    }

    // Handle charge cooldown and charging
    if (this.chargeCooldown > 0) {
      this.chargeCooldown--;
      if (this.isCharging()) {
        this.setDeltaMovement(this.chargeDirection.scale(1.5D));

        // Damage entities in the way
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(1.0D))) {
          if (entity instanceof LivingEntity && entity != this) {
            entity.hurt(this.damageSources().mobAttack(this), 8.0F);
          }
        }

        // Create water particles
        if (this.level().isClientSide) {
          for (int i = 0; i < 5; i++) {
            this.level()
                .addParticle(
                    ParticleTypes.BUBBLE,
                    this.getRandomX(1.0D),
                    this.getRandomY(),
                    this.getRandomZ(1.0D),
                    0.0D,
                    0.1D,
                    0.0D);
          }
        }
      }
    } else if (this.isCharging()) {
      this.setCharging(false);
      this.chargeDirection = Vec3.ZERO;
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    if (this.attackCooldown <= 0) {
      this.attackCooldown = 20; // 1 second cooldown

      if (this.random.nextInt(4) == 0) {
        // 25% chance to start a charge attack
        this.startChargeAttack(target.position());
        return true;
      }

      // Normal attack
      boolean flag =
          target.hurt(
              this.damageSources().mobAttack(this),
              (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
      if (flag) {
        if (target instanceof LivingEntity) {
          ((LivingEntity) target)
              .addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
        }
        this.doEnchantDamageEffects(this, target);
      }
      return flag;
    }
    return false;
  }

  private void startChargeAttack(Vec3 targetPos) {
    this.chargeDirection = this.position().vectorTo(targetPos).normalize();
    this.chargeCooldown = 40; // 2 seconds of charging
    this.setCharging(true);
    this.playSound(SoundEvents.ELDER_GUARDIAN_CURSE, 1.0F, 0.5F);
  }

  public boolean isCharging() {
    return this.entityData.get(DATA_IS_CHARGING);
  }

  public void setCharging(boolean charging) {
    this.entityData.set(DATA_IS_CHARGING, charging);
  }

  @Override
  public void startSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
    super.startSeenByPlayer(player);
    this.bossEvent.addPlayer(player);
  }

  @Override
  public void stopSeenByPlayer(net.minecraft.server.level.ServerPlayer player) {
    super.stopSeenByPlayer(player);
    this.bossEvent.removePlayer(player);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("Charging", this.isCharging());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setCharging(compound.getBoolean("Charging"));
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ELDER_GUARDIAN_AMBIENT;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.ELDER_GUARDIAN_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ELDER_GUARDIAN_DEATH;
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    return source.is(DamageTypeTags.IS_DROWNING) || super.isInvulnerableTo(source);
  }

  @Override
  public boolean isPushedByFluid() {
    return false;
  }

  @Override
  public boolean canBreatheUnderwater() {
    return true;
  }

  @Override
  protected float getWaterSlowDown() {
    return 0.98F;
  }

  static class MoveHelperController extends MoveControl {
    private final PressureLeviathanEntity leviathan;

    public MoveHelperController(PressureLeviathanEntity leviathan) {
      super(leviathan);
      this.leviathan = leviathan;
    }

    @Override
    public void tick() {
      if (this.operation == Operation.MOVE_TO && !this.leviathan.getNavigation().isDone()) {
        double d0 = this.wantedX - this.leviathan.getX();
        double d1 = this.wantedY - this.leviathan.getY();
        double d2 = this.wantedZ - this.leviathan.getZ();
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        d1 /= d3;
        float f = (float) (Math.atan2(d2, d0) * (180F / (float) Math.PI)) - 90.0F;
        this.leviathan.setYRot(this.rotlerp(this.leviathan.getYRot(), f, 90.0F));
        this.leviathan.yBodyRot = this.leviathan.getYRot();
        float f1 =
            (float)
                (this.speedModifier * this.leviathan.getAttributeValue(Attributes.MOVEMENT_SPEED));
        this.leviathan.setSpeed(f1 * 0.1F);
        this.leviathan.setDeltaMovement(
            this.leviathan
                .getDeltaMovement()
                .add(0.0D, (double) this.leviathan.getSpeed() * d1 * 0.1D, 0.0D));
      } else {
        this.leviathan.setSpeed(0.0F);
      }
    }
  }
}
