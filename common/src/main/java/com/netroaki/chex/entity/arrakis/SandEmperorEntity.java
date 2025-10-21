package com.netroaki.chex.entity.arrakis;

import com.netroaki.chex.init.SoundInit;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SandEmperorEntity extends Monster {
  private static final EntityDataAccessor<Integer> PHASE =
      SynchedEntityData.defineId(SandEmperorEntity.class, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Boolean> IS_BURROWED =
      SynchedEntityData.defineId(SandEmperorEntity.class, EntityDataSerializers.BOOLEAN);
  private static final EntityDataAccessor<Integer> BURROW_TICKS =
      SynchedEntityData.defineId(SandEmperorEntity.class, EntityDataSerializers.INT);

  private static final int MAX_BURROW_TICKS = 100;
  private static final int PHASE_1_HEALTH = 300; // 150 hearts
  private static final int PHASE_2_HEALTH = 150; // 75 hearts

  public SandEmperorEntity(EntityType<? extends Monster> type, Level level) {
    super(type, level);
    this.xpReward = 100;
    this.setHealth(this.getMaxHealth());
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 300.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 8.0D)
        .add(Attributes.ARMOR, 4.0D)
        .add(Attributes.FOLLOW_RANGE, 50.0D);
  }

  @Override
  protected void registerGoals() {
    // Basic AI
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(
        1,
        new MeleeAttackGoal(this, 1.0D, false) {
          @Override
          protected void checkAndPerformAttack(LivingEntity target, double distanceSquared) {
            if (distanceSquared <= this.getAttackReachSqr(target)
                && this.getTicksUntilNextAttack() <= 0) {
              // Play attack sound
              if (random.nextFloat() < 0.3f) {
                playSound(SoundInit.SAND_EMPEROR_TAIL_WHIP.get(), 1.0f, 1.0f);
              }
            }
            super.checkAndPerformAttack(target, distanceSquared);
          }
        });
    this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

    // Custom AI - TODO: Implement these goal classes
    // this.goalSelector.addGoal(4, new SandEmperorBurrowGoal(this));
    // this.goalSelector.addGoal(5, new SandEmperorStormSummonGoal(this));

    // Targeting
    this.targetSelector.addGoal(
        1,
        new NearestAttackableTargetGoal<>(this, Player.class, true) {
          @Override
          public boolean canUse() {
            boolean canUse = super.canUse();
            if (canUse) {
              // Play target acquired sound
              playSound(SoundInit.SAND_EMPEROR_ROAR.get(), 1.5f, 1.0f);
            }
            return canUse;
          }
        });

    // Remove default hurtByTarget goal to prevent targeting other mobs
    this.targetSelector.removeAllGoals(goal -> true);
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(PHASE, 1);
    this.entityData.define(IS_BURROWED, false);
    this.entityData.define(BURROW_TICKS, 0);
  }

  @Override
  public void tick() {
    super.tick();

    // Handle burrow state
    if (this.isBurrowed()) {
      int burrowTicks = this.getBurrowTicks() + 1;
      this.setBurrowTicks(burrowTicks);

      if (burrowTicks >= MAX_BURROW_TICKS) {
        this.setBurrowed(false);
        this.setBurrowTicks(0);
        // TODO: Add emerge effect and damage players in area
      } else if (burrowTicks % 20 == 0) {
        // TODO: Add burrow particles and sounds
      }
    }

    // Check for phase transitions
    if (this.getHealth() <= PHASE_2_HEALTH && this.getPhase() == 1) {
      this.setPhase(2);
      // TODO: Trigger phase transition effects
    }

    // Play ambient sounds randomly
    if (this.tickCount % 200 == 0 && this.random.nextInt(5) == 0 && !this.isSilent()) {
      if (!isBurrowed()) {
        this.playAmbientSound();
      }
    }

    // Play burrow/emerge sounds
    if (this.tickCount % 20 == 0) {
      if (isBurrowing() && !isBurrowed()) {
        if (getBurrowTicks() == 1) {
          // Just started burrowing
          this.playSound(SoundInit.SAND_EMPEROR_BURROW.get(), 1.5f, 1.0f);
        }
      } else if (isEmerging() && isBurrowed()) {
        if (getBurrowTicks() == MAX_BURROW_TICKS - 1) {
          // About to emerge
          this.playSound(SoundInit.SAND_EMPEROR_EMERGE.get(), 1.5f, 1.0f);
        }
      }
    }
  }

  // Phase management
  public int getPhase() {
    return this.entityData.get(PHASE);
  }

  public void setPhase(int phase) {
    if (phase >= 0 && phase <= 2 && phase != this.getPhase()) {
      this.entityData.set(PHASE, phase);
      // Play phase change sound and effects
      if (!this.level().isClientSide) {
        this.playSound(SoundInit.SAND_EMPEROR_PHASE_CHANGE.get(), 2.0f, 1.0f);
      }
    }
    // Update attributes based on phase
    this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D * (1 + (phase - 1) * 0.5));
    this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8.0D * phase);
  }

  // Burrow state management
  public boolean isBurrowed() {
    return this.entityData.get(IS_BURROWED);
  }

  public void setBurrowed(boolean burrowed) {
    this.entityData.set(IS_BURROWED, burrowed);
    this.noPhysics = burrowed;
    this.setNoGravity(burrowed);
  }

  public int getBurrowTicks() {
    return this.entityData.get(BURROW_TICKS);
  }

  public void setBurrowTicks(int ticks) {
    this.entityData.set(BURROW_TICKS, ticks);
  }

  public boolean isBurrowing() {
    return getBurrowTicks() > 0 && !isBurrowed();
  }

  public boolean isEmerging() {
    return getBurrowTicks() < 0 && isBurrowed();
  }

  public float getBurrowProgress(float partialTick) {
    int ticks = getBurrowTicks();
    if (ticks > 0) {
      return Math.min(1.0F, (float) ticks / MAX_BURROW_TICKS);
    } else if (ticks < 0) {
      return Math.max(0.0F, 1.0F + (float) ticks / MAX_BURROW_TICKS);
    }
    return isBurrowed() ? 1.0F : 0.0F;
  }

  public int getPhaseTransitionTicks() {
    // TODO: Implement phase transition tracking
    return 0;
  }

  public float getPhaseTransitionProgress() {
    // TODO: Implement phase transition progress
    return 0.0F;
  }

  // Override to handle custom death drops
  @Override
  protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
    super.dropCustomDeathLoot(source, looting, recentlyHit);

    // Always drop a Sand Core
    ItemStack sandCore =
        new ItemStack(com.netroaki.chex.registry.items.ArrakisItems.SAND_CORE.get());
    this.spawnAtLocation(sandCore);

    // Drop additional cores based on looting level
    if (looting > 0) {
      int extraCores = this.random.nextInt(looting + 1);
      if (extraCores > 0) {
        ItemStack extraStack =
            new ItemStack(
                com.netroaki.chex.registry.items.ArrakisItems.SAND_CORE.get(), extraCores);
        this.spawnAtLocation(extraStack);
      }
    }
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return isBurrowed() ? null : SoundInit.SAND_EMPEROR_AMBIENT.get();
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundInit.SAND_EMPEROR_HURT.get();
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundInit.SAND_EMPEROR_DEATH.get();
  }

  @Override
  protected float getSoundVolume() {
    return 2.0F;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(SoundEvents.WARDEN_STEP, 0.15F, 1.0F);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt("Phase", this.getPhase());
    tag.putBoolean("IsBurrowed", this.isBurrowed());
    tag.putInt("BurrowTicks", this.getBurrowTicks());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    if (tag.contains("Phase")) {
      this.setPhase(tag.getInt("Phase"));
    }
    if (tag.contains("IsBurrowed")) {
      this.setBurrowed(tag.getBoolean("IsBurrowed"));
    }
    if (tag.contains("BurrowTicks")) {
      this.setBurrowTicks(tag.getInt("BurrowTicks"));
    }
  }
}
