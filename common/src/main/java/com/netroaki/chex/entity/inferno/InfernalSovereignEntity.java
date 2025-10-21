package com.netroaki.chex.entity.inferno;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class InfernalSovereignEntity extends Monster implements PowerableMob {
  // Phase tracking
  private static final EntityDataAccessor<Integer> PHASE =
      SynchedEntityData.defineId(InfernalSovereignEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> IS_CHARGING =
      SynchedEntityData.defineId(InfernalSovereignEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> ABILITY_COOLDOWN =
      SynchedEntityData.defineId(InfernalSovereignEntity.class, EntityDataSerializers.INT);

  // Boss bar
  private final ServerBossEvent bossEvent =
      new ServerBossEvent(
          this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

  // Timers and cooldowns
  private int attackTimer;
  private int fireRainCooldown;
  private int magmaArmorCooldown;

  // Constants
  private static final float PHASE_2_HP_THRESHOLD = 0.6f; // 60% HP
  private static final float PHASE_3_HP_THRESHOLD = 0.3f; // 30% HP
  private static final int FIRE_RAIN_COOLDOWN = 200; // 10 seconds
  private static final int MAGMA_ARMOR_COOLDOWN = 300; // 15 seconds

  public InfernalSovereignEntity(EntityType<? extends InfernalSovereignEntity> type, Level level) {
    super(type, level);
    this.xpReward = 1000;
    this.moveControl = new InfernalSovereignMoveControl(this);
    this.setHealth(this.getMaxHealth());
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 500.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 12.0D)
        .add(Attributes.ARMOR, 10.0D)
        .add(Attributes.FOLLOW_RANGE, 50.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.ATTACK_KNOCKBACK, 2.0D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new InfernalSovereignChargeAttackGoal(this));
    this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(PHASE, 1);
    this.entityData.define(IS_CHARGING, false);
    this.entityData.define(ABILITY_COOLDOWN, 0);
  }

  @Override
  public void tick() {
    super.tick();

    // Update boss bar
    if (!this.level().isClientSide) {
      this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

      // Update phase based on health
      int phase = getPhase();
      if (this.getHealth() < this.getMaxHealth() * PHASE_3_HP_THRESHOLD && phase < 3) {
        setPhase(3);
        triggerPhaseTransition(3);
      } else if (this.getHealth() < this.getMaxHealth() * PHASE_2_HP_THRESHOLD && phase < 2) {
        setPhase(2);
        triggerPhaseTransition(2);
      }

      // Update cooldowns
      if (this.fireRainCooldown > 0) fireRainCooldown--;
      if (this.magmaArmorCooldown > 0) magmaArmorCooldown--;

      // Random ability usage
      if (this.getTarget() != null && this.random.nextInt(100) == 0) {
        if (fireRainCooldown <= 0) {
          castFireRain();
        } else if (magmaArmorCooldown <= 0) {
          activateMagmaArmor();
        }
      }
    }

    // Visual effects
    if (this.level().isClientSide) {
      spawnParticles();
    }
  }

  private void spawnParticles() {
    // Always emit smoke and flame particles
    for (int i = 0; i < 2; ++i) {
      double d0 = this.random.nextGaussian() * 0.02D;
      double d1 = this.random.nextGaussian() * 0.02D;
      double d2 = this.random.nextGaussian() * 0.02D;
      this.level()
          .addParticle(
              ParticleTypes.FLAME,
              this.getRandomX(1.0D),
              this.getRandomY() + 0.5D,
              this.getRandomZ(1.0D),
              d0,
              d1,
              d2);
      this.level()
          .addParticle(
              ParticleTypes.SMOKE,
              this.getRandomX(1.0D),
              this.getRandomY() + 0.5D,
              this.getRandomZ(1.0D),
              d0,
              d1,
              d2);
    }

    // Phase-specific effects
    if (getPhase() >= 2) {
      this.level()
          .addParticle(
              ParticleTypes.LAVA,
              this.getRandomX(0.5D),
              this.getRandomY() + 0.5D,
              this.getRandomZ(0.5D),
              0.0D,
              0.1D,
              0.0D);
    }
  }

  private void triggerPhaseTransition(int newPhase) {
    if (!this.level().isClientSide) {
      // Play sound and visual effect
      this.level()
          .explode(
              this,
              this.getX(),
              this.getY(),
              this.getZ(),
              3.0F,
              false,
              Level.ExplosionInteraction.NONE);
      this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 0.5F);

      // Phase-specific effects
      switch (newPhase) {
        case 2 -> {
          // Increase movement and attack speed
          this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4D);
          this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(16.0D);
          this.bossEvent.setName(this.getDisplayName().copy().append(" - Phase 2: Raging Inferno"));
        }
        case 3 -> {
          // Further increase stats and enable all abilities
          this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5D);
          this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(20.0D);
          this.getAttribute(Attributes.ARMOR).setBaseValue(15.0D);
          this.bossEvent.setName(this.getDisplayName().copy().append(" - Final Phase: Apocalypse"));
        }
      }
    }
  }

  private void castFireRain() {
    if (!this.level().isClientSide) {
      this.fireRainCooldown = FIRE_RAIN_COOLDOWN;

      // Create fire rain in an area
      AABB area = this.getBoundingBox().inflate(20.0D, 10.0D, 20.0D);
      int fireBalls = 20 + this.random.nextInt(20);

      for (int i = 0; i < fireBalls; i++) {
        double x = area.minX + this.random.nextDouble() * (area.maxX - area.minX);
        double z = area.minZ + this.random.nextDouble() * (area.maxZ - area.minZ);
        double y = area.maxY;

        // Spawn fireball entity
        // (Implementation for fireball entity would go here)

        // Visual effect
        ((ServerLevel) this.level())
            .sendParticles(ParticleTypes.FLAME, x, y, z, 10, 0.5, 0.5, 0.5, 0.1);
      }

      this.playSound(SoundEvents.WITHER_SHOOT, 2.0F, 0.8F);
    }
  }

  private void activateMagmaArmor() {
    if (!this.level().isClientSide) {
      this.magmaArmorCooldown = MAGMA_ARMOR_COOLDOWN;

      // Set entity on fire and create explosion
      this.setSecondsOnFire(10);
      this.level()
          .explode(
              this,
              this.getX(),
              this.getY(),
              this.getZ(),
              2.0F,
              false,
              Level.ExplosionInteraction.NONE);

      // Heal and apply resistance
      this.heal(50.0F);
      // (Implementation for temporary resistance effect would go here)

      this.playSound(SoundEvents.BLAZE_SHOOT, 2.0F, 0.5F);
    }
  }

  // Getters and setters for synced data
  public int getPhase() {
    return this.entityData.get(PHASE);
  }

  private void setPhase(int phase) {
    this.entityData.set(PHASE, phase);
  }

  public boolean isCharging() {
    return this.entityData.get(IS_CHARGING);
  }

  private void setCharging(boolean charging) {
    this.entityData.set(IS_CHARGING, charging);
  }

  // Boss bar handling
  @Override
  public void startSeenByPlayer(ServerPlayer player) {
    super.startSeenByPlayer(player);
    this.bossEvent.addPlayer(player);
  }

  @Override
  public void stopSeenByPlayer(ServerPlayer player) {
    super.stopSeenByPlayer(player);
    this.bossEvent.removePlayer(player);
  }

  @Override
  public void onAddedToWorld() {
    super.onAddedToWorld();
    this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
  }

  @Override
  public void onRemovedFromWorld() {
    super.onRemovedFromWorld();
    this.bossEvent.removeAllPlayers();
  }

  // Custom move control for smooth movement
  static class InfernalSovereignMoveControl extends MoveControl {
    private final InfernalSovereignEntity sovereign;
    private int floatDuration;

    public InfernalSovereignMoveControl(InfernalSovereignEntity entity) {
      super(entity);
      this.sovereign = entity;
    }

    @Override
    public void tick() {
      if (this.operation == MoveControl.Operation.MOVE_TO) {
        if (this.floatDuration-- <= 0) {
          this.floatDuration += this.sovereign.getRandom().nextInt(5) + 2;
          Vec3 vec3 =
              new Vec3(
                  this.wantedX - this.sovereign.getX(),
                  this.wantedY - this.sovereign.getY(),
                  this.wantedZ - this.sovereign.getZ());
          double d0 = vec3.length();
          vec3 = vec3.normalize();

          if (this.canReach(vec3, Mth.ceil(d0))) {
            this.sovereign.setDeltaMovement(
                this.sovereign.getDeltaMovement().add(vec3.scale(0.1D)));
          } else {
            this.operation = MoveControl.Operation.WAIT;
          }
        }
      }
    }

    private boolean canReach(Vec3 vec, int steps) {
      AABB aabb = this.sovereign.getBoundingBox();

      for (int i = 1; i < steps; ++i) {
        aabb = aabb.move(vec);
        if (!this.sovereign.level().noCollision(this.sovereign, aabb)) {
          return false;
        }
      }

      return true;
    }
  }

  // Custom attack goal for charge attacks
  static class InfernalSovereignChargeAttackGoal extends Goal {
    private final InfernalSovereignEntity sovereign;
    private int chargeTime;
    private int chargeDuration;

    public InfernalSovereignChargeAttackGoal(InfernalSovereignEntity entity) {
      this.sovereign = entity;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      LivingEntity target = this.sovereign.getTarget();
      return target != null && target.isAlive() && this.sovereign.canAttack(target);
    }

    @Override
    public void start() {
      this.chargeTime = 0;
      this.sovereign.setCharging(true);
    }

    @Override
    public void stop() {
      this.sovereign.setCharging(false);
    }

    @Override
    public void tick() {
      this.chargeTime++;
      LivingEntity target = this.sovereign.getTarget();

      if (target != null) {
        // Look at target
        this.sovereign
            .getLookControl()
            .setLookAt(target, 10.0F, (float) this.sovereign.getMaxHeadXRot());

        // Charge at target
        if (this.chargeTime < 20) {
          // Wind up
          this.sovereign.getNavigation().stop();
        } else if (this.chargeTime < 40) {
          // Charge!
          if (this.chargeDuration <= 0) {
            this.chargeDuration = 20 + this.sovereign.getRandom().nextInt(20);

            // Set velocity toward target
            Vec3 vec3 = this.sovereign.getViewVector(1.0F);
            double speed = 1.5D + (this.sovereign.getPhase() * 0.5D);
            this.sovereign.setDeltaMovement(vec3.scale(speed));
            this.sovereign.playSound(
                SoundEvents.BLAZE_SHOOT,
                1.0F,
                0.4F / (this.sovereign.getRandom().nextFloat() * 0.4F + 0.8F));
          }

          this.chargeDuration--;

          // Damage entities in path
          List<LivingEntity> entities =
              this.sovereign
                  .level()
                  .getEntitiesOfClass(
                      LivingEntity.class,
                      this.sovereign.getBoundingBox().inflate(1.0D, 0.5D, 1.0D),
                      e -> e != this.sovereign && e.isAlive() && this.sovereign.canAttack(e));

          for (LivingEntity entity : entities) {
            entity.hurt(this.sovereign.damageSources().mobAttack(this.sovereign), 10.0F);
            entity.setSecondsOnFire(5);

            // Knockback
            Vec3 vec3 = entity.position().subtract(this.sovereign.position()).normalize();
            entity.push(vec3.x * 0.8D, 0.3D, vec3.z * 0.8D);
          }
        } else {
          // Reset charge
          this.chargeTime = 0;
          this.chargeDuration = 0;
        }
      }
    }
  }

  @Override
  public boolean isPersistenceRequired() {
    return true;
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false;
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  protected boolean isAffectedByFluids() {
    return false;
  }

  @Override
  public boolean fireImmune() {
    return true;
  }

  @Override
  public boolean canBeLeashed(Player player) {
    return false;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("Phase", this.getPhase());
    compound.putBoolean("Charging", this.isCharging());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    if (compound.contains("Phase")) {
      this.setPhase(compound.getInt("Phase"));
    }
    if (compound.contains("Charging")) {
      this.setCharging(compound.getBoolean("Charging"));
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.WITHER_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.WITHER_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.WITHER_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.WITHER_STEP, 0.15F, 1.0F);
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof LivingEntity) {
      target.setSecondsOnFire(5);
    }
    return flag;
  }
}
