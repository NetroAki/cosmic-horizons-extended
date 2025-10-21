package com.netroaki.chex.entity.arrakis;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ShaiHuludEntity extends Monster {
  private static final EntityDataAccessor<Integer> PHASE =
      SynchedEntityData.defineId(ShaiHuludEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> IS_EMERGING =
      SynchedEntityData.defineId(ShaiHuludEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Boolean> IS_SUBMERGING =
      SynchedEntityData.defineId(ShaiHuludEntity.class, EntityDataSerializers.BOOLEAN);

  private int attackCooldown = 0;
  private int phaseChangeCooldown = 0;
  private int timeSinceLastAttack = 0;
  private BlockPos lastSurfacePos;

  public ShaiHuludEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 1000;
    this.setMaxUpStep(2.0F);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 1000.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3D)
        .add(Attributes.ATTACK_DAMAGE, 20.0D)
        .add(Attributes.ARMOR, 10.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(PHASE, 1);
    this.entityData.define(IS_EMERGING, false);
    this.entityData.define(IS_SUBMERGING, false);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.level().isClientSide) {
      // Handle attack cooldown
      if (this.attackCooldown > 0) {
        this.attackCooldown--;
      }

      // Handle phase change cooldown
      if (this.phaseChangeCooldown > 0) {
        this.phaseChangeCooldown--;
      }

      // Update time since last attack
      this.timeSinceLastAttack++;

      // Phase transition logic
      float healthRatio = this.getHealth() / this.getMaxHealth();
      int currentPhase = this.getPhase();

      if (currentPhase == 1 && healthRatio < 0.66F && phaseChangeCooldown <= 0) {
        this.setPhase(2);
        this.phaseChangeCooldown = 200; // 10 seconds cooldown
      } else if (currentPhase == 2 && healthRatio < 0.33F && phaseChangeCooldown <= 0) {
        this.setPhase(3);
        this.phaseChangeCooldown = 200; // 10 seconds cooldown
      }

      // Handle burrow/emerge logic
      if (this.isEmerging()) {
        // Handle emerging animation/effects
        if (this.tickCount % 20 == 0) {
          this.level()
              .levelEvent(
                  2001,
                  this.blockPosition(),
                  net.minecraft.world.level.block.Blocks.SAND
                      .defaultBlockState()
                      .getBlock()
                      .defaultBlockState()
                      .getBlock()
                      .getId());
        }

        if (this.tickCount % 40 == 0) {
          this.setEmerging(false);
        }
      } else if (this.isSubmerging()) {
        // Handle submerging animation/effects
        if (this.tickCount % 20 == 0) {
          this.level()
              .levelEvent(
                  2001,
                  this.blockPosition(),
                  net.minecraft.world.level.block.Blocks.SAND
                      .defaultBlockState()
                      .getBlock()
                      .defaultBlockState()
                      .getBlock()
                      .getId());
        }

        if (this.tickCount % 40 == 0) {
          this.setSubmerging(false);
          this.setInvisible(true);
          this.setNoGravity(true);
          this.setNoPhysics(true);
        }
      } else if (this.isInvisible()) {
        // Handle movement while burrowed
        if (this.timeSinceLastAttack > 100) { // 5 seconds
          this.moveToTarget();
        }
      }
    }
  }

  private void moveToTarget() {
    LivingEntity target = this.getTarget();
    if (target != null) {
      // Move towards target while burrowed
      Vec3 targetPos = target.position();
      this.setPos(targetPos.x, targetPos.y - 2, targetPos.z);

      // Random chance to emerge near target
      if (this.random.nextFloat() < 0.1F) {
        this.emergesAt(target.blockPosition().above(2));
      }
    }
  }

  public void emergesAt(BlockPos pos) {
    this.setPos(pos.getX(), pos.getY(), pos.getZ());
    this.setInvisible(false);
    this.setNoGravity(false);
    this.setNoPhysics(false);
    this.setEmerging(true);
    this.playSound(SoundEvents.SAND_BREAK, 2.0F, 0.5F);
    this.lastSurfacePos = this.blockPosition();
  }

  public void submerges() {
    this.setSubmerging(true);
    this.playSound(SoundEvents.SAND_FALL, 2.0F, 0.5F);
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    // Reduce damage when burrowed
    if (this.isInvisible()) {
      amount *= 0.1F; // 90% damage reduction when burrowed
    }
    return super.hurt(source, amount);
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.HOGLIN_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENDER_DRAGON_DEATH;
  }

  // Getters and setters for synced data
  public int getPhase() {
    return this.entityData.get(PHASE);
  }

  public void setPhase(int phase) {
    this.entityData.set(PHASE, phase);
    // Add phase transition effects here
    this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 2.0F, 0.5F);
  }

  public boolean isEmerging() {
    return this.entityData.get(IS_EMERGING);
  }

  public void setEmerging(boolean emerging) {
    this.entityData.set(IS_EMERGING, emerging);
  }

  public boolean isSubmerging() {
    return this.entityData.get(IS_SUBMERGING);
  }

  public void setSubmerging(boolean submerging) {
    this.entityData.set(IS_SUBMERGING, submerging);
  }

  @Override
  public boolean isPersistenceRequired() {
    return true; // Prevent despawning
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false; // Don't despawn
  }

  @Override
  public void checkDespawn() {
    // Don't despawn
  }

  @Override
  public boolean canBeLeashed(Player player) {
    return false; // Can't be leashed
  }

  @Override
  protected boolean isAffectedByFluids() {
    return false; // Not affected by water/lava
  }

  @Override
  public boolean isPushable() {
    return false; // Can't be pushed by entities
  }

  @Override
  protected void pushEntities() {
    // Push entities away when emerging
    if (this.isEmerging()) {
      AABB aabb = this.getBoundingBox().inflate(5.0D);
      this.level()
          .getEntities(this, aabb)
          .forEach(
              entity -> {
                if (entity instanceof LivingEntity && !(entity instanceof ShaiHuludEntity)) {
                  Vec3 pushVec =
                      entity.position().subtract(this.position()).normalize().scale(2.0D);
                  entity.push(pushVec.x, 0.5D, pushVec.z);
                  entity.hurt(this.damageSources().mobAttack(this), 10.0F);
                }
              });
    }
  }
}
