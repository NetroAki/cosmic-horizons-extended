package com.netroaki.chex.entity.alpha_centauri;

import java.util.EnumSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.phys.Vec3;

public class FlareSpriteEntity extends Monster {
  private static final EntityDataAccessor<Boolean> DATA_IS_DASHING =
      SynchedEntityData.defineId(FlareSpriteEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> DATA_DASH_COOLDOWN =
      SynchedEntityData.defineId(FlareSpriteEntity.class, EntityDataSerializers.INT);

  private int dashCooldown = 0;
  private Vec3 dashDirection = Vec3.ZERO;
  private int dashTime = 0;

  public FlareSpriteEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.moveControl = new FlareSpriteMoveControl(this);
    this.xpReward = 8;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.FLYING_SPEED, 0.6D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 3.0D)
        .add(Attributes.FOLLOW_RANGE, 24.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new FlareSpriteDashGoal(this));
    this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
    this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_IS_DASHING, false);
    this.entityData.define(DATA_DASH_COOLDOWN, 0);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.dashCooldown > 0) {
      this.dashCooldown--;
    }

    if (this.isDashing()) {
      this.dashTime++;

      if (this.dashTime > 10) {
        this.setDashing(false);
        this.dashTime = 0;
        this.dashCooldown = 40 + this.random.nextInt(40);
      } else if (this.dashTime > 2) {
        // Apply dash movement
        this.setDeltaMovement(this.dashDirection.scale(1.5D));
      }

      // Visual effects
      if (this.level().isClientSide) {
        for (int i = 0; i < 3; ++i) {
          double d0 = this.random.nextGaussian() * 0.02D;
          double d1 = this.random.nextGaussian() * 0.02D;
          double d2 = this.random.nextGaussian() * 0.02D;
          this.level()
              .addParticle(
                  net.minecraft.core.particles.ParticleTypes.FLAME,
                  this.getRandomX(0.5D),
                  this.getRandomY(),
                  this.getRandomZ(0.5D),
                  d0,
                  d1,
                  d2);
        }
      }
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    boolean flag =
        target.hurt(
            this.damageSources().mobAttack(this),
            (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
    if (flag) {
      target.setSecondsOnFire(3);
      this.doEnchantDamageEffects(this, target);
    }
    return flag;
  }

  @Override
  public boolean isOnFire() {
    return true; // Always appears to be on fire
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
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setDashing(compound.getBoolean("IsDashing"));
    this.dashCooldown = compound.getInt("DashCooldown");
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("IsDashing", this.isDashing());
    compound.putInt("DashCooldown", this.dashCooldown);
  }

  @Override
  protected PathNavigation createNavigation(Level level) {
    FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
    flyingpathnavigation.setCanOpenDoors(false);
    flyingpathnavigation.setCanFloat(true);
    flyingpathnavigation.setCanPassDoors(true);
    return flyingpathnavigation;
  }

  // Getters and setters
  public boolean isDashing() {
    return this.entityData.get(DATA_IS_DASHING);
  }

  public void setDashing(boolean dashing) {
    this.entityData.set(DATA_IS_DASHING, dashing);
  }

  public int getDashCooldown() {
    return this.entityData.get(DATA_DASH_COOLDOWN);
  }

  public void setDashCooldown(int cooldown) {
    this.entityData.set(DATA_DASH_COOLDOWN, cooldown);
  }

  // Custom AI for dash attack
  static class FlareSpriteDashGoal extends Goal {
    private final FlareSpriteEntity sprite;
    private int dashAttempts;

    public FlareSpriteDashGoal(FlareSpriteEntity sprite) {
      this.sprite = sprite;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      LivingEntity target = this.sprite.getTarget();
      return target != null && target.isAlive() && this.sprite.dashCooldown <= 0;
    }

    @Override
    public void start() {
      this.dashAttempts = 0;
      this.sprite.setDashing(false);
    }

    @Override
    public void stop() {
      this.sprite.setDashing(false);
      this.sprite.dashCooldown = 40 + this.sprite.random.nextInt(40);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
      return true;
    }

    @Override
    public void tick() {
      LivingEntity target = this.sprite.getTarget();
      if (target == null) return;

      double distanceSq = this.sprite.distanceToSqr(target);

      if (distanceSq < 4.0D) {
        // Too close, move away
        this.sprite
            .getNavigation()
            .moveTo(
                this.sprite.getX() + (this.sprite.getX() - target.getX()),
                this.sprite.getY() + (this.sprite.random.nextFloat() * 5.0F),
                this.sprite.getZ() + (this.sprite.getZ() - target.getZ()),
                1.2D);
      } else if (distanceSq < 100.0D && this.sprite.hasLineOfSight(target)) {
        // Prepare to dash
        this.sprite.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (this.dashAttempts < 3 && this.sprite.random.nextFloat() < 0.1F) {
          // Dash towards target
          this.sprite.dashDirection =
              new Vec3(
                      target.getX() - this.sprite.getX(),
                      target.getY(0.5D) - this.sprite.getY(),
                      target.getZ() - this.sprite.getZ())
                  .normalize();

          this.sprite.setDashing(true);
          this.dashAttempts++;
        }
      } else {
        // Move towards target normally
        this.sprite.getNavigation().moveTo(target, 1.0D);
      }
    }
  }

  // Custom movement controller for smooth flying
  static class FlareSpriteMoveControl extends FlyingMoveControl {
    private final FlareSpriteEntity sprite;
    private int floatDuration;

    public FlareSpriteMoveControl(FlareSpriteEntity sprite) {
      super(sprite, 20, true);
      this.sprite = sprite;
    }

    @Override
    public void tick() {
      if (this.sprite.isDashing()) {
        // Let the dash movement take over
        return;
      }

      if (this.operation == MoveControl.Operation.MOVE_TO) {
        if (this.floatDuration-- <= 0) {
          this.floatDuration += this.sprite.getRandom().nextInt(5) + 2;
          Vec3 vec3 =
              new Vec3(
                  this.wantedX - this.sprite.getX(),
                  this.wantedY - this.sprite.getY(),
                  this.wantedZ - this.sprite.getZ());
          double d0 = vec3.length();
          vec3 = vec3.normalize();
          if (this.canReach(vec3, Mth.ceil(d0))) {
            this.sprite.setDeltaMovement(this.sprite.getDeltaMovement().add(vec3.scale(0.1D)));
          } else {
            this.operation = MoveControl.Operation.WAIT;
          }
        }
      }
    }

    private boolean canReach(Vec3 direction, int steps) {
      // Simple check to prevent getting stuck
      return true;
    }
  }
}
