package com.netroaki.chex.entity.crystalis;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
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

public class CryoMonarchEntity extends Monster {
  private static final EntityDataAccessor<Integer> DATA_PHASE =
      SynchedEntityData.defineId(CryoMonarchEntity.class, EntityDataSerializers.INT);
  private final BossEvent bossEvent =
      (BossEvent)
          (new BossEvent(
              this.getDisplayName(),
              BossEvent.BossBarColor.BLUE,
              BossEvent.BossBarOverlay.PROGRESS));
  private int phaseTransitionTimer = 0;
  private int shardStormCooldown = 0;
  private int frozenDomainCooldown = 0;
  private int glacialCollapseCooldown = 0;
  private int teleportCooldown = 0;

  public CryoMonarchEntity(EntityType<? extends Monster> type, Level world) {
    super(type, world);
    this.xpReward = 500;
    this.setHealth(this.getMaxHealth());
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 500.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.25D)
        .add(Attributes.ATTACK_DAMAGE, 12.0D)
        .add(Attributes.ARMOR, 10.0D)
        .add(Attributes.ARMOR_TOUGHNESS, 8.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
        .add(Attributes.FOLLOW_RANGE, 50.0D);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_PHASE, 1);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
  }

  @Override
  public void aiStep() {
    super.aiStep();

    // Update boss bar
    this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

    // Handle phase transitions
    if (this.phaseTransitionTimer > 0) {
      this.phaseTransitionTimer--;
      if (this.phaseTransitionTimer == 0) {
        this.performPhaseTransition();
      }
    }

    // Handle phase-specific cooldowns
    int phase = this.getPhase();
    if (phase == 1 && this.shardStormCooldown > 0) this.shardStormCooldown--;
    if (phase == 2 && this.frozenDomainCooldown > 0) this.frozenDomainCooldown--;
    if (phase == 3 && this.glacialCollapseCooldown > 0) this.glacialCollapseCooldown--;
    if (this.teleportCooldown > 0) this.teleportCooldown--;

    // Phase-specific AI
    switch (phase) {
      case 1 -> this.aiPhaseOne();
      case 2 -> this.aiPhaseTwo();
      case 3 -> this.aiPhaseThree();
    }
  }

  private void aiPhaseOne() {
    if (this.shardStormCooldown <= 0) {
      this.performShardStorm();
      this.shardStormCooldown = 100 + this.random.nextInt(100); // 5-10 second cooldown
    }

    // Check for phase transition
    if (this.getHealth() < this.getMaxHealth() * 0.7) {
      this.initiatePhaseTransition(2);
    }
  }

  private void aiPhaseTwo() {
    if (this.frozenDomainCooldown <= 0) {
      this.performFrozenDomain();
      this.frozenDomainCooldown = 150 + this.random.nextInt(100); // 7.5-12.5 second cooldown
    }

    // Check for phase transition
    if (this.getHealth() < this.getMaxHealth() * 0.3) {
      this.initiatePhaseTransition(3);
    }
  }

  private void aiPhaseThree() {
    if (this.glacialCollapseCooldown <= 0) {
      this.performGlacialCollapse();
      this.glacialCollapseCooldown = 200 + this.random.nextInt(100); // 10-15 second cooldown
    }
  }

  private void performShardStorm() {
    if (!this.level().isClientSide) {
      // Play sound
      this.level()
          .playSound(
              null,
              this.getX(),
              this.getY(),
              this.getZ(),
              SoundEvents.GLASS_BREAK,
              this.getSoundSource(),
              2.0F,
              0.5F + this.random.nextFloat() * 0.5F);

      // Create multiple shards in a spread
      int shardCount = 8 + this.random.nextInt(5);
      for (int i = 0; i < shardCount; i++) {
        // Calculate random direction
        double spread = 0.5D;
        double x = (this.random.nextDouble() - 0.5D) * spread;
        double y = (this.random.nextDouble() - 0.5D) * spread;
        double z = (this.random.nextDouble() - 0.5D) * spread;

        // Create and launch shard
        IceShardProjectile shard = new IceShardProjectile(this.level(), this);
        shard.setDamage(4.0F * (1.0F + 0.2F * this.getPhase())); // Scale damage with phase
        shard.setPos(this.getX(), this.getEyeY() - 0.5D, this.getZ());
        shard.shoot(x, y, z, 1.5F, 1.0F);
        this.level().addFreshEntity(shard);
      }

      // Add particle effects
      for (int i = 0; i < 20; i++) {
        double offsetX = (this.random.nextDouble() - 0.5D) * 2.0D;
        double offsetY = (this.random.nextDouble() - 0.5D) * 2.0D;
        double offsetZ = (this.random.nextDouble() - 0.5D) * 2.0D;
        this.level()
            .addParticle(
                ParticleTypes.SNOWFLAKE,
                this.getX() + offsetX,
                this.getY() + 1.0D + offsetY,
                this.getZ() + offsetZ,
                0,
                0,
                0);
      }
    }
  }

  private void performFrozenDomain() {
    if (!this.level().isClientSide) {
      // Play sound
      this.level()
          .playSound(
              null,
              this.getX(),
              this.getY(),
              this.getZ(),
              SoundEvents.GLASS_BREAK,
              this.getSoundSource(),
              2.0F,
              0.5F);

      // Create ice walls in a circle around the boss
      int radius = 5;
      BlockPos centerPos = this.blockPosition();

      // Create ice walls
      for (int i = -radius; i <= radius; i++) {
        for (int j = -2; j <= 3; j++) {
          // Circular wall
          for (int k = 0; k < 16; k++) {
            double angle = k * Math.PI * 2.0 / 16.0;
            int x = centerPos.getX() + (int) (Math.cos(angle) * radius);
            int z = centerPos.getZ() + (int) (Math.sin(angle) * radius);
            BlockPos wallPos = new BlockPos(x, centerPos.getY() + j, z);

            // Place packed ice blocks for the wall
            if (this.level().isEmptyBlock(wallPos)) {
              this.level().setBlockAndUpdate(wallPos, Blocks.PACKED_ICE.defaultBlockState());
            }
          }
        }
      }

      // Slow players in range
      double slowRange = radius * 2.0;
      for (Player player :
          this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(slowRange))) {
        if (player.distanceToSqr(this) <= slowRange * slowRange) {
          player.addEffect(
              new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2)); // Slowness III
          player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1)); // Weakness II
        }
      }

      // Summon ice minions (Shardlings)
      int minionCount = 2 + this.random.nextInt(3); // 2-4 minions
      for (int i = 0; i < minionCount; i++) {
        double angle = i * Math.PI * 2.0 / minionCount;
        double x = this.getX() + Math.cos(angle) * 3.0;
        double z = this.getZ() + Math.sin(angle) * 3.0;

        ShardlingEntity minion = ModEntities.SHARDLING.get().create(this.level());
        if (minion != null) {
          minion.setPos(x, this.getY(), z);
          minion.setTarget(this.getTarget());
          this.level().addFreshEntity(minion);
        }
      }

      // Add particle effects
      for (int i = 0; i < 50; i++) {
        double angle = this.random.nextDouble() * Math.PI * 2.0;
        double dist = this.random.nextDouble() * radius;
        double x = this.getX() + Math.cos(angle) * dist;
        double z = this.getZ() + Math.sin(angle) * dist;
        double y = this.getY() + (this.random.nextDouble() - 0.5) * 2.0;

        this.level().addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0, 0.1, 0);
      }
    }
  }

  private void performGlacialCollapse() {
    if (!this.level().isClientSide) {
      // Play sound
      this.level()
          .playSound(
              null,
              this.getX(),
              this.getY(),
              this.getZ(),
              SoundEvents.GLASS_BREAK,
              this.getSoundSource(),
              3.0F,
              0.5F);

      int radius = 8;
      BlockPos centerPos = this.blockPosition();

      // Create ice spikes in a circular pattern
      for (int i = -radius; i <= radius; i++) {
        for (int k = -radius; k <= radius; k++) {
          if (i * i + k * k <= radius * radius) {
            // Only place spikes in certain positions for a pattern
            if ((i + k) % 3 == 0 || (i - k) % 3 == 0) {
              // Find the ground position
              BlockPos spikePos =
                  new BlockPos(centerPos.getX() + i, centerPos.getY(), centerPos.getZ() + k);
              spikePos =
                  this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, spikePos).below();

              // Create a spike of packed ice
              int height = 2 + this.random.nextInt(3);
              for (int j = 0; j < height; j++) {
                BlockPos pos = spikePos.above(j);
                if (this.level().isEmptyBlock(pos)) {
                  this.level().setBlockAndUpdate(pos, Blocks.PACKED_ICE.defaultBlockState());
                }
              }

              // Damage entities on the spike
              for (LivingEntity entity :
                  this.level()
                      .getEntitiesOfClass(
                          LivingEntity.class, new AABB(spikePos).inflate(1.0, 2.0, 1.0))) {
                if (entity != this) {
                  entity.hurt(this.damageSources().mobAttack(this), 8.0F);
                  entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                }
              }
            }

            // Randomly spawn falling icicles from the ceiling
            if (this.random.nextInt(10) == 0) {
              BlockPos ceilingPos =
                  this.level()
                      .getHeightmapPos(
                          Heightmap.Types.MOTION_BLOCKING,
                          new BlockPos(centerPos.getX() + i, 0, centerPos.getZ() + k));

              if (ceilingPos.getY() > centerPos.getY() + 3) {
                FallingBlockEntity fallingBlock =
                    new FallingBlockEntity(
                        this.level(),
                        ceilingPos.getX() + 0.5,
                        ceilingPos.getY(),
                        ceilingPos.getZ() + 0.5,
                        Blocks.PACKED_ICE.defaultBlockState());
                fallingBlock.time = 1;
                fallingBlock.setHurtsEntities(8.0F, 1);
                this.level().addFreshEntity(fallingBlock);
              }
            }
          }
        }
      }

      // Create a shockwave effect
      for (int i = 0; i < 100; i++) {
        double angle = this.random.nextDouble() * Math.PI * 2.0;
        double dist = this.random.nextDouble() * radius;
        double x = this.getX() + Math.cos(angle) * dist;
        double z = this.getZ() + Math.sin(angle) * dist;

        // Spawn particles moving outward
        double vx = Math.cos(angle) * 0.5;
        double vz = Math.sin(angle) * 0.5;

        this.level()
            .sendParticles(ParticleTypes.SNOWFLAKE, x, this.getY() + 0.5, z, 0, vx, 0.1, vz, 0.5);
      }

      // Apply area damage and slowness
      for (LivingEntity entity :
          this.level()
              .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius))) {
        if (entity != this) {
          double distSq = entity.distanceToSqr(this);
          if (distSq <= radius * radius) {
            // Scale damage by distance
            float damage = (float) (12.0 * (1.0 - Math.sqrt(distSq) / radius));
            entity.hurt(this.damageSources().mobAttack(this), damage);

            // Apply slowness effect
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
          }
        }
      }
    }
  }

  private void initiatePhaseTransition(int newPhase) {
    this.phaseTransitionTimer = 40; // 2 second transition
    this.getNavigation().stop();
    this.setTarget(null);

    // Play transition sound
    if (!this.level().isClientSide) {
      this.level()
          .playSound(
              null,
              this.getX(),
              this.getY(),
              this.getZ(),
              SoundEvents.ENDER_DRAGON_GROWL,
              this.getSoundSource(),
              2.0F,
              0.5F);
    }

    // Teleport to a random nearby location
    this.teleportToRandomLocation();

    // Add transition particles
    if (this.level().isClientSide) {
      for (int i = 0; i < 50; i++) {
        double offsetX = (this.random.nextDouble() - 0.5D) * 4.0D;
        double offsetY = this.random.nextDouble() * 4.0D;
        double offsetZ = (this.random.nextDouble() - 0.5D) * 4.0D;
        this.level()
            .addParticle(
                ParticleTypes.ENCHANT,
                this.getX() + offsetX,
                this.getY() + offsetY,
                this.getZ() + offsetZ,
                0,
                0,
                0);
      }
    }
  }

  private void teleportToRandomLocation() {
    if (this.level().isClientSide || this.teleportCooldown > 0) {
      return;
    }

    // Try to find a valid teleport location
    for (int i = 0; i < 10; i++) {
      double x = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D;
      double y = this.getY() + (this.random.nextDouble() - 0.5D) * 4.0D;
      double z = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D;

      // Ensure the target position is valid
      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
      if (this.level().getBlockState(pos).isAir()
          && this.level().getBlockState(pos.below()).isSolidRender(this.level(), pos.below())) {

        // Teleport the entity
        this.teleportTo(x, y, z);
        this.teleportCooldown = 20; // 1 second cooldown

        // Play teleport sound
        this.level()
            .playSound(
                null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);

        // Spawn particles at both locations
        this.spawnParticlesAtPosition(x, y, z);
        break;
      }
    }
  }

  private void spawnParticlesAtPosition(double x, double y, double z) {
    if (this.level().isClientSide) {
      for (int i = 0; i < 30; i++) {
        double offsetX = (this.random.nextDouble() - 0.5D) * 2.0D;
        double offsetY = (this.random.nextDouble() - 0.5D) * 2.0D;
        double offsetZ = (this.random.nextDouble() - 0.5D) * 2.0D;
        this.level()
            .addParticle(ParticleTypes.PORTAL, x + offsetX, y + offsetY, z + offsetZ, 0, 0, 0);
      }
    }
  }

  private void performPhaseTransition() {
    int newPhase = this.getPhase() + 1;
    this.setPhase(newPhase);

    // Heal slightly on phase transition
    this.heal(this.getMaxHealth() * 0.2f);

    // TODO: Add phase transition effects and announcements
  }

  public int getPhase() {
    return this.entityData.get(DATA_PHASE);
  }

  public void setPhase(int phase) {
    this.entityData.set(DATA_PHASE, phase);
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
    compound.putInt("Phase", this.getPhase());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setPhase(compound.getInt("Phase"));
  }

  @Nullable
  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENDER_DRAGON_GROWL;
  }

  @Nullable
  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource) {
    return SoundEvents.ENDER_DRAGON_HURT;
  }

  @Nullable
  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENDER_DRAGON_DEATH;
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    return source.isBypassInvul() || super.isInvulnerableTo(source);
  }

  @Override
  public void checkDespawn() {
    // Prevent despawning
  }
}
