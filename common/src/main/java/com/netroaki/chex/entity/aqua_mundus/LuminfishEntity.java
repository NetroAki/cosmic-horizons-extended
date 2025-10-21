package com.netroaki.chex.entity.aqua_mundus;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class LuminfishEntity extends AquaticEntity {
  private static final EntityDataAccessor<Integer> VARIANT =
      SynchedEntityData.defineId(LuminfishEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> GLOWING =
      SynchedEntityData.defineId(LuminfishEntity.class, EntityDataSerializers.BOOLEAN);

  public static final int NUM_VARIANTS = 4;
  private float glowIntensity = 0.0F;
  private int glowCooldown = 0;

  public LuminfishEntity(EntityType<? extends LuminfishEntity> type, Level level) {
    super(type, level);
    this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return AquaticEntity.createAttributes()
        .add(Attributes.MAX_HEALTH, 4.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.7D)
        .add(Attributes.ATTACK_DAMAGE, 1.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(VARIANT, 0);
    this.entityData.define(GLOWING, false);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
    this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
    this.goalSelector.addGoal(
        2,
        new AvoidEntityGoal<>(
            this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
    this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
    this.goalSelector.addGoal(6, new LuminfishGlowGoal(this));
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Update glowing effect
    if (!this.level().isClientSide) {
      if (this.glowCooldown > 0) {
        this.glowCooldown--;
        if (this.glowCooldown <= 0) {
          this.setGlowing(false);
        }
      }

      // Randomly trigger glowing in dark areas
      if (this.random.nextInt(100) == 0
          && this.level().getMaxLocalRawBrightness(this.blockPosition()) < 5) {
        this.triggerGlow(100 + this.random.nextInt(200));
      }
    } else {
      // Client-side glow intensity animation
      if (this.isGlowing()) {
        this.glowIntensity = Math.min(1.0F, this.glowIntensity + 0.05F);
      } else {
        this.glowIntensity = Math.max(0.0F, this.glowIntensity - 0.05F);
      }
    }
  }

  public void triggerGlow(int duration) {
    this.setGlowing(true);
    this.glowCooldown = Math.max(this.glowCooldown, duration);
  }

  public boolean isGlowing() {
    return this.entityData.get(GLOWING);
  }

  public void setGlowing(boolean glowing) {
    this.entityData.set(GLOWING, glowing);
  }

  public int getVariant() {
    return Mth.clamp(this.entityData.get(VARIANT), 0, NUM_VARIANTS - 1);
  }

  public void setVariant(int variant) {
    this.entityData.set(VARIANT, Mth.clamp(variant, 0, NUM_VARIANTS - 1));
  }

  public float getGlowIntensity(float partialTicks) {
    return this.glowIntensity;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putInt("Variant", this.getVariant());
    compound.putBoolean("Glowing", this.isGlowing());
    if (this.glowCooldown > 0) {
      compound.putInt("GlowCooldown", this.glowCooldown);
    }
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setVariant(compound.getInt("Variant"));
    this.setGlowing(compound.getBoolean("Glowing"));
    if (compound.contains("GlowCooldown")) {
      this.glowCooldown = compound.getInt("GlowCooldown");
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.TROPICAL_FISH_AMBIENT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.TROPICAL_FISH_DEATH;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.TROPICAL_FISH_HURT;
  }

  @Override
  protected SoundEvent getFlopSound() {
    return SoundEvents.TROPICAL_FISH_FLOP;
  }

  @Override
  public ItemStack getBucketItemStack() {
    return new ItemStack(Items.TROPICAL_FISH_BUCKET); // TODO: Replace with custom bucket
  }

  @Override
  public void saveToBucketTag(ItemStack stack) {
    super.saveToBucketTag(stack);
    CompoundTag compound = stack.getOrCreateTag();
    compound.putInt("BucketVariantTag", this.getVariant());
  }

  @Override
  public void loadFromBucketTag(CompoundTag tag) {
    super.loadFromBucketTag(tag);
    if (tag.contains("BucketVariantTag", 3)) {
      this.setVariant(tag.getInt("BucketVariantTag"));
    }
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
    return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
  }

  public static boolean checkLuminfishSpawnRules(
      EntityType<LuminfishEntity> type,
      LevelAccessor level,
      MobSpawnType spawnType,
      BlockPos pos,
      RandomSource random) {
    return level.getBlockState(pos).is(Blocks.WATER)
        && level.getBlockState(pos.above()).is(Blocks.WATER);
  }

  private static class LuminfishGlowGoal extends Goal {
    private final LuminfishEntity fish;
    private int cooldown;

    public LuminfishGlowGoal(LuminfishEntity fish) {
      this.fish = fish;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
      if (this.cooldown > 0) {
        this.cooldown--;
        return false;
      }

      // Only glow in dark water
      if (this.fish.level().getMaxLocalRawBrightness(this.fish.blockPosition()) > 7) {
        return false;
      }

      // Random chance to start glowing
      return this.fish.random.nextInt(200) == 0;
    }

    @Override
    public void start() {
      this.fish.triggerGlow(100 + this.fish.random.nextInt(200));
      this.cooldown = 200 + this.fish.random.nextInt(600);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
      return true;
    }
  }
}
