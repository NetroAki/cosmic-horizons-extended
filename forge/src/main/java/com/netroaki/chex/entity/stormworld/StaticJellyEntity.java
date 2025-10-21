package com.netroaki.chex.entity.stormworld;

import com.netroaki.chex.world.stormworld.weather.StormworldWeatherManager;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class StaticJellyEntity extends StormEntity {
  private static final EntityDataAccessor<Integer> DATA_CHARGE_LEVEL =
      SynchedEntityData.defineId(StaticJellyEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING =
      SynchedEntityData.defineId(StaticJellyEntity.class, EntityDataSerializers.BOOLEAN);

  private int dischargeCooldown = 0;
  private int chargeAbsorptionCooldown = 0;
  private int glowTicks = 0;

  public StaticJellyEntity(EntityType<? extends StormEntity> type, Level level) {
    super(type, level);
    this.xpReward = 8;
  }

  public static AttributeSupplier.Builder createStaticJellyAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 30.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.2D)
        .add(Attributes.ATTACK_DAMAGE, 2.0D)
        .add(Attributes.ARMOR, 4.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new StaticJellyDischargeGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_CHARGE_LEVEL, 0);
    this.entityData.define(DATA_IS_CHARGING, false);
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.level().isClientSide) {
      // Update charge level based on storm state
      if (this.tickCount % 20 == 0) {
        updateChargeFromEnvironment();
      }

      // Update cooldowns
      if (this.dischargeCooldown > 0) {
        this.dischargeCooldown--;
      }

      if (this.chargeAbsorptionCooldown > 0) {
        this.chargeAbsorptionCooldown--;
      }

      // Visual effects when charged
      if (this.getChargeLevel() > 0) {
        if (this.glowTicks <= 0) {
          this.glowTicks = 10;
          this.spawnGlowParticles();
        } else {
          this.glowTicks--;
        }
      }
    }
  }

  private void updateChargeFromEnvironment() {
    if (this.chargeAbsorptionCooldown <= 0) {
      int currentCharge = this.getChargeLevel();

      // Gain charge from storms
      if (StormworldWeatherManager.isStormActive() && this.random.nextFloat() < 0.3f) {
        this.setChargeLevel(Math.min(currentCharge + 1, 5));
        this.chargeAbsorptionCooldown = 20; // Prevent rapid charging
      }

      // Lose charge over time
      if (currentCharge > 0 && this.random.nextFloat() < 0.1f) {
        this.setChargeLevel(currentCharge - 1);
      }
    }
  }

  private void spawnGlowParticles() {
    if (this.level() instanceof ServerLevel serverLevel) {
      int charge = this.getChargeLevel();
      float size = this.getBbWidth() * 0.8f;

      for (int i = 0; i < charge * 2; i++) {
        double offsetX = (this.random.nextDouble() - 0.5) * size;
        double offsetY = this.random.nextDouble() * this.getBbHeight();
        double offsetZ = (this.random.nextDouble() - 0.5) * size;

        serverLevel.sendParticles(
            ParticleTypes.ELECTRIC_SPARK,
            this.getX() + offsetX,
            this.getY() + offsetY,
            this.getZ() + offsetZ,
            1,
            0,
            0,
            0,
            0.1);
      }
    }
  }

  public int getChargeLevel() {
    return this.entityData.get(DATA_CHARGE_LEVEL);
  }

  public void setChargeLevel(int level) {
    int oldLevel = this.getChargeLevel();
    this.entityData.set(DATA_CHARGE_LEVEL, Mth.clamp(level, 0, 5));

    if (level > oldLevel) {
      // Play charge sound
      this.playSound(
          SoundEvents.LIGHTNING_BOLT_THUNDER, 0.5f, 1.5f + this.random.nextFloat() * 0.5f);
    }
  }

  public boolean isCharging() {
    return this.entityData.get(DATA_IS_CHARGING);
  }

  public void setCharging(boolean charging) {
    this.entityData.set(DATA_IS_CHARGING, charging);
  }

  public boolean canDischarge() {
    return this.getChargeLevel() > 0 && this.dischargeCooldown <= 0;
  }

  public void performDischarge() {
    if (this.level() instanceof ServerLevel serverLevel) {
      int charge = this.getChargeLevel();
      float range = 3.0f + (charge * 0.5f);
      float damage = 2.0f + (charge * 0.5f);

      // Create AOE damage and effects
      AABB aabb = this.getBoundingBox().inflate(range);
      for (LivingEntity target :
          this.level()
              .getEntitiesOfClass(
                  LivingEntity.class, aabb, e -> e != this && e.isAlive() && this.canAttack(e))) {
        // Damage and apply effects
        target.hurt(this.damageSources().mobAttack(this), damage);
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));

        // Visual effect
        if (serverLevel != null) {
          Vec3 startPos = this.position().add(0, this.getBbHeight() * 0.5, 0);
          Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
          spawnLightningBeam(serverLevel, startPos, targetPos, charge);
        }
      }

      // Play sound
      this.playSound(
          SoundEvents.LIGHTNING_BOLT_THUNDER, 1.0f, 0.5f + this.random.nextFloat() * 0.5f);

      // Reset charge and set cooldown
      this.setChargeLevel(0);
      this.dischargeCooldown = 100 + (charge * 20);
    }
  }

  private void spawnLightningBeam(ServerLevel level, Vec3 start, Vec3 end, int segments) {
    double distance = start.distanceTo(end);
    Vec3 direction = end.subtract(start).normalize();

    for (int i = 0; i < segments; i++) {
      double progress = (double) i / (segments - 1);
      Vec3 segmentStart = start.add(direction.scale(distance * progress));

      // Add some randomness to the beam
      double offsetX = (this.random.nextDouble() - 0.5) * 0.5;
      double offsetY = (this.random.nextDouble() - 0.5) * 0.5;
      double offsetZ = (this.random.nextDouble() - 0.5) * 0.5;

      level.sendParticles(
          ParticleTypes.ELECTRIC_SPARK,
          segmentStart.x + offsetX,
          segmentStart.y + offsetY,
          segmentStart.z + offsetZ,
          3,
          0.1,
          0.1,
          0.1,
          0.1);
    }
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    // Absorb lightning damage to gain charge
    if (source.is(net.minecraft.world.damagesource.DamageTypes.LIGHTNING_BOLT)) {
      this.setChargeLevel(Math.min(this.getChargeLevel() + 3, 5));
      return false; // Immune to lightning damage
    }

    // Chance to discharge when hit
    if (this.getChargeLevel() > 0 && this.random.nextFloat() < 0.3f) {
      this.performDischarge();
    }

    return super.hurt(source, amount);
  }

  @Override
  public void thunderHit(ServerLevel level, LightningBolt lightning) {
    // Absorb lightning to gain charge
    this.setChargeLevel(5); // Max charge from direct hit
    this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 1.0f, 0.5f);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt("ChargeLevel", this.getChargeLevel());
    tag.putInt("DischargeCooldown", this.dischargeCooldown);
    tag.putInt("ChargeAbsorptionCooldown", this.chargeAbsorptionCooldown);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.setChargeLevel(tag.getInt("ChargeLevel"));
    this.dischargeCooldown = tag.getInt("DischargeCooldown");
    this.chargeAbsorptionCooldown = tag.getInt("ChargeAbsorptionCooldown");
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.SLIME_SQUISH_SMALL;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.SLIME_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.SLIME_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.SLIME_SQUISH_SMALL, 0.15F, 1.0F);
  }

  // Custom AI Goal for discharge attack
  static class StaticJellyDischargeGoal extends Goal {
    private final StaticJellyEntity jelly;
    private int chargeTime;

    public StaticJellyDischargeGoal(StaticJellyEntity jelly) {
      this.jelly = jelly;
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      LivingEntity target = this.jelly.getTarget();
      if (target == null || !target.isAlive()) {
        return false;
      }

      // Only use if target is close enough and we have charge
      double distanceSq = this.jelly.distanceToSqr(target);
      return this.jelly.canDischarge() && distanceSq <= 16.0D;
    }

    @Override
    public boolean canContinueToUse() {
      return this.chargeTime > 0
          && this.jelly.getTarget() != null
          && this.jelly.getTarget().isAlive();
    }

    @Override
    public void start() {
      this.chargeTime = 20 + this.jelly.getRandom().nextInt(10);
      this.jelly.setCharging(true);
      this.jelly.getNavigation().stop();
    }

    @Override
    public void stop() {
      this.chargeTime = 0;
      this.jelly.setCharging(false);
    }

    @Override
    public void tick() {
      LivingEntity target = this.jelly.getTarget();
      if (target == null) return;

      // Look at target
      this.jelly.getLookControl().setLookAt(target, 30.0F, 30.0F);

      if (this.chargeTime > 0) {
        this.chargeTime--;

        // Visual effect while charging
        if (this.jelly.level() instanceof ServerLevel serverLevel) {
          Vec3 pos = this.jelly.position();
          serverLevel.sendParticles(
              ParticleTypes.ELECTRIC_SPARK,
              pos.x,
              pos.y + this.jelly.getBbHeight() * 0.5,
              pos.z,
              5,
              0.5,
              0.5,
              0.5,
              0.1);
        }

        // Perform discharge at the end of charge
        if (this.chargeTime == 0) {
          this.jelly.performDischarge();
        }
      }
    }
  }
}
