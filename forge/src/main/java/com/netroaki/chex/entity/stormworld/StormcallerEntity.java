package com.netroaki.chex.entity.stormworld;

import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class StormcallerEntity extends StormEntity {
  private static final EntityDataAccessor<Integer> DATA_LIGHTNING_CHARGE =
      SynchedEntityData.defineId(StormcallerEntity.class, EntityDataSerializers.INT);

  private int summonCooldown = 0;
  private int chargeTime = 0;
  private static final int MAX_CHARGE = 100;

  public StormcallerEntity(EntityType<? extends StormEntity> type, Level level) {
    super(type, level);
    this.xpReward = 15;
  }

  public static AttributeSupplier.Builder createStormcallerAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 40.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 5.0D)
        .add(Attributes.ARMOR, 5.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(1, new SummonLightningGoal(this));
    this.goalSelector.addGoal(2, new StormcallerAttackGoal(this, 1.0D, false));
    this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_LIGHTNING_CHARGE, 0);
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.level().isClientSide) {
      // Update summon cooldown
      if (summonCooldown > 0) {
        summonCooldown--;
      }

      // Charge up when in storm or when in combat
      if ((this.getTarget() != null || isStormPowered()) && chargeTime < MAX_CHARGE) {
        chargeTime++;
        if (chargeTime % 20 == 0) {
          this.entityData.set(DATA_LIGHTNING_CHARGE, (chargeTime * 100) / MAX_CHARGE);
        }
      } else if (chargeTime > 0) {
        chargeTime--;
        if (chargeTime % 20 == 0) {
          this.entityData.set(DATA_LIGHTNING_CHARGE, (chargeTime * 100) / MAX_CHARGE);
        }
      }

      // Visual effects when charged
      if (this.isFullyCharged() && this.tickCount % 5 == 0) {
        this.spawnChargedParticles();
      }
    }
  }

  public boolean isFullyCharged() {
    return chargeTime >= MAX_CHARGE;
  }

  public int getLightningCharge() {
    return this.entityData.get(DATA_LIGHTNING_CHARGE);
  }

  public boolean canSummonLightning() {
    return summonCooldown <= 0 && (isStormPowered() || isFullyCharged());
  }

  public void setSummonCooldown(int cooldown) {
    this.summonCooldown = cooldown;
    if (isStormPowered()) {
      this.summonCooldown /= 2; // Faster cooldown during storms
    }
  }

  private void spawnChargedParticles() {
    if (this.level() instanceof ServerLevel serverLevel) {
      // Spawn electric particles around the entity
      for (int i = 0; i < 5; i++) {
        double offsetX = (this.random.nextDouble() - 0.5) * this.getBbWidth() * 1.5;
        double offsetY = this.random.nextDouble() * this.getBbHeight();
        double offsetZ = (this.random.nextDouble() - 0.5) * this.getBbWidth() * 1.5;

        serverLevel.sendParticles(
            ParticleTypes.ELECTRIC_SPARK,
            this.getX() + offsetX,
            this.getY() + offsetY,
            this.getZ() + offsetZ,
            1,
            0,
            0,
            0,
            0.2);
      }
    }
  }

  @Override
  protected void onStormPowerChange(boolean isPowered) {
    super.onStormPowerChange(isPowered);
    if (isPowered) {
      // Double charge rate during storms
      this.chargeTime = Math.min(chargeTime * 2, MAX_CHARGE);
      this.entityData.set(DATA_LIGHTNING_CHARGE, (chargeTime * 100) / MAX_CHARGE);
    }
  }

  @Override
  public boolean doHurtTarget(Entity target) {
    boolean flag = super.doHurtTarget(target);
    if (flag && target instanceof LivingEntity living) {
      // Apply effects on hit
      int duration = isStormPowered() ? 200 : 100;
      living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 0));

      // Chance to chain lightning to nearby entities
      if (this.random.nextFloat() < 0.3f * (isStormPowered() ? 2 : 1)) {
        chainLightning(living, 3, 5.0f);
      }

      // Consume charge on hit
      if (isFullyCharged()) {
        chargeTime = 0;
        this.entityData.set(DATA_LIGHTNING_CHARGE, 0);
        this.level()
            .explode(
                this, this.getX(), this.getY(), this.getZ(), 2.0f, Level.ExplosionInteraction.MOB);
      }
    }
    return flag;
  }

  private void chainLightning(LivingEntity source, int chains, float range) {
    if (chains <= 0 || !(this.level() instanceof ServerLevel serverLevel)) return;

    AABB area =
        new AABB(
            source.getX() - range,
            source.getY() - range,
            source.getZ() - range,
            source.getX() + range,
            source.getY() + range,
            source.getZ() + range);

    // Find nearby entities to chain to
    for (LivingEntity target :
        serverLevel.getEntitiesOfClass(
            LivingEntity.class,
            area,
            e -> e != this && e != source && e.isAlive() && e.getHealth() > 0)) {
      // Damage the target
      target.hurt(this.damageSources().mobAttack(this), 2.0f);

      // Visual effect
      serverLevel.sendParticles(
          ParticleTypes.ELECTRIC_SPARK,
          source.getX(),
          source.getY() + source.getBbHeight() * 0.5,
          source.getZ(),
          10,
          0.5,
          0.5,
          0.5,
          0.1);

      // Play sound
      serverLevel.playSound(
          null,
          source.getX(),
          source.getY(),
          source.getZ(),
          SoundEvents.LIGHTNING_BOLT_THUNDER,
          this.getSoundSource(),
          0.5f,
          1.5f + this.random.nextFloat() * 0.5f);

      // Continue the chain
      if (chains > 1) {
        chainLightning(target, chains - 1, range * 0.7f);
      }

      // Only chain to one target per level
      break;
    }
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt("SummonCooldown", this.summonCooldown);
    tag.putInt("ChargeTime", this.chargeTime);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.summonCooldown = tag.getInt("SummonCooldown");
    this.chargeTime = tag.getInt("ChargeTime");
    this.entityData.set(DATA_LIGHTNING_CHARGE, (chargeTime * 100) / MAX_CHARGE);
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENDERMAN_SCREAM;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.ENDERMAN_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENDERMAN_DEATH;
  }

  // Custom attack goal that uses lightning attacks at range
  static class StormcallerAttackGoal extends MeleeAttackGoal {
    private final StormcallerEntity stormcaller;
    private int raiseArmTicks;

    public StormcallerAttackGoal(
        StormcallerEntity stormcaller, double speedModifier, boolean followingTargetEvenIfNotSeen) {
      super(stormcaller, speedModifier, followingTargetEvenIfNotSeen);
      this.stormcaller = stormcaller;
    }

    @Override
    public void start() {
      super.start();
      this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
      super.stop();
      this.stormcaller.setAggressive(false);
    }

    @Override
    public void tick() {
      LivingEntity target = this.stormcaller.getTarget();
      if (target == null) return;

      double distanceSq =
          this.stormcaller.distanceToSqr(target.getX(), target.getY(), target.getZ());

      // If target is too far, try to summon lightning instead of chasing
      if (distanceSq > 16.0D && this.stormcaller.canSummonLightning()) {
        this.stormcaller.getLookControl().setLookAt(target, 30.0F, 30.0F);
        ++this.raiseArmTicks;

        if (this.raiseArmTicks >= 20) {
          this.stormcaller.performRangedAttack();
          this.raiseArmTicks = -40; // Cooldown before next ranged attack
        }
      } else {
        // Normal melee attack
        super.tick();
      }
    }
  }

  // Goal for summoning lightning
  static class SummonLightningGoal extends Goal {
    private final StormcallerEntity stormcaller;
    private int chargeTime;

    public SummonLightningGoal(StormcallerEntity stormcaller) {
      this.stormcaller = stormcaller;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      LivingEntity target = this.stormcaller.getTarget();
      if (target == null || !target.isAlive()) {
        return false;
      }

      // Only use if target is far enough and we have line of sight
      double distanceSq = this.stormcaller.distanceToSqr(target);
      return this.stormcaller.canSummonLightning()
          && distanceSq > 16.0D
          && this.stormcaller.getSensing().hasLineOfSight(target);
    }

    @Override
    public void start() {
      this.chargeTime = 0;
      this.stormcaller.setAggressive(true);
      this.stormcaller.getNavigation().stop();
    }

    @Override
    public void stop() {
      this.chargeTime = 0;
    }

    @Override
    public void tick() {
      LivingEntity target = this.stormcaller.getTarget();
      if (target == null) return;

      // Look at target
      this.stormcaller.getLookControl().setLookAt(target, 30.0F, 30.0F);

      // Charge up
      if (++this.chargeTime >= 40) {
        // Perform the summoning
        this.stormcaller.performRangedAttack();
        this.chargeTime = -40; // Cooldown
      } else if (this.chargeTime > 0) {
        // Charging effect
        if (this.stormcaller.level() instanceof ServerLevel serverLevel) {
          Vec3 pos = this.stormcaller.position();
          serverLevel.sendParticles(
              ParticleTypes.ELECTRIC_SPARK,
              pos.x,
              pos.y + this.stormcaller.getBbHeight() * 0.8,
              pos.z,
              3,
              0.5,
              0.5,
              0.5,
              0.1);
        }
      }
    }
  }

  private void performRangedAttack() {
    if (this.getTarget() == null) return;

    LivingEntity target = this.getTarget();
    double d0 = target.getX() - this.getX();
    double d1 = target.getY(0.5) - this.getY(0.5);
    double d2 = target.getZ() - this.getZ();

    if (this.level() instanceof ServerLevel serverLevel) {
      // Spawn lightning at target
      LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
      if (lightning != null) {
        lightning.moveTo(target.getX(), target.getY(), target.getZ());
        serverLevel.addFreshEntity(lightning);

        // Play sound
        serverLevel.playSound(
            null,
            this.getX(),
            this.getY(),
            this.getZ(),
            SoundEvents.LIGHTNING_BOLT_THUNDER,
            this.getSoundSource(),
            2.0f,
            0.8f + this.random.nextFloat() * 0.4f);

        // Set cooldown
        this.setSummonCooldown(100);

        // Consume charge if not in storm
        if (!isStormPowered()) {
          this.chargeTime = Math.max(0, this.chargeTime - (MAX_CHARGE / 2));
          this.entityData.set(DATA_LIGHTNING_CHARGE, (chargeTime * 100) / MAX_CHARGE);
        }
      }
    }
  }
}
