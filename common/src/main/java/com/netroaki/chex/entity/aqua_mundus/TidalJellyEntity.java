package com.netroaki.chex.entity.aqua_mundus;

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
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TidalJellyEntity extends AquaticEntity {
  private static final EntityDataAccessor<Integer> PULSE_TICKS =
      SynchedEntityData.defineId(TidalJellyEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> VARIANT =
      SynchedEntityData.defineId(TidalJellyEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Float> TARGET_DEPTH =
      SynchedEntityData.defineId(TidalJellyEntity.class, EntityDataSerializers.FLOAT);

  private static final int PULSE_INTERVAL = 40;
  private static final int NUM_VARIANTS = 3;

  private float tentacleMovement = 0.0F;
  private float prevTentacleMovement = 0.0F;
  private float bodyRotation = 0.0F;
  private float prevBodyRotation = 0.0F;
  private float rotationVelocity;

  public TidalJellyEntity(EntityType<? extends TidalJellyEntity> type, Level level) {
    super(type, level);
    this.moveControl = new SmoothSwimmingMoveControl(this, 20, 10, 0.02F, 0.1F, true);
    this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    this.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 0.0F);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return AquaticEntity.createAttributes()
        .add(Attributes.MAX_HEALTH, 50.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.2D)
        .add(Attributes.ATTACK_DAMAGE, 3.0D)
        .add(Attributes.ARMOR, 4.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
        .add(Attributes.FOLLOW_RANGE, 32.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(PULSE_TICKS, 0);
    this.entityData.define(VARIANT, 0);
    this.entityData.define(TARGET_DEPTH, 0.0F);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new TidalJellyFloatGoal(this));
    this.goalSelector.addGoal(1, new TidalJellySwimGoal(this));
    this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));

    // Neutral behavior - only attacks when provoked
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
  }

  @Override
  public void aiStep() {
    super.aiStep();

    this.prevTentacleMovement = this.tentacleMovement;
    this.prevBodyRotation = this.bodyRotation;

    // Update pulse animation
    int pulseTicks = this.getPulseTicks();
    if (pulseTicks > 0) {
      this.setPulseTicks(pulseTicks - 1);
    } else if (this.random.nextInt(100) == 0) {
      this.setPulseTicks(PULSE_INTERVAL);
    }

    // Update tentacle movement
    if (!this.isInWaterOrBubble()) {
      this.tentacleMovement = Mth.lerp(0.05F, this.tentacleMovement, 0.0F);
    } else {
      this.tentacleMovement = Mth.lerp(0.05F, this.tentacleMovement, 1.0F);

      // Gentle bobbing motion
      if (!this.isNoAi()) {
        this.setDeltaMovement(
            this.getDeltaMovement().add(0.0D, Math.sin(this.tickCount * 0.05D) * 0.002D, 0.0D));
      }
    }

    // Update body rotation
    this.bodyRotation = this.prevBodyRotation + this.rotationVelocity;

    // Randomly change rotation velocity
    if (this.level().isClientSide) {
      if (this.random.nextInt(50) == 0) {
        this.rotationVelocity = (this.random.nextFloat() - 0.5F) * 0.5F;
      }
    }

    // Emit particles
    if (this.level().isClientSide && this.isInWater()) {
      if (this.random.nextFloat() < 0.1F) {
        for (int i = 0; i < 2; i++) {
          this.level()
              .addParticle(
                  ParticleTypes.BUBBLE,
                  this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                  this.getY() + (this.random.nextDouble() - 0.5) * this.getBbHeight(),
                  this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                  0.0D,
                  0.0D,
                  0.0D);
        }
      }

      // Glow particles
      if (this.getPulseTicks() > PULSE_INTERVAL - 10) {
        float pulse = (float) (PULSE_INTERVAL - this.getPulseTicks()) / 10.0F;
        int count = 3 + this.random.nextInt(3);

        for (int i = 0; i < count; i++) {
          float angle = this.random.nextFloat() * (float) Math.PI * 2.0F;
          float dist = 0.5F + this.random.nextFloat() * 0.5F;
          float x = (float) (Math.cos(angle) * dist);
          float z = (float) (Math.sin(angle) * dist);

          this.level()
              .addParticle(
                  ParticleTypes.ELECTRIC_SPARK,
                  this.getX() + x,
                  this.getY() + (this.random.nextDouble() - 0.5) * 0.5,
                  this.getZ() + z,
                  x * 0.02,
                  0.05,
                  z * 0.02);
        }
      }
    }
  }

  @Override
  public void travel(Vec3 travelVector) {
    if (this.isEffectiveAi() && this.isInWater()) {
      this.moveRelative(0.01F, travelVector);
      this.move(MoverType.SELF, this.getDeltaMovement());

      // Gentle floating motion
      float targetY = this.getTargetDepth();
      float currentY = (float) this.getY();

      if (Math.abs(targetY - currentY) > 0.1F) {
        double motionY = Math.signum(targetY - currentY) * 0.01D;
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, motionY, 0.0D));
      }

      this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
    } else {
      super.travel(travelVector);
    }
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    // Reduce damage from non-player sources when in water
    if (source.getEntity() == null && this.isInWater()) {
      amount = amount * 0.5F;
    }

    // Chance to release ink cloud when damaged
    if (!this.level().isClientSide && this.random.nextFloat() < 0.3F) {
      this.spawnInkCloud();
    }

    return super.hurt(source, amount);
  }

  private void spawnInkCloud() {
    if (this.level() instanceof ServerLevel serverLevel) {
      for (int i = 0; i < 30; i++) {
        double x = this.getX() + (this.random.nextDouble() - 0.5) * 3.0;
        double y = this.getY() + (this.random.nextDouble() - 0.5) * 2.0;
        double z = this.getZ() + (this.random.nextDouble() - 0.5) * 3.0;

        serverLevel.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
      }

      // Apply blindess to nearby entities
      for (LivingEntity entity :
          this.level()
              .getEntitiesOfClass(
                  LivingEntity.class,
                  this.getBoundingBox().inflate(4.0D),
                  (e) -> e != this && e.isInWater())) {
        // TODO: Apply custom effect instead of blindness
        // entity.addEffect(new MobEffectInstance(Effects.BLINDNESS, 100, 0));
      }
    }
  }

  public int getPulseTicks() {
    return this.entityData.get(PULSE_TICKS);
  }

  public void setPulseTicks(int ticks) {
    this.entityData.set(PULSE_TICKS, ticks);
  }

  public int getVariant() {
    return Mth.clamp(this.entityData.get(VARIANT), 0, NUM_VARIANTS - 1);
  }

  public void setVariant(int variant) {
    this.entityData.set(VARIANT, Mth.clamp(variant, 0, NUM_VARIANTS - 1));
  }

  public float getTargetDepth() {
    return this.entityData.get(TARGET_DEPTH);
  }

  public void setTargetDepth(float depth) {
    this.entityData.set(TARGET_DEPTH, depth);
  }

  public float getTentacleMovement(float partialTicks) {
    return Mth.lerp(partialTicks, this.prevTentacleMovement, this.tentacleMovement);
  }

  public float getBodyRotation(float partialTicks) {
    return Mth.lerp(partialTicks, this.prevBodyRotation, this.bodyRotation);
  }

  public float getPulseIntensity(float partialTicks) {
    int pulseTicks = this.getPulseTicks();
    if (pulseTicks > PULSE_INTERVAL - 10) {
      float progress = (PULSE_INTERVAL - pulseTicks + partialTicks) / 10.0F;
      return Mth.sin(progress * (float) Math.PI) * 0.5F + 0.5F;
    }
    return 0.0F;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("PulseTicks", this.getPulseTicks());
    compound.putInt("Variant", this.getVariant());
    compound.putFloat("TargetDepth", this.getTargetDepth());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setPulseTicks(compound.getInt("PulseTicks"));
    this.setVariant(compound.getInt("Variant"));

    if (compound.contains("TargetDepth")) {
      this.setTargetDepth(compound.getFloat("TargetDepth"));
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.GUARDIAN_AMBIENT;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.GUARDIAN_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.GUARDIAN_DEATH;
  }

  @Override
  protected SoundEvent getFlopSound() {
    return SoundEvents.GUARDIAN_FLOP;
  }

  @Override
  protected ItemStack getBucketItemStack() {
    return new ItemStack(Items.INK_SAC); // TODO: Replace with custom bucket
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor level,
      DifficultyInstance difficulty,
      MobSpawnType reason,
      @Nullable SpawnGroupData spawnData,
      @Nullable CompoundTag dataTag) {
    this.setVariant(this.random.nextInt(NUM_VARIANTS));

    // Set initial target depth
    if (reason == MobSpawnType.NATURAL || reason == MobSpawnType.SPAWNER) {
      this.setTargetDepth(this.getY() + (this.random.nextFloat() - 0.5F) * 4.0F);
    }

    return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
  }

  public static boolean checkTidalJellySpawnRules(
      EntityType<TidalJellyEntity> type,
      LevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    return level.getBlockState(pos).is(Blocks.WATER)
        && level.getBlockState(pos.above()).is(Blocks.WATER)
        && pos.getY() < level.getSeaLevel() - 10;
  }

  private static class TidalJellyFloatGoal extends Goal {
    private final TidalJellyEntity jelly;

    public TidalJellyFloatGoal(TidalJellyEntity jelly) {
      this.jelly = jelly;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
      return this.jelly.isInWater() && this.jelly.getRandom().nextFloat() < 0.02F;
    }

    @Override
    public void start() {
      // Slight vertical movement
      float depthChange = (this.jelly.getRandom().nextFloat() - 0.5F) * 4.0F;
      float newDepth =
          Mth.clamp(
              this.jelly.getY() + depthChange,
              this.jelly.level().getMinBuildHeight() + 10,
              this.jelly.level().getSeaLevel() - 5);

      this.jelly.setTargetDepth(newDepth);
    }
  }

  private static class TidalJellySwimGoal extends RandomSwimmingGoal {
    private final TidalJellyEntity jelly;

    public TidalJellySwimGoal(TidalJellyEntity jelly) {
      super(jelly, 1.0D, 20);
      this.jelly = jelly;
    }

    @Override
    public boolean canUse() {
      return super.canUse() && this.jelly.getRandom().nextFloat() < 0.1F;
    }

    @Override
    public boolean canContinueToUse() {
      return super.canContinueToUse() && this.jelly.getRandom().nextFloat() < 0.1F;
    }
  }
}
